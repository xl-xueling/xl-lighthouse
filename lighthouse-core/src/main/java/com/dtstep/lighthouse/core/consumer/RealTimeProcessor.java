package com.dtstep.lighthouse.core.consumer;
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
import com.dtstep.lighthouse.common.constant.StatConst;
import com.dtstep.lighthouse.common.entity.event.SimpleSlotEvent;
import com.dtstep.lighthouse.common.util.StringUtil;
import com.dtstep.lighthouse.common.aggregator.BlockingEventPool;
import com.dtstep.lighthouse.common.aggregator.EventPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class RealTimeProcessor {

    private static final Logger logger = LoggerFactory.getLogger(RealTimeProcessor.class);

    private static EventPool<SimpleSlotEvent> eventPool;

    private static final int consumerThreadSize = 5;

    static {
        try{
            eventPool = new BlockingEventPool<>("ExpandedEventPool",StatConst.DEFAULT_POOL_SLOT_SIZE,500000);
            ExpandedConsumer expandedConsumer = new ExpandedConsumer(eventPool, consumerThreadSize);
            expandedConsumer.start();
        }catch (Exception ex){
            logger.error("lighthouse tasks consumer start error!",ex);
        }
    }

    public static void onEvent(int slot, String message, int repeat){
        try{
            if(StringUtil.isNotEmpty(message)){
                eventPool.put(slot,new SimpleSlotEvent(message,repeat));
            }
        }catch (Exception ex){
            logger.error("process expanded events error!",ex);
        }
    }

}
