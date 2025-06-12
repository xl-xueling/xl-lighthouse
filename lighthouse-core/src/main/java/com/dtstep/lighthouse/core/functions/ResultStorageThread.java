package com.dtstep.lighthouse.core.functions;
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
import com.dtstep.lighthouse.core.storage.result.ResultStorageSelector;
import com.dtstep.lighthouse.common.constant.StatConst;
import com.dtstep.lighthouse.common.entity.calculate.MicroCalculateEnum;
import com.dtstep.lighthouse.common.entity.calculate.MicroBucket;
import com.dtstep.lighthouse.common.aggregator.EventPool;
import com.dtstep.lighthouse.common.aggregator.SlotsGroup;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ResultStorageThread extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(ResultStorageThread.class);

    private static final long _threadConsumePeriod = TimeUnit.SECONDS.toMillis(20);

    private static final long _maximumBacklogPeriod = TimeUnit.MINUTES.toMillis(2);

    private final EventPool<MicroBucket> eventPool;

    private final int batchSize;

    ResultStorageThread(EventPool<MicroBucket> eventPool, int batchSize){
        this.eventPool = eventPool;
        this.batchSize = batchSize;
    }

    @Override
    public void run() {
        IntStream.range(0, eventPool.slotSize()).forEach(this::consumer);
    }

    public void consumer(int slot) {
        try{
            SlotsGroup.SlotWrapper<MicroBucket> slotWrapper = eventPool.take(slot);
            while (slotWrapper.size() > batchSize * StatConst.storage_backlog_factor
                    || System.currentTimeMillis() - slotWrapper.getLastAccessTime() > _threadConsumePeriod
                    || System.currentTimeMillis() - slotWrapper.getHeadElementTime() > _maximumBacklogPeriod){
                StopWatch stopWatch = new StopWatch();
                stopWatch.start();
                List<MicroBucket> events = slotWrapper.getEvents(batchSize);
                if(CollectionUtils.isEmpty(events)){
                    break;
                }
                if(logger.isDebugEnabled()){
                    logger.debug("process storage events start,thread:{},slot:{},process size:{}",Thread.currentThread().getName(),slot,events.size());
                }
                List<MicroBucket> incrementList = events.stream().filter(x -> x.getCalculateEnum() == MicroCalculateEnum.IncCalculate).collect(Collectors.toList());
                if(!incrementList.isEmpty()){
                    ResultStorageSelector.increment(incrementList);
                }
                List<MicroBucket> maxPutList = events.stream().filter(x -> x.getCalculateEnum() == MicroCalculateEnum.MaxCalculate).collect(Collectors.toList());
                if(!maxPutList.isEmpty()){
                    ResultStorageSelector.maxPut(maxPutList);
                }
                List<MicroBucket> minPutList = events.stream().filter(x -> x.getCalculateEnum() == MicroCalculateEnum.MinCalculate).collect(Collectors.toList());
                if(!minPutList.isEmpty()){
                    ResultStorageSelector.minPut(minPutList);
                }
                List<MicroBucket> seqPutList = events.stream().filter(x -> x.getCalculateEnum() == MicroCalculateEnum.SeqCalculate).collect(Collectors.toList());
                if(!seqPutList.isEmpty()){
                    ResultStorageSelector.put(seqPutList);
                }
                List<MicroBucket> limitList = events.stream().filter(MicroBucket::isLimit).collect(Collectors.toList());
                if(!limitList.isEmpty()){
                    LimitStatProcess.getInstance().process(limitList);
                }
                long cost = stopWatch.getTime();
                logger.info("process storage events,thread:{},slot:{},process size:{},remaining size:{},capacity:{},accessTime:{},cost:{}ms",
                        Thread.currentThread().getName(),slot,events.size(),slotWrapper.size(),slotWrapper.getCapacity(),slotWrapper.getLastAccessTime(),cost);
                if(cost > _threadConsumePeriod){
                    logger.warn("batch processing storage events takes too long, and may cause message delays" +
                            ",thread:{},slot:{},cost:{}",Thread.currentThread().getName(),slot,String.format("[%sms > %sms]",cost,_threadConsumePeriod));
                }
            }
        }catch (Exception ex){
            logger.error("process storage events error!",ex);
        }
    }
}
