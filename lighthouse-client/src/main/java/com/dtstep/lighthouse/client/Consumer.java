package com.dtstep.lighthouse.client;
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
import Ice.Communicator;
import com.dtstep.lighthouse.common.aggregator.EventPool;
import com.dtstep.lighthouse.common.entity.event.SimpleSlotEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;


final class Consumer {

    private static final Logger logger = LoggerFactory.getLogger(Consumer.class);

    private static final int threadSize = 4;

    private static ScheduledExecutorService executor;

    private final EventPool<SimpleSlotEvent> eventPool;

    private final Communicator communicator;

    private final int frequency;

    private final int batchSize;

    Consumer(Communicator communicator, EventPool<SimpleSlotEvent> eventPool, final int frequency, final int batchSize){
        this.communicator = communicator;
        this.eventPool = eventPool;
        this.frequency = frequency;
        this.batchSize = batchSize;
        if(executor != null && !executor.isShutdown()){
            executor.shutdown();
            try{
                if(!executor.awaitTermination(3,TimeUnit.SECONDS)){
                    logger.error("lighthouse executor shutdown failed!");
                }
            }catch (Exception ex) {
                logger.error("lighthouse executor shutdown failed!",ex);
            }
        }
        executor = Executors.newScheduledThreadPool(threadSize);
    }


    public void start(){
        for(int i = 0; i< threadSize; i++){
            executor.scheduleWithFixedDelay(new DelayRunnable(eventPool,communicator,batchSize),0,frequency, TimeUnit.MILLISECONDS);
        }
    }

    public void shutdown(){
        if(executor != null && !executor.isShutdown()){
            executor.shutdown();
        }
    }

    public void destroy() throws Exception{
        int delay = 30;
        if(!executor.isShutdown()){
            for(int i=0;i<delay;i++){
                if(eventPool.isEmpty()){
                    break;
                }
                Thread.sleep(1000);
            }
            executor.shutdown();
        }
        if(!eventPool.isEmpty()){
            eventPool.clear();
            throw new InterruptedException("lighthouse thread resource release exception!");
        }
    }
}
