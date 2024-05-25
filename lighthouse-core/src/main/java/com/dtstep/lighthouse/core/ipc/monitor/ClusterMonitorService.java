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
import com.dtstep.lighthouse.common.constant.SysConst;
import com.dtstep.lighthouse.common.util.IpUtils;
import com.sun.net.httpserver.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClusterMonitorService {

    private static final Logger logger = LoggerFactory.getLogger(ClusterMonitorService.class);

    public static void start() throws Exception {
        boolean isPortUsed = IpUtils.isPortUsing(SysConst.CLUSTER_MONITOR_SERVICE_PORT);
        if(isPortUsed){
            logger.info("ldp monitor service not start,port:{} already used!",SysConst.CLUSTER_MONITOR_SERVICE_PORT);
        }else{
            HttpServer server = HttpServer.create(new InetSocketAddress(SysConst.CLUSTER_MONITOR_SERVICE_PORT), 0);
            ExecutorService exec = Executors.newFixedThreadPool(2);
            server.setExecutor(exec);
            server.createContext("/clusterInfo", new ClusterInfoHandler());
            server.start();
            logger.info("ldp monitor service start,listen:{}",SysConst.CLUSTER_MONITOR_SERVICE_PORT);
        }
    }
}
