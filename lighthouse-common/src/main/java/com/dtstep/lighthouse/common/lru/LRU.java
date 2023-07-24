package com.dtstep.lighthouse.common.lru;
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
import com.dtstep.lighthouse.common.lru.impl.LDPCache;

import java.util.concurrent.TimeUnit;

public final class LRU<K,V> {

    private long expireAfterWriteSeconds = -1;

    private long expireAfterAccessSeconds = -1;

    private long maximumSize = 300;

    private boolean softValues = true;

    public long getExpireAfterWriteSeconds() {
        return expireAfterWriteSeconds;
    }

    public void setExpireAfterWriteSeconds(long expireAfterWriteSeconds) {
        this.expireAfterWriteSeconds = expireAfterWriteSeconds;
    }

    public long getExpireAfterAccessSeconds() {
        return expireAfterAccessSeconds;
    }

    public void setExpireAfterAccessSeconds(long expireAfterAccessSeconds) {
        this.expireAfterAccessSeconds = expireAfterAccessSeconds;
    }

    public boolean isSoftValues() {
        return softValues;
    }

    public void setSoftValues(boolean softValues) {
        this.softValues = softValues;
    }

    public long getMaximumSize() {
        return maximumSize;
    }

    public void setMaximumSize(long maximumSize) {
        this.maximumSize = maximumSize;
    }

    public static LRU<Object, Object> newBuilder(){
        return new LRU<>();
    }

    private LRU() {
    }

    public LRU<K, V> expireAfterWrite(long interval, TimeUnit unit){
        this.expireAfterWriteSeconds = unit.toSeconds(interval);
        return this;
    }

    public LRU<K, V> expireAfterAccess(long interval, TimeUnit unit){
        this.expireAfterAccessSeconds = unit.toSeconds(interval);
        return this;
    }

    public LRU<K, V> maximumSize(long maximumSize) {
        this.maximumSize = maximumSize;
        return this;
    }

    public LRU<K, V> softValues(){
        this.softValues = true;
        return this;
    }

    public <K1 extends K ,V1 extends V> Cache<K1,V1> build(){
        return new LDPCache<K1,V1>(this);
    }
}
