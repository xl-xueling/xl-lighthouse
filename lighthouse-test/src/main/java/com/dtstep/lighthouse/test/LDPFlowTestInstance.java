package com.dtstep.lighthouse.test;
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
import com.dtstep.lighthouse.core.config.LDPConfig;
import com.dtstep.lighthouse.core.wrapper.GroupDBWrapper;
import com.dtstep.lighthouse.test.config.TestConfigContext;
import com.dtstep.lighthouse.test.entity.BehaviorSampleEntity;
import com.dtstep.lighthouse.test.impl.FlowModeTestImpl;
import com.dtstep.lighthouse.test.mode.BehaviorModalSample;
import com.dtstep.lighthouse.test.mode.ModalSample;
import org.apache.commons.lang3.Validate;

public class LDPFlowTestInstance {

    public static void main(String[] args) throws Exception {
        int minuteSize = Integer.parseInt(args[0]);
        LDPConfig.loadConfiguration();
        LightHouse.init(LDPConfig.getVal(LDPConfig.KEY_LIGHTHOUSE_ICE_LOCATORS));
        TestConfigContext testConfigContext = new TestConfigContext();
        ModalSample<BehaviorSampleEntity> modalSample = new BehaviorModalSample();
        String token = modalSample.getToken();
        testConfigContext.setToken(token);
        GroupExtEntity groupExtEntity = GroupDBWrapper.queryByToken(token);
        Validate.notNull(groupExtEntity);
        testConfigContext.setSecretKey(groupExtEntity.getSecretKey());
        testConfigContext.setModalSample(modalSample);
        FlowModeTestImpl impl = new FlowModeTestImpl();
        testConfigContext.setMessageSize(minuteSize);
        impl.send(testConfigContext);
        System.out.println("Send Success!");
    }
}
