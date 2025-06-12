package com.dtstep.lighthouse.common.rpc.netty;
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
import com.dtstep.lighthouse.common.entity.rpc.RpcMsgType;
import com.dtstep.lighthouse.common.entity.rpc.RpcRequest;
import com.dtstep.lighthouse.common.entity.rpc.RpcResponse;
import com.dtstep.lighthouse.common.ice.LightRpcException;
import com.dtstep.lighthouse.common.random.RandomID;
import com.dtstep.lighthouse.common.util.StringUtil;
import io.netty.channel.Channel;
import io.netty.channel.pool.*;
import io.netty.util.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

public class RemoteProxy implements InvocationHandler {

    private static final Logger logger = LoggerFactory.getLogger(RemoteProxy.class);

    private final Class<?> clazz;

    private final List<InetSocketAddress> addressList;

    public RemoteProxy(List<InetSocketAddress> addressList,Class<?> clazz) {
       this.addressList = addressList;
       this.clazz = clazz;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return invoke(method,args);
    }

    public Object invoke(Method method, Object[] args) throws Exception {
        RpcRequest rpcRequest = new RpcRequest();
        String reqId = RandomID.id(32);
        rpcRequest.setRequestId(reqId);
        rpcRequest.setClassName(this.clazz.getName());
        rpcRequest.setMethodName(method.getName());
        CompletableFuture<RpcResponse<?>> completableFuture = new CompletableFuture<>();
        ProcessedFuture.put(reqId,completableFuture);
        rpcRequest.setParameterTypes(method.getParameterTypes());
        rpcRequest.setParameterValues(args);
        rpcRequest.setType(RpcMsgType.Normal);
        ChannelPoolMap<InetSocketAddress, ChannelPool> poolMap = NettyClientAdapter.getPoolMap();
        ChannelPool pool = poolMap.get(addressList.get(ThreadLocalRandom.current().nextInt(addressList.size())));
        Future<Channel> future = pool.acquire().sync();
        Channel channel = future.getNow();
        if(logger.isDebugEnabled()){
            logger.debug("Remote request,use channel:{}",channel.id());
        }
        try {
            channel.writeAndFlush(rpcRequest).sync();
        } finally {
            pool.release(channel);
        }
        RpcResponse<?> res = completableFuture.get();
        if(StringUtil.isNotEmpty(res.getError())){
            throw new LightRpcException(res.getError());
        }
        return res.getResult();
    }
}
