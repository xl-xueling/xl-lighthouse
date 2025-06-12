package com.dtstep.lighthouse.core.ipc;
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
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.dtstep.lighthouse.common.entity.event.IceEvent;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;


public final class DisruptorEventProducer {

    private static final RingBuffer<IceEvent> ringBuffer;

    private static final Logger logger = LoggerFactory.getLogger(DisruptorEventProducer.class);

    static {
        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory();
        long maxMemoryInMB = maxMemory / (1024 * 1024);
        int threadSize = Math.min((int) (maxMemoryInMB / 300), 10);
        int ringBufferSize;
        if(maxMemoryInMB <= 1024){
            ringBufferSize = 1024 * 64;
        }else if(maxMemoryInMB <= 2048){
            ringBufferSize = 1024 * 128;
        }else if(maxMemoryInMB <= 3072){
            ringBufferSize = 1024 * 256;
        }else {
            ringBufferSize = 1024 * 512;
        }
        logger.info("RingBuffer init,maxMemoryInMB:{},thread size:{},ringBufferSize:{}",maxMemoryInMB,threadSize,ringBufferSize);
        Validate.isTrue(threadSize > 0);
        Disruptor<IceEvent> disruptor = new Disruptor<>(
                IceEvent::new,
                ringBufferSize,
                Executors.defaultThreadFactory(),
                ProducerType.MULTI,
                new BlockingWaitStrategy()
        );
        DisruptorEventHandler[] handlers = new DisruptorEventHandler[threadSize];
        for(int i=0;i<handlers.length;i++){
            handlers[i] = new DisruptorEventHandler();
        }
        disruptor.handleEventsWithWorkerPool(handlers);
        disruptor.start();
        ringBuffer = disruptor.getRingBuffer();
    }


    public void onData(final String message,final int repeat) {
        long sequence = ringBuffer.next();
        try {
            IceEvent iceEvent = ringBuffer.get(sequence);
            iceEvent.setMessage(message);
            iceEvent.setRepeat(repeat);
        } finally {
            ringBuffer.publish(sequence);
        }
    }
}
