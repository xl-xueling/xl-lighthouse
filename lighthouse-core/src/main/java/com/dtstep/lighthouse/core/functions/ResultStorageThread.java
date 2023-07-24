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
import com.dtstep.lighthouse.core.storage.proxy.ResultStorageProxy;
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
            while (slotWrapper.size() > batchSize * StatConst.storage_backlog_factor || System.currentTimeMillis() - slotWrapper.getLastAccessTime() > TimeUnit.SECONDS.toMillis(15)){
                StopWatch stopWatch = new StopWatch();
                stopWatch.start();
                List<MicroBucket> events = slotWrapper.getEvents(batchSize);
                if(CollectionUtils.isEmpty(events)){
                    break;
                }
                List<MicroBucket> incrementList = events.stream().filter(x -> x.getCalculateEnum() == MicroCalculateEnum.IncCalculate).collect(Collectors.toList());
                if(!incrementList.isEmpty()){
                    ResultStorageProxy.increment(incrementList);
                }
                List<MicroBucket> maxPutList = events.stream().filter(x -> x.getCalculateEnum() == MicroCalculateEnum.MaxCalculate).collect(Collectors.toList());
                if(!maxPutList.isEmpty()){
                    ResultStorageProxy.maxPut(maxPutList);
                }
                List<MicroBucket> minPutList = events.stream().filter(x -> x.getCalculateEnum() == MicroCalculateEnum.MinCalculate).collect(Collectors.toList());
                if(!minPutList.isEmpty()){
                    ResultStorageProxy.minPut(minPutList);
                }
                List<MicroBucket> seqPutList = events.stream().filter(x -> x.getCalculateEnum() == MicroCalculateEnum.SeqCalculate).collect(Collectors.toList());
                if(!seqPutList.isEmpty()){
                    ResultStorageProxy.put(seqPutList);
                }
                List<MicroBucket> limitList = events.stream().filter(MicroBucket::isLimit).collect(Collectors.toList());
                if(!limitList.isEmpty()){
                    LimitStatProcess.getInstance().process(limitList);
                }
                logger.info("process storage events,thread:{},slot:{},process size:{},remaining size:{},capacity:{},accessTime:{},cost:{}ms",
                        Thread.currentThread().getName(),slot,events.size(),slotWrapper.size(),slotWrapper.getCapacity(),slotWrapper.getLastAccessTime(),stopWatch.getTime());
            }
        }catch (Exception ex){
            logger.error("process storage events error!",ex);
        }
    }
}
