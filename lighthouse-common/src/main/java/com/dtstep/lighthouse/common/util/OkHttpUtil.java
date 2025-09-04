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
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

public class OkHttpUtil {

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .retryOnConnectionFailure(false)
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
        HttpUrl.Builder urlBuilder = HttpUrl.parse(requestConfig.getUrl()).newBuilder();
        if (requestConfig.getParams() != null) {
            for (KeyValue param : requestConfig.getParams()) {
                urlBuilder.addQueryParameter(param.getKey(), param.getValue());
            }
        }
        HttpUrl url = urlBuilder.build();
        RequestBody body = null;
        RequestBodyDTO bodyDTO = requestConfig.getBody();
        if (bodyDTO != null) {
            String type = bodyDTO.getType();
            if ("form-data".equalsIgnoreCase(type) && bodyDTO.getContent() != null) {
                FormBody.Builder formBuilder = new FormBody.Builder();
                for (KeyValue kv : bodyDTO.getContent()) {
                    formBuilder.add(kv.getKey(), kv.getValue());
                }
                body = formBuilder.build();
            } else if ("json".equalsIgnoreCase(type) && bodyDTO.getJson() != null) {
                MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
                body = RequestBody.create(mediaType, bodyDTO.getJson().toString());
            } else if ("xml".equalsIgnoreCase(type) && bodyDTO.getXml() != null) {
                MediaType mediaType = MediaType.parse("application/xml; charset=utf-8");
                body = RequestBody.create(mediaType, bodyDTO.getXml());
            } else if ("raw".equalsIgnoreCase(type) && bodyDTO.getRaw() != null) {
                MediaType mediaType = MediaType.parse("text/plain; charset=utf-8");
                body = RequestBody.create(mediaType, bodyDTO.getRaw());
            }
        }
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
                requestBuilder.post(body != null ? body : RequestBody.create(null, new byte[0]));
                break;
            case "PUT":
                requestBuilder.put(body != null ? body : RequestBody.create(null, new byte[0]));
                break;
            case "DELETE":
                if (body != null) {
                    requestBuilder.delete(body);
                } else {
                    requestBuilder.delete();
                }
                break;
            default:
                requestBuilder.method(method, body);
                break;
        }
        try (Response response = client.newCall(requestBuilder.build()).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            return response.body() != null ? response.body().string() : null;
        }
    }
}
