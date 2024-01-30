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
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CaffeineCacheConfig {

    @Bean
    public CacheManager caffeineCacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        List<CaffeineCache> cacheCaches = initCaffeineCache();
        cacheManager.setCaches(cacheCaches);
        return cacheManager;
    }

    private static List<CaffeineCache> initCaffeineCache() {
        List<CaffeineCache> caffeineCacheList = new ArrayList<>();
        CaffeineCache miniDurationCache = new CaffeineCache("ShortPeriod",Caffeine.newBuilder().recordStats()
                .expireAfterWrite(30,TimeUnit.SECONDS).maximumSize(300000).build());
        CaffeineCache durationCache = new CaffeineCache("NormalPeriod",Caffeine.newBuilder().recordStats()
                .expireAfterWrite(10,TimeUnit.MINUTES).maximumSize(300000).build());
        CaffeineCache maxDurationCache = new CaffeineCache("LongPeriod",Caffeine.newBuilder().recordStats()
                .expireAfterWrite(2,TimeUnit.HOURS).maximumSize(300000).build());
        caffeineCacheList.add(miniDurationCache);
        caffeineCacheList.add(durationCache);
        caffeineCacheList.add(maxDurationCache);
        return caffeineCacheList;
    }
}
