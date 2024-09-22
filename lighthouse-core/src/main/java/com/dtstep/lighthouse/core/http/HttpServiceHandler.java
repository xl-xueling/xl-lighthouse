package com.dtstep.lighthouse.core.http;

import com.dtstep.lighthouse.common.entity.ApiResultCode;
import com.dtstep.lighthouse.common.entity.ApiResultData;
import com.dtstep.lighthouse.common.entity.monitor.ClusterInfo;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.core.config.LDPConfig;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;


public class HttpServiceHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private static final Logger logger = LoggerFactory.getLogger(HttpServiceHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        if (request.method() == HttpMethod.GET) {
            handleGetRequest(ctx, request);
        }
        else if (request.method() == HttpMethod.POST) {
            handlePostRequest(ctx, request);
        }
    }

    private void handleGetRequest(ChannelHandlerContext ctx, FullHttpRequest request) {
        String uri = request.uri();
        String result;
        if(uri.startsWith("/clusterInfo")){
            ClusterInfo clusterInfo = new ClusterInfo();
            clusterInfo.setRunningMode(LDPConfig.getRunningMode());
            long time = ManagementFactory.getRuntimeMXBean().getStartTime();
            clusterInfo.setStartTime(time);
            clusterInfo.setRunningTime(System.currentTimeMillis() - time);
            result = JsonUtil.toJSONString(clusterInfo);
            FullHttpResponse response = new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(result.getBytes()));
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        }else{
            result = "The current http service does not allow the GET request type!";
            logger.warn(result);
            FullHttpResponse response = new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1, HttpResponseStatus.METHOD_NOT_ALLOWED, Unpooled.wrappedBuffer(result.getBytes()));
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        }
    }


    private void handlePostRequest(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        String requestBody = request.content().toString(io.netty.util.CharsetUtil.UTF_8);
        String uri = request.uri();
        String result;
        if(uri.startsWith("/clusterInfo")){
            ClusterInfo clusterInfo = new ClusterInfo();
            clusterInfo.setRunningMode(LDPConfig.getRunningMode());
            long time = ManagementFactory.getRuntimeMXBean().getStartTime();
            clusterInfo.setStartTime(time);
            clusterInfo.setRunningTime(System.currentTimeMillis() - time);
            result = JsonUtil.toJSONString(clusterInfo);
        }else{
            ApiResultData apiResultData;
            try{
                apiResultData = request(uri,requestBody);
            }catch (Exception ex){
                logger.error("http request process error!",ex);
                ApiResultCode apiResultCode = ApiResultCode.SystemError;
                apiResultData = new ApiResultData(apiResultCode.getCode(),ex.getMessage());
            }
            result = JsonUtil.toJSONString(apiResultData);
        }
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(result.getBytes()));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private ApiResultData request(String uri, String requestBody) throws Exception {
        if(uri.startsWith("/api/rpc/v1/")){
            String interfaceName = getInterfaceName(uri);
            if(interfaceName.equals("stat")){
                return HttpProcessor.stat(requestBody);
            }
        }
        return new ApiResultData(ApiResultCode.ApiNotSupported.getCode(), ApiResultCode.ApiNotSupported.getMessage());
    }

    private String getInterfaceName(String uri) {
        String[] segments = uri.split("/");
        return segments.length > 4 ? segments[4] : "";
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
