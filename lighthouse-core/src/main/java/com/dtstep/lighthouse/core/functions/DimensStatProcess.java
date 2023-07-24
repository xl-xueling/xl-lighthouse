package com.dtstep.lighthouse.core.functions;
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
import com.google.common.hash_snp.Hashing;
import com.dtstep.lighthouse.common.aggregator.BlockingEventPool;
import com.dtstep.lighthouse.common.aggregator.EventPool;
import com.dtstep.lighthouse.common.constant.StatConst;
import com.dtstep.lighthouse.common.counter.CycleCounterAdvisor;
import com.dtstep.lighthouse.common.entity.event.DimensBucket;
import com.dtstep.lighthouse.common.entity.group.GroupExtEntity;
import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.entity.stat.TemplateEntity;
import com.dtstep.lighthouse.common.hash.HashUtil;
import com.dtstep.lighthouse.core.batch.BatchAdapter;
import com.dtstep.lighthouse.core.roaring.BitSetFilterSupplier;
import com.dtstep.lighthouse.core.wrapper.GroupDBWrapper;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.StringTokenizer;
import java.util.concurrent.*;


public final class DimensStatProcess extends Process {

    private static final EventPool<DimensBucket> eventPool = new BlockingEventPool<>("DimensStorageEventPool",4,30000);

    private static final DimensStatProcess instance  = new DimensStatProcess();

    private static final Logger logger = LoggerFactory.getLogger(DimensStatProcess.class);

    public static DimensStatProcess getInstance(){
        return instance;
    }

    static {
        final int threadSize = 2;
        ScheduledExecutorService service = Executors.newScheduledThreadPool(threadSize,
                new BasicThreadFactory.Builder().namingPattern("dimens-consumer-schedule-pool-%d").daemon(true).build());
        for (int i = 0; i < threadSize; i++) {
            service.scheduleWithFixedDelay(new DimensStorageThread(eventPool), 0, 20, TimeUnit.SECONDS);
        }
    }

    public void process(StatExtEntity statExtEntity, String dimensValue) throws Exception{
        GroupExtEntity groupExtEntity = GroupDBWrapper.queryById(statExtEntity.getGroupId());
        if(groupExtEntity == null){
            return;
        }
        BitSetFilterSupplier roaringBitMap = BitSetFilterSupplier.getInstance();
        TemplateEntity templateEntity = statExtEntity.getTemplateEntity();
        String[] dimensArr = templateEntity.getDimensArr();
        int i=0;
        StringTokenizer stringTokenizer = new StringTokenizer(dimensValue, StatConst.DIMENS_SEPARATOR);
        while(stringTokenizer.hasMoreTokens()){
            String value = stringTokenizer.nextToken();
            String existKey = groupExtEntity.getToken() + "_" + dimensArr[i] + "_" + value;
            long hash = Math.abs(Hashing.murmur3_128().hashBytes(existKey.getBytes(StandardCharsets.UTF_8)).asLong());
            if(!roaringBitMap.check("dimens_filter_keys",hash)){
                long winTime = BatchAdapter.getBatch(1,TimeUnit.MINUTES,System.currentTimeMillis());
                String symbol = "dimens_storage_limit" + "_"  + groupExtEntity.getToken() + "_" + dimensArr[i];
                if(CycleCounterAdvisor.incrementAndGet(symbol,winTime) > StatConst.DIMENS_VALUE_STORAGE_DURATION_MAX_SIZE){
                    continue;
                }
                long ttl = getTTL(groupExtEntity.getDataExpire());
                if(logger.isTraceEnabled()){
                    logger.trace("lighthouse trace,save dimens,token:{},dimens:{},dimensValue:{},ttl:{}", groupExtEntity.getToken(),dimensArr[i],value,ttl);
                }
                DimensBucket dimensBucket = new DimensBucket(groupExtEntity.getToken(),dimensArr[i],value,ttl);
                int slot = HashUtil.getHashIndex(dimensBucket.getToken() + "_" + dimensBucket.getDimens() + "_" + dimensBucket.getDimensValue(),eventPool.slotSize());
                eventPool.put(slot, dimensBucket);
            }
            i++;
        }
    }
}
