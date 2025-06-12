package com.dtstep.lighthouse.core.builtin;
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
import com.dtstep.lighthouse.client.LightHouse;
import com.dtstep.lighthouse.core.config.LDPConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class CallerStat {

    private static final Logger logger = LoggerFactory.getLogger(CallerStat.class);

    public static void stat(int callerId, String function, int status, Long inBytes, Long outBytes) {
        try{
            if(!LightHouse.isInit()){
                LightHouse.init(LDPConfig.getVal(LDPConfig.KEY_LIGHTHOUSE_ICE_LOCATORS));
            }
        }catch (Exception ex){
            logger.error("CallerStat initialization error!",ex);
        }
        HashMap<String,Object> paramMap = new HashMap<>();
        paramMap.put("callerId",callerId);
        paramMap.put("function",function);
        paramMap.put("status",status);
        paramMap.put("in_bytes",inBytes);
        paramMap.put("out_bytes",outBytes);
        try{
            LightHouse.stat("_builtin_caller_stat","2l2ipBHOssTzsHsdErKDcarxjU6rKZwo",paramMap,System.currentTimeMillis());
        }catch (Exception ex){
            logger.error("caller stat error!",ex);
        }
    }
}
