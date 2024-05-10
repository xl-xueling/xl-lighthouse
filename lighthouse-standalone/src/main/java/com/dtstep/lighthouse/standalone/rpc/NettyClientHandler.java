package com.dtstep.lighthouse.standalone.rpc;

import com.dtstep.lighthouse.common.util.JsonUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.atomic.AtomicInteger;

public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    static AtomicInteger count = new AtomicInteger(1);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("ctx channel:" + ctx.channel().isActive() + ",id:" + ctx.channel().id());
        System.out.println("client receive,index:"+ count.getAndIncrement() + ",message:" + JsonUtil.toJSONString(msg));
//        if(msg instanceof RpcResponse){
//            RpcResponse response = (RpcResponse) msg;
//            this.response = response.getResult();
//        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}
