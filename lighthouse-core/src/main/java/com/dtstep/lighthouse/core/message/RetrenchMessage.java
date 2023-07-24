package com.dtstep.lighthouse.core.message;
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
import com.dtstep.lighthouse.common.entity.message.LightMessage;
import net.sf.cglib.beans.BeanCopier;
import java.io.Serializable;
import java.util.Map;


public final class RetrenchMessage implements Serializable {

    private static final long serialVersionUID = 767799306156040976L;

    private String id;

    private String token;

    private Map<String,Object> paramMap;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, Object> getParamMap() {
        return paramMap;
    }

    public void setParamMap(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }

    public static RetrenchMessage translateTo(LightMessage lightMessage) throws Exception {
        RetrenchMessage obj = new RetrenchMessage();
        BeanCopier copier = BeanCopier.create(LightMessage.class, RetrenchMessage.class, false);
        copier.copy(lightMessage, obj, null);
        return obj;
    }
}
