package com.dtstep.lighthouse.insights.config;
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
import org.apache.curator.shaded.com.google.common.collect.Lists;
import org.springframework.security.authentication.AbstractAuthenticationToken;

public class SeedAuthenticationToken extends AbstractAuthenticationToken {

    private Integer userId;

    private String seed;

    public SeedAuthenticationToken(Integer userId,String seed){
        super(Lists.newArrayList());
        this.userId = userId;
        this.seed = seed;
    }

    public void setSeed(String seed) {
        this.seed = seed;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getSeed() {
        return seed;
    }

    @Override
    public Integer getPrincipal() {
        return userId;
    }

    @Override
    public Object getCredentials() {
        return seed;
    }
}
