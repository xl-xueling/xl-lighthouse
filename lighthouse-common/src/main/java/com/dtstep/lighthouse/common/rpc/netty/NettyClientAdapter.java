package com.dtstep.lighthouse.common.rpc.netty;
/*
 * Copyright (C) 2022-2024 XueLing.雪灵
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import com.dtstep.lighthouse.common.entity.rpc.RpcRequest;
import com.dtstep.lighthouse.common.entity.rpc.RpcResponse;
import com.dtstep.lighthouse.common.serializer.KryoSerializer;
import com.dtstep.lighthouse.common.util.IpUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.pool.*;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class NettyClientAdapter {

    private static final Logger logger = LoggerFactory.getLogger(NettyClientAdapter.class);

    private static final List<InetSocketAddress> addressList = new ArrayList<>();

    private static ChannelPoolMap<InetSocketAddress, ChannelPool> poolMap;

    private static final int maxConnections = 10;

    private static final NettyClientHandler clientHandler = new NettyClientHandler();

    private static final NettyClientAdapter nettyClientAdapter = new NettyClientAdapter();

    private NettyClientAdapter(){}

    public static NettyClientAdapter instance(){
        return nettyClientAdapter;
    }

    public void init(String locators) throws Exception {
        String[] locatorArr = locators.split(",");
        for (String conf : locatorArr) {
            String[] arr = conf.split(":");
            String ip = arr[0];
            int port = Integer.parseInt(arr[1]);
            InetSocketAddress inetSocketAddress = new InetSocketAddress(ip,port);
            if(!addressList.contains(inetSocketAddress) && IpUtils.checkIpPort(ip,port)){
                addressList.add(inetSocketAddress);
            }
        }
        connect();
    }

    public void connect(){
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioSocketChannel.class);
        poolMap = new AbstractChannelPoolMap<InetSocketAddress, ChannelPool>() {

            @Override
            protected ChannelPool newPool(InetSocketAddress key) {

                ChannelPool channelPool = new FixedChannelPool(bootstrap.remoteAddress(key), new ChannelPoolHandler() {
                    @Override
                    public void channelReleased(Channel ch) throws Exception {
                        if(logger.isDebugEnabled()){
                            logger.debug("channel released,id:" + ch.id());
                        }
                    }

                    @Override
                    public void channelAcquired(Channel ch) throws Exception {
                        if(logger.isDebugEnabled()){
                            logger.debug("channel acquired,id:" + ch.id());
                        }
                    }

                    @Override
                    public void channelCreated(Channel ch) throws Exception {
                        if(logger.isInfoEnabled()){
                            logger.info("Netty channel created,id:" + ch.id());
                        }
                        int fieldLength = 4;
                        ch.pipeline()
                                .addLast(new IdleStateHandler(0, 30, 0, TimeUnit.SECONDS))
                                .addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, fieldLength, 0, fieldLength))
                                .addLast(new LengthFieldPrepender(fieldLength))
                                .addLast("encoder", new RpcEncoder(RpcRequest.class, new KryoSerializer()))
                                .addLast("decoder", new RpcDecoder(RpcResponse.class, new KryoSerializer()))
                                .addLast(clientHandler);
                    }
                }, maxConnections);

                channelPool.acquire().addListener((Future<Channel> future) -> {
                    if (future.isSuccess()) {
                        logger.info("Netty create channel success,id:{}",future.getNow().id());
                        Channel channel = future.getNow();
                        channelPool.release(channel);
                    } else {
                        logger.info("Netty create channel failed,id:{}",future.getNow().id());
                    }
                });
                return channelPool;
            }
        };
    }

    public static ChannelPoolMap<InetSocketAddress, ChannelPool> getPoolMap(){
        return poolMap;
    }

    public <T> T create(Class<?> clazz){
        RemoteProxy proxy = new RemoteProxy(addressList,clazz);
        Class<?> [] interfaces = clazz.isInterface() ? new Class[]{clazz} : clazz.getInterfaces();
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(),interfaces,proxy);
    }
}