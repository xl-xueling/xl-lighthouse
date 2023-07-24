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
import com.dtstep.lighthouse.common.aggregator.EventPool;
import com.dtstep.lighthouse.common.aggregator.SlotsGroup;
import com.dtstep.lighthouse.common.constant.StatConst;
import com.dtstep.lighthouse.common.entity.event.DimensBucket;
import com.dtstep.lighthouse.core.storage.proxy.DimensStorageProxy;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public final class DimensStorageThread extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(DimensStorageThread.class);

    private final EventPool<DimensBucket> eventPool;

    private static final int batchSize = 2000;

    DimensStorageThread(EventPool<DimensBucket> eventPool){
        this.eventPool = eventPool;
    }

    @Override
    public void run() {
        IntStream.range(0, eventPool.slotSize()).forEach(this::consumer);
    }

    public void consumer(int slot) {
        try{
            SlotsGroup.SlotWrapper<DimensBucket> slotWrapper = eventPool.take(slot);
            while (slotWrapper.size() > batchSize * StatConst.backlog_factor || System.currentTimeMillis() - slotWrapper.getLastAccessTime() > TimeUnit.SECONDS.toMillis(20)) {
                StopWatch stopWatch = new StopWatch();
                stopWatch.start();
                List<DimensBucket> events = slotWrapper.getEvents(batchSize);
                if(CollectionUtils.isEmpty(events)){
                    break;
                }
                DimensStorageProxy.put(events);
                logger.info("process dimens events,thread:{},slot:{},process size:{},remaining size:{},capacity:{},accessTime:{},cost:{}ms",
                        Thread.currentThread().getName(),slot,events.size(),slotWrapper.size(),slotWrapper.getCapacity(),slotWrapper.getLastAccessTime(),stopWatch.getTime());
            }
        }catch (Exception ex){
            logger.error("process dimens events error!",ex);
        }
    }
}
