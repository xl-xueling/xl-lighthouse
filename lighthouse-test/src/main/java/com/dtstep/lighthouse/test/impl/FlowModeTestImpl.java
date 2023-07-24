package com.dtstep.lighthouse.test.impl;
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
import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.test.entity.BehaviorSampleEntity;
import com.dtstep.lighthouse.test.entity.SampleEntity;
import com.dtstep.lighthouse.test.mode.ModalSample;
import com.dtstep.lighthouse.test.config.TestConfigContext;
import com.dtstep.lighthouse.test.util.BeanUtil;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class FlowModeTestImpl implements TestModel {

    private static final ScheduledExecutorService service = Executors.newScheduledThreadPool(5);

    @Override
    public void send(TestConfigContext testConfigContext) throws Exception {
        Runnable runnable = () -> {
            try{
                long t1 = System.currentTimeMillis();
                long timestamp = DateUtil.batchTime(1,TimeUnit.MINUTES,System.currentTimeMillis());
                long messageTime = timestamp + ThreadLocalRandom.current().nextInt(60 * 1000);
                String token = testConfigContext.getToken();
                String secretKey = testConfigContext.getSecretKey();
                ModalSample<BehaviorSampleEntity> modalSample = testConfigContext.getModalSample();
                int size = testConfigContext.getMessageSize();
                for(int i=0;i < size * 30;i++){
                    SampleEntity sampleEntity = modalSample.generateSample();
                    LightHouse.stat(token,secretKey, BeanUtil.beanToMap(sampleEntity),messageTime);
                }
                long t2 = System.currentTimeMillis();
                System.out.println("send success,batchTime:" + DateUtil.formatTimeStamp(timestamp,"yyyy-MM-dd HH:mm:ss")
                        + ",execute time:" + DateUtil.formatTimeStamp(t1,"yyyy-MM-dd HH:mm:ss") + ",cost:" + (t2 - t1));
            }catch (Exception ex){
                ex.printStackTrace();
            }
        };
        service.scheduleAtFixedRate(runnable,0,30, TimeUnit.SECONDS);
    }
}
