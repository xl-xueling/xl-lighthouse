package com.dtstep.lighthouse.ice.servant;
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
import Ice.Current;
import com.dtstep.lighthouse.ice.servant.disruptor.IceEventHandler;
import com.google.common.base.Splitter;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.dtstep.lighthouse.common.constant.StatConst;
import com.dtstep.lighthouse.common.ice._ReceiverInterfaceDisp;
import com.dtstep.lighthouse.common.util.SnappyUtil;
import com.dtstep.lighthouse.common.util.StringUtil;
import com.dtstep.lighthouse.common.entity.event.IceEvent;
import com.dtstep.lighthouse.core.disruptor.IceEventProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;


final class ReceiverI extends _ReceiverInterfaceDisp {

    private static final long serialVersionUID = -8319019055700701595L;

    private static final Logger logger = LoggerFactory.getLogger(ReceiverI.class);

    ReceiverI() {}

    private static final IceEventProducer eventProducer;

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
        RingBuffer<IceEvent> ringBuffer = disruptor.getRingBuffer();
        eventProducer = new IceEventProducer(ringBuffer);
    }

    @Override
    public String logic(byte[] bytes, Current __current) {
        try{
            if(bytes == null){
                return null;
            }
            String data;
            if(SnappyUtil.isCompress(bytes)){
                data = SnappyUtil.uncompressByte(bytes);
            }else{
                data = new String(bytes, StandardCharsets.UTF_8);
            }
            if(StringUtil.isEmpty(data)){
                return null;
            }
            if(logger.isDebugEnabled()){
                logger.debug("lighthouse debug,ice service receive message:{}",data);
            }
            for (String temp : Splitter.on(StatConst.SEPARATOR_LEVEL_0).split(data)) {
                if (!StringUtil.isEmpty(temp)) {
                    int index = temp.lastIndexOf(StatConst.SEPARATOR_LEVEL_1);
                    eventProducer.onData(temp.substring(0, index), Integer.parseInt(temp.substring(index + 1)));
                }
            }
        }catch (Exception ex){
            logger.error("ice logic error",ex);
        }
        return null;
    }
}
