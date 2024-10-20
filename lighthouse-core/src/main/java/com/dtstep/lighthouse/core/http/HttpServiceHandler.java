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
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class HttpServiceHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private static final Logger logger = LoggerFactory.getLogger(HttpServiceHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
       try{
           if (request.method() == HttpMethod.GET) {
               handleGetRequest(ctx, request);
           }
           else if (request.method() == HttpMethod.POST) {
               handlePostRequest(ctx, request);
           }
       }finally{
           ReferenceCountUtil.release(request);
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
            byte[] responseData = JsonUtil.toJSONString(clusterInfo).getBytes(StandardCharsets.UTF_8);
            FullHttpResponse response = new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(responseData));
            HttpHeaders responseHeaders = response.headers();
            responseHeaders.set(HttpHeaderNames.CONTENT_TYPE, "application/json; charset=utf-8");
            responseHeaders.set(HttpHeaderNames.CONTENT_LENGTH, responseData.length);
            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        }else{
            result = "The current http service does not allow GET request type!";
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
        byte[] responseData;
        if(uri.startsWith("/clusterInfo")){
            ClusterInfo clusterInfo = new ClusterInfo();
            clusterInfo.setRunningMode(LDPConfig.getRunningMode());
            long time = ManagementFactory.getRuntimeMXBean().getStartTime();
            clusterInfo.setStartTime(time);
            clusterInfo.setRunningTime(System.currentTimeMillis() - time);
            responseData = JsonUtil.toJSONString(clusterInfo).getBytes(StandardCharsets.UTF_8);
        }else{
            HttpHeaders headers = request.headers();
            String callerName = headers.get("Caller-Name");
            String callerKey = headers.get("Caller-Key");
            ApiResultData apiResultData;
            try {
                apiResultData = request(uri, callerName, callerKey, requestBody);
            } catch (Exception ex) {
                logger.error("http request process error!", ex);
                ApiResultCode apiResultCode = ApiResultCode.SystemError;
                apiResultData = new ApiResultData(apiResultCode.getCode(), ex.getMessage());
            }
            responseData = JsonUtil.toJSONString(apiResultData).getBytes(StandardCharsets.UTF_8);
        }
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(responseData));
        HttpHeaders responseHeaders = response.headers();
        responseHeaders.set(HttpHeaderNames.CONTENT_TYPE, "application/json; charset=utf-8");
        responseHeaders.set(HttpHeaderNames.CONTENT_LENGTH, responseData.length);
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private ApiResultData request(String uri,String callerName,String callerKey, String requestBody) throws Exception {
        if(uri.startsWith("/api/rpc/v1/")){
            String interfaceName = getInterfaceName(uri);
            switch (interfaceName) {
                case "stat":
                    return HttpProcessor.stat(requestBody);
                case "stats":
                    return HttpProcessor.stats(requestBody);
                case "dataQuery":
                    return HttpProcessor.dataQuery(callerName,callerKey,requestBody);
                case "dataQueryWithDimensList":
                    return HttpProcessor.dataQueryWithDimensList(callerName,callerKey,requestBody);
                case "limitQuery":
                    return HttpProcessor.limitQuery(callerName,callerKey,requestBody);
            }
        }
        return new ApiResultData(ApiResultCode.ApiNotSupported.getCode(), ApiResultCode.ApiNotSupported.getMessage());
    }

    private static final Pattern API_PATTERN = Pattern.compile("/api/rpc/v1/([^/]+)");

    private String getInterfaceName(String uri) {
        String cleanedUrl = uri.replaceAll("/+", "/");
        Matcher matcher = API_PATTERN.matcher(cleanedUrl);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
