package com.dtstep.lighthouse.standalone.rpc;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

@ChannelHandler.Sharable
public class CustomIdleStateHandler extends IdleStateHandler {

    public CustomIdleStateHandler() {
        super(15, 0, 0, TimeUnit.SECONDS);
    }

    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) {
        ctx.channel().close();
    }
}
