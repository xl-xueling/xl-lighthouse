package com.dtstep.lighthouse.ice.servant.rpc.http;

import com.dtstep.lighthouse.common.util.JsonUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class ServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        String requestBody = request.content().toString(io.netty.util.CharsetUtil.UTF_8);
        System.out.println("requestBody is:" + requestBody);
        if (request.method() == HttpMethod.GET) {
            handleGetRequest(ctx, request);
        }
        else if (request.method() == HttpMethod.POST) {
            handlePostRequest(ctx, request);
        }
    }

    private void handleGetRequest(ChannelHandlerContext ctx, FullHttpRequest request) {
        String content = "Response!";
        FullHttpResponse response = new DefaultFullHttpResponse(
                HTTP_1_1, OK, Unpooled.wrappedBuffer(content.getBytes()));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private void handlePostRequest(ChannelHandlerContext ctx, FullHttpRequest request) {
        String requestBody = request.content().toString(io.netty.util.CharsetUtil.UTF_8);
        String content = "Response data: " + requestBody;
        FullHttpResponse response = new DefaultFullHttpResponse(
                HTTP_1_1, OK, Unpooled.wrappedBuffer(content.getBytes()));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
