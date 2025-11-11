package com.dtstep.lighthouse.core.http;
/*
 * Copyright (C) 2022-2025 XueLing.雪灵
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
import com.dtstep.lighthouse.common.entity.ApiResultCode;
import com.dtstep.lighthouse.common.entity.ApiResultData;
import com.dtstep.lighthouse.common.entity.monitor.ClusterInfo;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.core.config.LDPConfig;
import com.dtstep.lighthouse.core.reactor.AsyncReactorExecutor;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpServiceHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private static final Logger logger = LoggerFactory.getLogger(HttpServiceHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        try {
            if (request.method() == HttpMethod.GET) {
                handleGetRequest(ctx, request);
            } else if (request.method() == HttpMethod.POST) {
                handlePostRequest(ctx, request);
            } else {
                sendError(ctx, request, HttpResponseStatus.METHOD_NOT_ALLOWED, "Unsupported HTTP method.");
            }
        } catch (Exception ex) {
            logger.error("Unhandled exception in channelRead0", ex);
            sendError(ctx, request, HttpResponseStatus.INTERNAL_SERVER_ERROR, "Internal server error.");
        }
    }

    private static final long JVM_START_TIME = ManagementFactory.getRuntimeMXBean().getStartTime();

    private void handleGetRequest(ChannelHandlerContext ctx, FullHttpRequest request) {
        try {
            String uri = request.uri();
            if (uri.startsWith("/clusterInfo")) {
                ClusterInfo clusterInfo = new ClusterInfo();
                clusterInfo.setRunningMode(LDPConfig.getRunningMode());
                clusterInfo.setStartTime(JVM_START_TIME);
                clusterInfo.setRunningTime(System.currentTimeMillis() - JVM_START_TIME);
                sendObjectResponse(ctx, request, HttpResponseStatus.OK, clusterInfo);
            } else {
                logger.warn("The current http service does not allow GET request type! URI: {}", uri);
                sendError(ctx, request, HttpResponseStatus.METHOD_NOT_ALLOWED,
                        "The current http service does not allow GET request type!");
            }
        } catch (Exception ex){
            logger.error("process get error!",ex);
        }
    }

    private void handlePostRequest(ChannelHandlerContext ctx, FullHttpRequest request) {
        request.retain();
        ByteBuf byteBuf = request.content();
        String requestBody = byteBuf.isReadable() ? byteBuf.toString(StandardCharsets.UTF_8) : "";
        String uri = request.uri();
        HttpHeaders headers = request.headers();
        String callerName = Optional.ofNullable(headers.get("Caller-Name")).orElse("");
        String callerKey = Optional.ofNullable(headers.get("Caller-Key")).orElse("");
        long startTime = System.currentTimeMillis();
        boolean isSync = uri.startsWith("/clusterInfo")
                || uri.startsWith("/api/rpc/v1/stat")
                || uri.startsWith("/api/rpc/v1/stats");
        if (isSync) {
            try {
                Object result;
                if (uri.startsWith("/clusterInfo")) {
                    ClusterInfo clusterInfo = new ClusterInfo();
                    clusterInfo.setRunningMode(LDPConfig.getRunningMode());
                    clusterInfo.setStartTime(System.currentTimeMillis());
                    clusterInfo.setRunningTime(System.currentTimeMillis() - JVM_START_TIME);
                    result = clusterInfo;
                } else {
                    result = request(uri, callerName, callerKey, requestBody);
                }
                long duration = System.currentTimeMillis() - startTime;
                logger.debug("Sync request completed in {}ms: {}", duration, uri);
                sendObjectResponse(ctx, request, HttpResponseStatus.OK, result);
            } catch (Exception e) {
                logger.error("Sync request failed for {}: {}", uri, e.getMessage(), e);
                ApiResultData errorResult = buildErrorResult(e);
                sendObjectResponse(ctx, request, HttpResponseStatus.INTERNAL_SERVER_ERROR, errorResult);
            } finally {
                safeRelease(request);
            }
        } else {
            AsyncReactorExecutor.executeAsync(() -> {
                try {
                    return request(uri, callerName, callerKey, requestBody);
                } catch (Exception e) {
                    logger.error("Error executing async task for URI: {}", uri, e);
                    throw new RuntimeException("Async task execution failed: " + e.getMessage(), e);
                }
            }, 60000L)
                    .subscribe(
                            result -> ctx.executor().execute(() -> {
                                try {
                                    long duration = System.currentTimeMillis() - startTime;
                                    logger.debug("Async request completed in {}ms: {}", duration, uri);
                                    sendObjectResponse(ctx, request, HttpResponseStatus.OK, result);
                                } catch (Exception ex) {
                                    logger.error("Failed to send async response for {}: {}", uri, ex.getMessage(), ex);
                                    sendError(ctx, request, HttpResponseStatus.INTERNAL_SERVER_ERROR, "Failed to send response");
                                } finally {
                                    safeRelease(request);
                                }
                            }),
                            error -> ctx.executor().execute(() -> {
                                try {
                                    long duration = System.currentTimeMillis() - startTime;
                                    logger.error("Async HTTP POST processing failed in {}ms: {}", duration, uri, error);
                                    ApiResultData errorResult = buildErrorResult(error);
                                    sendObjectResponse(ctx, request, HttpResponseStatus.INTERNAL_SERVER_ERROR, errorResult);
                                } catch (Exception ex) {
                                    logger.error("Failed to send async error response for {}: {}", uri, ex.getMessage(), ex);
                                    sendError(ctx, request, HttpResponseStatus.INTERNAL_SERVER_ERROR, "Failed to send error response");
                                } finally {
                                    safeRelease(request);
                                }
                            })
                    );
        }
    }

    private ApiResultData request(String uri, String callerName, String callerKey, String requestBody) {
        try {
            if (uri.startsWith("/api/rpc/v1/")) {
                String interfaceName = getInterfaceName(uri);
                switch (interfaceName) {
                    case "stat":
                        return HttpProcessor.stat(requestBody);
                    case "stats":
                        return HttpProcessor.stats(requestBody);
                    case "dataQuery":
                        return HttpProcessor.dataQuery(callerName, callerKey, requestBody);
                    case "dataDurationQuery":
                        return HttpProcessor.dataDurationQuery(callerName, callerKey, requestBody);
                    case "dataQueryWithDimensList":
                        return HttpProcessor.dataQueryWithDimensList(callerName, callerKey, requestBody);
                    case "dataDurationQueryWithDimensList":
                        return HttpProcessor.dataDurationQueryWithDimensList(callerName, callerKey, requestBody);
                    case "limitQuery":
                        return HttpProcessor.limitQuery(callerName, callerKey, requestBody);
                    case "viewQuery":
                        return HttpProcessor.viewQuery(callerName, callerKey, requestBody);
                    default:
                        logger.warn("Unsupported API interface: {}", interfaceName);
                        return new ApiResultData(ApiResultCode.ApiNotSupported.getCode(),
                                ApiResultCode.ApiNotSupported.getMessage());
                }
            }
            return new ApiResultData(ApiResultCode.ApiNotSupported.getCode(),
                    ApiResultCode.ApiNotSupported.getMessage());
        } catch (Exception e) {
            logger.error("Error processing request for URI: {}", uri, e);
            throw new RuntimeException("Request processing failed: " + e.getMessage(), e);
        }
    }

    private ApiResultData buildErrorResult(Throwable error) {
        String errorMessage;
        if (error instanceof TimeoutException) {
            errorMessage = "Request timeout";
        } else if (error.getCause() != null) {
            errorMessage = error.getCause().getMessage();
        } else {
            errorMessage = error.getMessage();
        }
        if (errorMessage == null || errorMessage.trim().isEmpty()) {
            errorMessage = "Internal server error";
        }
        return new ApiResultData(ApiResultCode.SystemError.getCode(), errorMessage);
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
        logger.error("Unhandled exception caught in pipeline, closing channel", cause);
        ctx.close();
    }

    private void sendObjectResponse(ChannelHandlerContext ctx, FullHttpRequest request,
                                    HttpResponseStatus status, Object responseObj) {
        try {
            String jsonResponse = JsonUtil.toJSONString(responseObj);
            byte[] responseBytes = jsonResponse.getBytes(StandardCharsets.UTF_8);
            ByteBuf buf = ctx.alloc().ioBuffer(responseBytes.length);
            buf.writeBytes(responseBytes);

            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, buf);
            HttpHeaders headers = response.headers();
            headers.set(HttpHeaderNames.CONTENT_TYPE, "application/json; charset=utf-8");
            headers.set(HttpHeaderNames.CONTENT_LENGTH, responseBytes.length);
            boolean keepAlive = HttpUtil.isKeepAlive(request);
            if (keepAlive) {
                headers.set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            } else {
                headers.set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
            }
            ChannelFuture future = ctx.writeAndFlush(response);
            if (!keepAlive) {
                future.addListener(ChannelFutureListener.CLOSE);
            }
        } catch (Exception e) {
            logger.error("Failed to send object response", e);
            sendError(ctx, request, HttpResponseStatus.INTERNAL_SERVER_ERROR, "Error generating response");
        }
    }

    private void sendError(ChannelHandlerContext ctx, FullHttpRequest request,
                           HttpResponseStatus status, String message) {
        try {
            ApiResultData errorResult = new ApiResultData(ApiResultCode.ProcessError.getCode(), message);
            String jsonResponse = JsonUtil.toJSONString(errorResult);
            byte[] content = jsonResponse.getBytes(StandardCharsets.UTF_8);
            ByteBuf buf = ctx.alloc().ioBuffer(content.length);
            buf.writeBytes(content);
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, buf);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json; charset=utf-8");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.length);
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        } catch (Exception e) {
            logger.error("Failed to send error response", e);
            ctx.close();
        }
    }

    private void safeRelease(FullHttpRequest request) {
        if (request != null && request.refCnt() > 0) {
            request.release();
        }
    }
}