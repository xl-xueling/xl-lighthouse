package com.dtstep.lighthouse.standalone.rpc;

import com.dtstep.lighthouse.common.util.JsonUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("client receive message:" + JsonUtil.toJSONString(msg) + ",type:" + msg.getClass());
//        ByteBuf buf = (ByteBuf) msg;
//        try {
//            byte[] bytes = new byte[buf.readableBytes()];
//            buf.readBytes(bytes);
//            String response = new String(bytes);
//            System.out.println("Received response from server: " + response);
//        } finally {
//            buf.release();
//        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}

