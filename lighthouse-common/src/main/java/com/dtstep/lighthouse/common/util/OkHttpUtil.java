package com.dtstep.lighthouse.common.util;
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
import com.dtstep.lighthouse.common.modal.HttpRequestConfig;
import com.dtstep.lighthouse.common.modal.KeyValue;
import com.dtstep.lighthouse.common.modal.RequestBodyDTO;
import io.netty.handler.timeout.ReadTimeoutException;
import okhttp3.*;
import org.apache.commons.collections.MapUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

public class OkHttpUtil {

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .retryOnConnectionFailure(false)
            .build();

    private static final OkHttpClient retryClient = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build();

    public static String post(String url, String requestBody) throws IOException {
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(requestBody,mediaType);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            return response.body().string();
        }
    }

    public static String post(HttpUrl httpUrl, TreeMap<String, String> headers,RequestBody requestBody) throws IOException {
           Request.Builder requestBuilder = new Request.Builder()
                    .url(httpUrl)
                    .method("POST", requestBody);
           if(MapUtils.isNotEmpty(headers)){
               for (Map.Entry<String, String> header : headers.entrySet()) {
                   requestBuilder.addHeader(header.getKey(), header.getValue());
               }
           }
            Request request = requestBuilder.build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            return response.body().string();
        }
    }

    public static String get(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            return response.body().string();
        }
    }

    public static String request(HttpRequestConfig requestConfig) throws IOException {
        Objects.requireNonNull(requestConfig.getUrl(), "URL must not be null");
        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(requestConfig.getUrl())).newBuilder();
        if (requestConfig.getParams() != null) {
            for (KeyValue param : requestConfig.getParams()) {
                urlBuilder.addQueryParameter(param.getKey(), param.getValue());
            }
        }
        HttpUrl url = urlBuilder.build();
        RequestBody body = buildRequestBody(requestConfig.getBody());
        Request.Builder requestBuilder = new Request.Builder().url(url);
        if (requestConfig.getHeaders() != null) {
            for (KeyValue header : requestConfig.getHeaders()) {
                requestBuilder.addHeader(header.getKey(), header.getValue());
            }
        }
        String method = requestConfig.getMethod().toUpperCase();
        switch (method) {
            case "GET":
                requestBuilder.get();
                break;
            case "POST":
                requestBuilder.post(body != null ? body : emptyBody());
                break;
            case "PUT":
                requestBuilder.put(body != null ? body : emptyBody());
                break;
            case "DELETE":
                requestBuilder.delete(body != null ? body : emptyBody());
                break;
            default:
                requestBuilder.method(method, body != null ? body : emptyBody());
                break;
        }
        try (Response response = retryClient.newCall(requestBuilder.build()).execute()) {
            if (!response.isSuccessful()) {
                return "ErrorCode:" + response.code() + ", Message:" + response.message()
                        + ", URL:" + response.request().url() + ", Time:" + DateUtil.formatTimeStamp(System.currentTimeMillis(),"yyyy-MM-dd HH:mm:ss")  + ", ResponseBody:" + response.body().string();
            }
            return response.body() != null ? response.body().string() : null;
        }
    }

    private static RequestBody buildRequestBody(RequestBodyDTO bodyDTO) {
        if (bodyDTO == null || bodyDTO.getType() == null) return null;
        String type = bodyDTO.getType().toLowerCase();
        switch (type) {
            case "form-data":
                if (bodyDTO.getFormData() != null) {
                    MultipartBody.Builder multipartBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
                    for (KeyValue kv : bodyDTO.getFormData()) {
                        multipartBuilder.addFormDataPart(kv.getKey(), kv.getValue());
                    }
                    return multipartBuilder.build();
                }
                break;
            case "x-www-form-urlencoded":
                if (bodyDTO.getUrlencodedData() != null) {
                    StringBuilder encoded = new StringBuilder();
                    for (KeyValue kv : bodyDTO.getUrlencodedData()) {
                        if (encoded.length() > 0) encoded.append("&");
                        encoded.append(URLEncoder.encode(kv.getKey(), StandardCharsets.UTF_8))
                                .append("=")
                                .append(URLEncoder.encode(kv.getValue(), StandardCharsets.UTF_8));
                    }
                    MediaType mediaType = MediaType.get("application/x-www-form-urlencoded; charset=utf-8");
                    return RequestBody.create(encoded.toString(), mediaType);
                }
                break;
            case "json":
                if (bodyDTO.getJson() != null) {
                    MediaType mediaType = MediaType.get("application/json; charset=utf-8");
                    return RequestBody.create(bodyDTO.getJson().toString(), mediaType);
                }
                break;
            case "xml":
                if (bodyDTO.getXml() != null) {
                    MediaType mediaType = MediaType.get("application/xml; charset=utf-8");
                    return RequestBody.create(bodyDTO.getXml(), mediaType);
                }
                break;
            case "raw":
                if (bodyDTO.getRaw() != null) {
                    MediaType mediaType = MediaType.get("text/plain; charset=utf-8");
                    return RequestBody.create(bodyDTO.getRaw(), mediaType);
                }
                break;
        }
        return null;
    }

    private static RequestBody emptyBody() {
        return RequestBody.create(new byte[0], null);
    }
}
