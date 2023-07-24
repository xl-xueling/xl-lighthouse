package com.dtstep.lighthouse.ice.servant;
/*
 * Copyright (C) 2022-2023 XueLing.雪灵
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
import Ice.Communicator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class AuxService extends AbstractService {

    private static final Logger logger = LoggerFactory.getLogger(AuxService.class);

    private Ice.ObjectAdapter adapter;

    @Override
    public void start(String name, Communicator ic, String[] args) {
        super.init();
        adapter = ic.createObjectAdapter(name);
        ic.getProperties().setProperty("Ice.MessageSizeMax", "1409600");
        Ice.Object object = new AuxI();
        adapter.add(object,ic.stringToIdentity("identity_aux"));
        adapter.activate();
        logger.info("ice server(aux) start success!");
    }

    @Override
    public void stop() {
        if(adapter != null){
            logger.info("call stop servant:{aux service}!");
            adapter.destroy();
        }
    }
}
