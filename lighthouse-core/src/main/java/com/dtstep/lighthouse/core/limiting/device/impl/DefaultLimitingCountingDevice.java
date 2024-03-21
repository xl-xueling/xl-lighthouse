package com.dtstep.lighthouse.core.limiting.device.impl;
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
import com.dtstep.lighthouse.core.callback.CaffeineExpiry;
import com.dtstep.lighthouse.core.limiting.device.CountingDevice;
import com.dtstep.lighthouse.core.storage.result.ResultStorageSelector;
import com.dtstep.lighthouse.core.wrapper.StatDBWrapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Scheduler;
import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.entity.view.StatValue;
import com.dtstep.lighthouse.common.util.DateUtil;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class DefaultLimitingCountingDevice implements CountingDevice {

    private static final Logger logger = LoggerFactory.getLogger(DefaultLimitingCountingDevice.class);

    private final static Cache<Pair<Integer,Integer>, Value> valueCache = Caffeine.newBuilder()
            .expireAfter(new CaffeineExpiry.ExpiryAfterLastAccess<Pair<Integer,Integer>, Value>(TimeUnit.MINUTES.toMillis(2)))
            .scheduler(Scheduler.systemScheduler())
            .maximumSize(300000)
            .softValues()
            .build();

    public boolean tryRequire(Params params) throws Exception {
        int threshold = params.getPermitsPerSecond();
        Value value = valueCache.get(Pair.of(params.getBuiltinStat().getId(), params.getRelationId()), k -> Value.newVar());
        assert value != null;
        value.setAccessTime(System.currentTimeMillis());
        if(logger.isTraceEnabled()){
            logger.trace("default limiting counting device check,builtInStatId:{},relationId:{},value:{},threshold:{}"
                    ,params.getBuiltinStat().getId(),params.getRelationId(),value.getV(),threshold * 60);
        }
        return threshold * 60L > value.getV();
    }

    static {
        ScheduledExecutorService service = Executors.newScheduledThreadPool(1,
                new BasicThreadFactory.Builder().namingPattern("limiting-counting-schedule-pool-%d").daemon(true).build());
        service.scheduleWithFixedDelay(new RefreshThread(valueCache),0,1, TimeUnit.MINUTES);
    }

    static class RefreshThread implements Runnable {

        private final Cache<Pair<Integer,Integer>, Value> valueCache;

        public RefreshThread(Cache<Pair<Integer,Integer>, Value> valueCache){
            this.valueCache = valueCache;
        }
        @Override
        public void run(){
            ConcurrentMap<Pair<Integer,Integer>, Value> dataMap = valueCache.asMap();
            Set<Pair<Integer,Integer>> sets = dataMap.keySet();
            HashMap<Integer,List<Integer>> paramMap = new HashMap<>();
            for (Pair<Integer, Integer> pair : sets) {
                List<Integer> list = paramMap.computeIfAbsent(pair.getLeft(), k -> new ArrayList<>());
                list.add(pair.getRight());
            }
            try{
                long batchTime = DateUtil.previousBatchTime(1, TimeUnit.MINUTES,System.currentTimeMillis());
                for (Integer builtStatId : paramMap.keySet()){
                    List<Integer> list = paramMap.get(builtStatId);
                    StatExtEntity statExtEntity = StatDBWrapper.queryById(builtStatId);
                    if(statExtEntity != null){
                        List<String> dimensList = list.stream().map(Object::toString).collect(Collectors.toList());
                        Map<String, StatValue> data = ResultStorageSelector.queryWithDimensList(statExtEntity,dimensList,batchTime);
                        if(MapUtils.isNotEmpty(data)){
                            data.forEach((k, v) -> {
                                Value value = valueCache.getIfPresent(Pair.of(builtStatId,Integer.parseInt(k)));
                                assert value != null;
                                value.setV(Long.parseLong(v.getValue().toString()));
                                value.setUpdateTime(System.currentTimeMillis());
                                if(logger.isTraceEnabled()){
                                    logger.trace("default limiting counting device refresh,builtInStatId:{},relationId:{},value:{}"
                                            ,builtStatId,k,value.getV());
                                }
                            });
                        }
                    }
                }
            }catch (Exception ex){
                logger.error("refresh limiting counting data error!",ex);
            }
        }
    }
}

