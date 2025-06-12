package com.dtstep.lighthouse.ice.servant.rpc.ice;
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
import com.dtstep.lighthouse.common.constant.SysConst;
import com.dtstep.lighthouse.common.exception.InitializationException;
import com.dtstep.lighthouse.common.ice.RemoteLightServer;
import com.dtstep.lighthouse.core.config.LDPConfig;
import com.dtstep.lighthouse.core.http.LightHouseHttpService;
import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.Util;
import com.zeroc.IceBox.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ICELightHouseService implements Service {

    private static final Logger logger = LoggerFactory.getLogger(ICELightHouseService.class);

    @Override
    public void start(String s, Communicator communicator, String[] strings) {
        try{
            LDPConfig.loadConfiguration();
        }catch (Exception ex){
            logger.error("ice service initialization error!",ex);
            throw new InitializationException();
        }
        new Thread(LightHouseHttpService::start).start();
        ObjectAdapter adapter = communicator.createObjectAdapter(s);
        communicator.getProperties().setProperty("Ice.MessageSizeMax", "1409600");
        RemoteLightServer servant = new ICERemoteLightServerImpl();
        adapter.add(servant, Util.stringToIdentity("LightHouseServiceIdentity"));
        adapter.activate();
        System.out.println("ice server start success!");
        logger.info("ldp rpc service start,listen:{}", SysConst.CLUSTER_RPC_SERVICE_PORT);
    }

    @Override
    public void stop() {}
}
