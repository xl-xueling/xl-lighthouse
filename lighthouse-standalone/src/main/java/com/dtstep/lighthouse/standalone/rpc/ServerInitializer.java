package com.dtstep.lighthouse.standalone.rpc;
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
import com.dtstep.lighthouse.common.rpc.netty.CustomIdleServerHandler;
import com.dtstep.lighthouse.common.rpc.netty.RpcDecoder;
import com.dtstep.lighthouse.common.rpc.netty.RpcEncoder;
import com.dtstep.lighthouse.common.serializer.KryoSerializer;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class ServerInitializer extends ChannelInitializer<SocketChannel> {

    private static final NettyServerHandler nettyServerHandler = new NettyServerHandler();

    @Override
    protected void initChannel(SocketChannel ch) {
        int fieldLength = 4;
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,fieldLength,0,fieldLength));
        pipeline.addLast(new LengthFieldPrepender(fieldLength));
        pipeline.addLast("encoder",new RpcEncoder(RpcResponse.class,new KryoSerializer()));
        pipeline.addLast("decoder",new RpcDecoder(RpcRequest.class,new KryoSerializer()));
        pipeline.addLast(new IdleStateHandler(0, 0, 30, TimeUnit.SECONDS));
        pipeline.addLast(nettyServerHandler);
    }
}
