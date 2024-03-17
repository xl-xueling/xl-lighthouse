package com.dtstep.lighthouse.client;
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
import com.dtstep.lighthouse.client.rpc.RPCClientProxy;
import com.dtstep.lighthouse.common.aggregator.EventPool;
import com.dtstep.lighthouse.common.aggregator.SlotsGroup;
import com.dtstep.lighthouse.common.entity.event.SimpleSlotEvent;
import com.dtstep.lighthouse.common.constant.StatConst;
import com.dtstep.lighthouse.common.enums.fusing.FusingRules;
import com.dtstep.lighthouse.common.fusing.FusingSwitch;
import com.dtstep.lighthouse.common.fusing.FusingToken;
import com.dtstep.lighthouse.common.sbr.StringBuilderHolder;
import com.dtstep.lighthouse.common.util.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

final class DelayRunnable extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(Consumer.class);

    private final EventPool<SimpleSlotEvent> eventPool;

    private final int batchSize;

    private static final long timeThreshold = TimeUnit.SECONDS.toMillis(1);

    DelayRunnable(EventPool<SimpleSlotEvent> eventPool,int batchSize){
        this.setDaemon(true);
        this.eventPool = eventPool;
        this.batchSize = batchSize;
    }

    @Override
    public void run() {
        IntStream.range(0, eventPool.slotSize()).forEach(this::consumer);
    }

    public void consumer(int slot) {
        try{
            SlotsGroup.SlotWrapper<SimpleSlotEvent> slotWrapper = eventPool.take(slot);
            StringBuilder sbr = StringBuilderHolder.Bigger.getStringBuilder();
            while (slotWrapper.size() >= batchSize * StatConst.client_backlog_factor || System.currentTimeMillis() - slotWrapper.getLastAccessTime() > timeThreshold){
                StopWatch stopWatch = new StopWatch();
                stopWatch.start();
                List<SimpleSlotEvent> events = slotWrapper.getEvents(batchSize);
                if(events.isEmpty()){
                    break;
                }
                Map<String,Long> counterMap = events.stream().collect(Collectors.groupingBy(SimpleSlotEvent::getMessage, Collectors.summingLong(SimpleSlotEvent::getRepeat)));
                Iterator<Map.Entry<String,Long>> iterator = counterMap.entrySet().iterator();
                int i = 0;
                while (iterator.hasNext()){
                    Map.Entry<String,Long> entry = iterator.next();
                    if(i != 0){
                        sbr.append(StatConst.SEPARATOR_LEVEL_0);
                    }
                    sbr.append(entry.getKey()).append(StatConst.SEPARATOR_LEVEL_1).append(entry.getValue());
                    i++;
                }
                send(sbr.toString());
                sbr.setLength(0);
                if(logger.isDebugEnabled()){
                    logger.debug("process client message,thread:{},slot:{},process size:{},remaining size:{},capacity:{},accessTime:{},cost:{}ms",
                            Thread.currentThread().getName(),slot,events.size(),slotWrapper.size(),slotWrapper.getCapacity(),slotWrapper.getLastAccessTime(),stopWatch.getTime());
                }
                events.clear();
            }
        }catch (Exception ex){
            logger.error("lighthouse send client messages error!",ex);
        }
    }

    public void send(String text){
        FusingToken fusingToken = null;
        try{
            fusingToken = FusingSwitch.entry(FusingRules.CLIENT_EXCEPTION_RULE);
            if(fusingToken == null){
                logger.error("number of exceptions reaches the threshold, the call is blocked!");
                return;
            }
            RPCClientProxy.instance().send(text);
        }catch (Ice.NotRegisteredException ex){
            LightHouse._InitFlag.set(false);
            logger.error("lighthouse client failed to send message!",ex);
            FusingSwitch.track(fusingToken);
        }catch (Exception ex){
            logger.error("lighthouse client failed to send message!",ex);
            FusingSwitch.track(fusingToken);
        }
    }
}
