package com.dtstep.lighthouse.standalone.rpc;

import com.dtstep.lighthouse.common.entity.rpc.RpcRequest;
import com.dtstep.lighthouse.common.entity.rpc.RpcResponse;
import com.dtstep.lighthouse.standalone.rpc.provider.StandaloneRemoteServiceImpl;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

@ChannelHandler.Sharable
public class NettyServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private static final ConcurrentHashMap<String,Object> registryMap = new ConcurrentHashMap<String, Object>();

    public NettyServerHandler(){}

    public static void register() {
        registryMap.put("com.dtstep.lighthouse.common.rpc.netty.provider.StandaloneRemoteService",new StandaloneRemoteServiceImpl());
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, RpcRequest request) throws Exception {
        Object result = new Object();
        if(registryMap.containsKey(request.getClassName())){
            Object provider = registryMap.get(request.getClassName());
            Method method = provider.getClass().getMethod(request.getMethodName(),request.getParameterTypes());
            result = method.invoke(provider,request.getParameterValues());
        }
        RpcResponse response = new RpcResponse();
        response.setRequestId(request.getRequestId());
        response.setResult(result);
        ctx.writeAndFlush(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
