package com.dtstep.lighthouse.core.callback;
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
import com.github.benmanes.caffeine.cache.Expiry;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class CaffeineExpiry {

    public static class ExpiryAfterCreate<K,V extends CacheValue> implements Expiry<K,V> {

        private long expireMills;

        public ExpiryAfterCreate(long expireMills){
            this.expireMills = expireMills;
        }

        public long getExpireMills() {
            return expireMills;
        }

        public void setExpireMills(long expireMills) {
            this.expireMills = expireMills;
        }

        @Override
        public long expireAfterCreate(@NonNull K k, @NonNull V v, long l) {
            if(v.getCreateTime() + expireMills > System.currentTimeMillis()){
                return l + 1;
            }else{
                return 0;
            }
        }

        @Override
        public long expireAfterUpdate(@NonNull K k, @NonNull V v, long l, @NonNegative long l1) {
            if(v.getCreateTime() + expireMills > System.currentTimeMillis()){
                return l + 1;
            }else{
                return 0;
            }
        }

        @Override
        public long expireAfterRead(@NonNull K k, @NonNull V v, long l, @NonNegative long l1) {
            if(v.getCreateTime() + expireMills > System.currentTimeMillis()){
                return l + 1;
            }else{
                return 0;
            }
        }
    }

    public static class ExpiryAfterLastAccess<K,V extends CacheValue> implements Expiry<K,V> {

        private long expireMills;

        public ExpiryAfterLastAccess(long expireNanos){
            this.expireMills = expireNanos;
        }

        public long getExpireMills() {
            return expireMills;
        }

        public void setExpireMills(long expireMills) {
            this.expireMills = expireMills;
        }

        @Override
        public long expireAfterCreate(@NonNull K k, @NonNull V v, long l) {
            if(v.getAccessTime() + expireMills > System.currentTimeMillis()){
                return l + 1;
            }else{
                return 0;
            }
        }

        @Override
        public long expireAfterUpdate(@NonNull K k, @NonNull V v, long l, @NonNegative long l1) {
            if(v.getAccessTime() + expireMills > System.currentTimeMillis()){
                return l + 1;
            }else{
                return 0;
            }
        }

        @Override
        public long expireAfterRead(@NonNull K k, @NonNull V v, long l, @NonNegative long l1) {
            if(v.getAccessTime() + expireMills > System.currentTimeMillis()){
                return l + 1;
            }else{
                return 0;
            }
        }
    }

}
