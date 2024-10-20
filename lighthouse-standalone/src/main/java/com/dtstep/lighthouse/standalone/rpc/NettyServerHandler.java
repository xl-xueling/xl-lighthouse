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
import com.dtstep.lighthouse.common.entity.rpc.RpcMsgType;
import com.dtstep.lighthouse.common.entity.rpc.RpcRequest;
import com.dtstep.lighthouse.common.entity.rpc.RpcResponse;
import com.dtstep.lighthouse.common.ice.LightRpcException;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.standalone.rpc.provider.StandaloneRemoteServiceImpl;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@ChannelHandler.Sharable
public class NettyServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private static final ConcurrentHashMap<String,Object> registryMap = new ConcurrentHashMap<String, Object>();

    private static final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);

    public NettyServerHandler(){}

    public static void register() {
        registryMap.put("com.dtstep.lighthouse.common.rpc.BasicRemoteLightServerPrx",new StandaloneRemoteServiceImpl());
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, RpcRequest request) throws Exception {
        try{
            Object result = new Object();
            String error = null;
            if(request.getType() == RpcMsgType.Normal){
                if(registryMap.containsKey(request.getClassName())){
                    try{
                        Object provider = registryMap.get(request.getClassName());
                        Method method = provider.getClass().getMethod(request.getMethodName(),request.getParameterTypes());
                        result = method.invoke(provider,request.getParameterValues());
                    }catch (InvocationTargetException ex){
                        Throwable cause = ex.getCause();
                        if(cause instanceof LightRpcException){
                            LightRpcException lightRpcException = (LightRpcException) cause;
                            String errorMessage = lightRpcException.getMessage();
                            error = errorMessage == null ? "Remote Server process error!" : errorMessage;
                        }else{
                            logger.error("method process error,request:{}!", JsonUtil.toJSONString(request),ex);
                            error = ex.getMessage() == null ? "Remote Server process error!" : cause.getMessage();
                        }
                    }catch (Exception ex){
                        logger.error("method process error,request:{}!", JsonUtil.toJSONString(request),ex);
                        error = ex.getMessage() == null ? "Remote Server process error!" : ex.getMessage();
                    }
                }
                RpcResponse response = new RpcResponse();
                response.setRequestId(request.getRequestId());
                response.setType(RpcMsgType.Normal);
                response.setResult(result);
                response.setError(error);
                ctx.writeAndFlush(response);
            }else if(request.getType() == RpcMsgType.HeartBeat){
                RpcResponse response = new RpcResponse();
                response.setRequestId(request.getRequestId());
                response.setType(RpcMsgType.HeartBeat);
                response.setError(error);
                ctx.writeAndFlush(response);
            }
        }finally {
            ReferenceCountUtil.release(request);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
