package com.dtstep.lighthouse.standalone.rpc;

import com.dtstep.lighthouse.common.entity.rpc.RpcMsgType;
import com.dtstep.lighthouse.common.entity.rpc.RpcRequest;
import com.dtstep.lighthouse.common.entity.rpc.RpcResponse;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.standalone.rpc.provider.StandaloneRemoteServiceImpl;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        Object result = new Object();
        String error = null;
        if(request.getType() == RpcMsgType.Normal){
            if(registryMap.containsKey(request.getClassName())){
                try{
                    Object provider = registryMap.get(request.getClassName());
                    Method method = provider.getClass().getMethod(request.getMethodName(),request.getParameterTypes());
                    result = method.invoke(provider,request.getParameterValues());
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
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
