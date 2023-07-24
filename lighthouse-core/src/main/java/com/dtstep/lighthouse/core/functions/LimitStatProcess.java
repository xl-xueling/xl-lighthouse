package com.dtstep.lighthouse.core.functions;

import com.dtstep.lighthouse.common.aggregator.BlockingEventPool;
import com.dtstep.lighthouse.common.aggregator.EventPool;
import com.dtstep.lighthouse.common.entity.calculate.MicroBucket;
import com.dtstep.lighthouse.common.entity.event.LimitBucket;
import com.dtstep.lighthouse.common.hash.HashUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
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
public class LimitStatProcess extends Process {

    private static final EventPool<LimitBucket> eventPool = new BlockingEventPool<>("LimitStorageEventPool",5,500000);

    private static final LimitStatProcess instance  = new LimitStatProcess();

    private static final Logger logger = LoggerFactory.getLogger(LimitStatProcess.class);

    public static LimitStatProcess getInstance(){
        return instance;
    }

    static {
        final int threadSize = 2;
        ScheduledExecutorService service = Executors.newScheduledThreadPool(threadSize,
                new BasicThreadFactory.Builder().namingPattern("limit-consumer-schedule-pool-%d").daemon(true).build());
        for (int i = 0; i < threadSize; i++) {
            service.scheduleWithFixedDelay(new LimitStorageThread(eventPool), 0, 10, TimeUnit.SECONDS);
        }
    }

    public void process(List<MicroBucket> microBucketList) throws Exception {
        if(CollectionUtils.isEmpty(microBucketList)){
            return;
        }
        for (MicroBucket microBucket : microBucketList) {
            LimitBucket limitBucket = new LimitBucket(microBucket);
            int slot = HashUtil.getHashIndex(limitBucket.getStatId() + "_" + limitBucket.getBatchTime(), eventPool.slotSize());
            eventPool.put(slot, limitBucket);
        }
    }
}
