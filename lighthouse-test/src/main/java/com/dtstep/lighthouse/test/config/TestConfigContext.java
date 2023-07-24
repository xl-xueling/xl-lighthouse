package com.dtstep.lighthouse.test.config;
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
import com.dtstep.lighthouse.test.entity.BehaviorSampleEntity;
import com.dtstep.lighthouse.test.mode.ModalSample;

public class TestConfigContext {

    private String token;

    private String secretKey;

    private int messageSize;

    private ModalSample<BehaviorSampleEntity> modalSample;

    public int getMessageSize() {
        return messageSize;
    }

    public void setMessageSize(int messageSize) {
        this.messageSize = messageSize;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public ModalSample<BehaviorSampleEntity> getModalSample() {
        return modalSample;
    }

    public void setModalSample(ModalSample<BehaviorSampleEntity> modalSample) {
        this.modalSample = modalSample;
    }
}
