package com.dtstep.lighthouse.tasks.monitor;
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
import com.dtstep.lighthouse.client.LightHouse;
import com.dtstep.lighthouse.common.entity.group.GroupExtEntity;
import com.dtstep.lighthouse.core.builtin.BuiltinLoader;
import com.dtstep.lighthouse.core.config.LDPConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;

public final class BuiltinMonitor {

    private static final Logger logger = LoggerFactory.getLogger(BuiltinMonitor.class);

    private static final BuiltinMonitor instance = new BuiltinMonitor();

    public static BuiltinMonitor getInstance(){
        return instance;
    }

    static {
        LightHouse.init(LDPConfig.getVal(LDPConfig.KEY_LIGHTHOUSE_ICE_LOCATORS));
    }

    public void monitor(String monitorToken, Map<String,Object> paramMap,int repeat) throws Exception {
        GroupExtEntity groupExtEntity = BuiltinLoader.getBuiltinGroup(monitorToken);
        if(groupExtEntity == null){
            return;
        }
        LightHouse.stat(groupExtEntity.getToken(),groupExtEntity.getSecretKey(),paramMap,repeat,System.currentTimeMillis());
    }
}
