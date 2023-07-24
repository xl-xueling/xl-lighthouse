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
import com.dtstep.lighthouse.common.aggregator.EventPool;
import com.dtstep.lighthouse.common.entity.event.SimpleSlotEvent;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.*;


public final class ExpandedConsumer {

    private final int threadSize;

    private final EventPool<SimpleSlotEvent> eventPool;

    private final ScheduledExecutorService service;

    ExpandedConsumer(EventPool<SimpleSlotEvent> eventPool, int threadSize){
        this.eventPool = eventPool;
        this.threadSize = threadSize;
        service = Executors.newScheduledThreadPool(threadSize,
                        new BasicThreadFactory.Builder().namingPattern("expanded-event-schedule-pool-%d").daemon(true).build());
    }

    public void start(){
        for(int i = 0; i< threadSize; i++){
            service.scheduleWithFixedDelay(new ExpandedEventRunnable(eventPool),0,5, TimeUnit.SECONDS);
        }
    }

}
