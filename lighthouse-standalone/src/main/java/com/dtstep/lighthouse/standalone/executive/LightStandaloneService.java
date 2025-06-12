package com.dtstep.lighthouse.standalone.executive;
/*
 * Copyright (C) 2022-2025 XueLing.雪灵
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
import com.dtstep.lighthouse.common.constant.SysConst;
import com.dtstep.lighthouse.standalone.rpc.NettyServerHandler;
import com.dtstep.lighthouse.standalone.rpc.ServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.kqueue.KQueue;
import io.netty.channel.kqueue.KQueueEventLoopGroup;
import io.netty.channel.kqueue.KQueueServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LightStandaloneService {

    private static final Logger logger = LoggerFactory.getLogger(LightStandaloneService.class);

    private static final int port = SysConst.CLUSTER_RPC_SERVICE_PORT;

    public static void start() {
        EventLoopGroup bossGroup;
        EventLoopGroup workerGroup;
        if (Epoll.isAvailable()) {
            bossGroup = new EpollEventLoopGroup();
            workerGroup = new EpollEventLoopGroup();
        } else if (KQueue.isAvailable()) {
            bossGroup = new KQueueEventLoopGroup();
            workerGroup = new KQueueEventLoopGroup();
        } else {
            bossGroup = new NioEventLoopGroup();
            workerGroup = new NioEventLoopGroup();
        }
        NettyServerHandler.register();
        try {
            ServerBootstrap server = new ServerBootstrap();
            server.group(bossGroup, workerGroup)
                    .channel(Epoll.isAvailable() ? EpollServerSocketChannel.class :
                            (KQueue.isAvailable() ? KQueueServerSocketChannel.class : NioServerSocketChannel.class))
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .option(EpollChannelOption.SO_REUSEPORT, true)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ServerInitializer())
                    .childOption(ChannelOption.SO_SNDBUF, 10 * 1024 * 1024)
                    .childOption(ChannelOption.SO_RCVBUF, 10 * 1024 * 1024);
            ChannelFuture future = server.bind(port).sync();
            logger.info("ldp standalone rpc service start,listen:{}",port);
            future.channel().closeFuture().sync();
        }catch (Exception ex){
            logger.error("ldp standalone rpc service startup exception!",ex);
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
