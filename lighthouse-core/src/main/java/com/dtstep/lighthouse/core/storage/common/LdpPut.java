package com.dtstep.lighthouse.core.storage.common;
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

public class LdpPut {

    private String key;

    private String column;

    private Object data;

    private long ttl;

    public static LdpPut with(String key,String column,Object data,long ttl){
        LdpPut ldpPut = new LdpPut();
        ldpPut.setKey(key);
        ldpPut.setColumn(column);
        ldpPut.setData(data);
        ldpPut.setTtl(ttl);
        return ldpPut;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        if (data instanceof String || data instanceof Long) {
            this.data = data;
        } else {
            throw new IllegalArgumentException("LdpPut data attribute must be of type String or Long!");
        }
    }

    public long getTtl() {
        return ttl;
    }

    public void setTtl(long ttl) {
        this.ttl = ttl;
    }
}
