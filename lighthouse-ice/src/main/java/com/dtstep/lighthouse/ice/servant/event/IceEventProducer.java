package com.dtstep.lighthouse.ice.servant.event;
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
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.dtstep.lighthouse.common.entity.event.IceEvent;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.Executors;


public final class IceEventProducer {

    private static final RingBuffer<IceEvent> ringBuffer;

    static {
        Disruptor<IceEvent> disruptor = new Disruptor<>(
                IceEvent::new,
                1024 * 1024 * 2,
                Executors.defaultThreadFactory(),
                ProducerType.MULTI,
                new BlockingWaitStrategy()
        );
        IceEventHandler[] handlers = new IceEventHandler[10];
        for(int i=0;i<handlers.length;i++){
            handlers[i] = new IceEventHandler();
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
