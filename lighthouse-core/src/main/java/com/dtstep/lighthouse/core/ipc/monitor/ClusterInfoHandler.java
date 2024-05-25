package com.dtstep.lighthouse.core.ipc.monitor;
/*
 * Copyright (C) 2022-2024 XueLing.雪灵
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
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.core.config.LDPConfig;
import com.dtstep.lighthouse.common.entity.monitor.ClusterInfo;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.management.ManagementFactory;
import java.nio.charset.StandardCharsets;

public class ClusterInfoHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        ClusterInfo clusterInfo = new ClusterInfo();
        clusterInfo.setRunningMode(LDPConfig.getRunningMode());
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        clusterInfo.setStartTime(time);
        clusterInfo.setRunningTime(System.currentTimeMillis() - time);
        exchange.sendResponseHeaders(200, 0);
        OutputStream output = exchange.getResponseBody();
        String text = JsonUtil.toJSONString(clusterInfo);
        output.write(text.getBytes(StandardCharsets.UTF_8));
        output.flush();
        output.close();
        exchange.close();
    }
}
