package com.dtstep.lighthouse.common.entity.event;
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
public class DimensBucket extends SlotEvent<DimensBucket> {

    private static final long serialVersionUID = -6361051338612407945L;

    private String token;

    private String dimens;

    private String dimensValue;

    private long ttl;

    public DimensBucket(String token, String dimens, String dimensValue, long ttl){
        this.token = token;
        this.dimens = dimens;
        this.dimensValue = dimensValue;
        this.ttl = ttl;
    }

    public DimensBucket(){}

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDimens() {
        return dimens;
    }

    public void setDimens(String dimens) {
        this.dimens = dimens;
    }

    public String getDimensValue() {
        return dimensValue;
    }

    public void setDimensValue(String dimensValue) {
        this.dimensValue = dimensValue;
    }

    public long getTtl() {
        return ttl;
    }

    public void setTtl(long ttl) {
        this.ttl = ttl;
    }

    @Override
    public String toString() {
        return this.getToken() + "_" + this.getDimens() + "_" + this.getDimensValue();
    }

    @Override
    public int compareTo(DimensBucket o) {
        if(this.getTimestamp() > o.getTimestamp()){
            return 1;
        }else if(this.getTimestamp() == o.getTimestamp()){
            return this.toString().compareTo(o.toString());
        }else {
            return -1;
        }
    }
}
