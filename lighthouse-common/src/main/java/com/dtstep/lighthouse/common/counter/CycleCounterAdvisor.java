package com.dtstep.lighthouse.common.counter;
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
import com.dtstep.lighthouse.common.lru.Cache;
import com.dtstep.lighthouse.common.lru.LRU;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

public class CycleCounterAdvisor {

    private static final Cache<String, LongAdder> counterCache =
            LRU.newBuilder().maximumSize(500000).expireAfterAccess(3, TimeUnit.MINUTES).softValues().build();

    public static long incrementAndGet(String symbol,long windowTime){
        String counterKey = symbol + "_" + windowTime;
        LongAdder longAdder = counterCache.get(counterKey,k -> new LongAdder());
        longAdder.increment();
        return longAdder.longValue();
    }

    public static void increment(String symbol,long windowTime){
        String counterKey = symbol + "_" + windowTime;
        LongAdder longAdder = counterCache.get(counterKey,k -> new LongAdder());
        longAdder.increment();
    }

    public static long getValue(String symbol,long windowTime){
        String counterKey = symbol + "_" + windowTime;
        LongAdder longAdder = counterCache.get(counterKey,k -> new LongAdder());
        return longAdder.longValue();
    }
}