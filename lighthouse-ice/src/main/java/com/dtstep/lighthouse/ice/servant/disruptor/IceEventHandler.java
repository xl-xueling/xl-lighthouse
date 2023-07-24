package com.dtstep.lighthouse.ice.servant.disruptor;
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
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;
import com.dtstep.lighthouse.common.constant.StatConst;
import com.dtstep.lighthouse.common.entity.event.IceEvent;
import com.dtstep.lighthouse.common.sbr.StringBuilderHolder;
import com.dtstep.lighthouse.core.kafka.KafkaSender;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;


public final class IceEventHandler implements EventHandler<IceEvent>, WorkHandler<IceEvent> {

    private static final Logger logger = LoggerFactory.getLogger(IceEventHandler.class);

    @Override
    public void onEvent(IceEvent event, long sequence, boolean endOfBatch) {}

    private static final int CONSUMER_THRESHOLD = 20000;

    private final Multiset<String> multiset = HashMultiset.create(CONSUMER_THRESHOLD);

    private volatile long lastBatchTime;

    private static final int SENDER_SIZE = 60;

    public IceEventHandler(){
        ScheduledExecutorService service = Executors.newScheduledThreadPool(1,
                new BasicThreadFactory.Builder().namingPattern("ice-clear-schedule-pool-%d").daemon(true).build());
        service.scheduleWithFixedDelay(new ClearThread(),5,5, TimeUnit.SECONDS);
    }

    private final ReentrantLock lock = new ReentrantLock(false);

    @Override
    public void onEvent(IceEvent event) {
        lock.lock();
        try{
            multiset.add(event.getMessage(),event.getRepeat());
            if(multiset.entrySet().size() >= CONSUMER_THRESHOLD){
                consumer();
            }
        }catch (Exception ex){
            logger.error("ice consumer error.",ex);
        }finally {
            lock.unlock();
        }
    }


    private void consumer() {
        int sendSize = multiset.entrySet().size();
        if(sendSize == 0){
            return;
        }
        StringBuilder sbr = StringBuilderHolder.Bigger.getStringBuilder();
        int i = 0;
        for (Multiset.Entry<String> entry : multiset.entrySet()) {
            if (i != 0) {
                sbr.append(StatConst.SEPARATOR_LEVEL_0);
            }
            sbr.append(entry.getElement()).append(StatConst.SEPARATOR_LEVEL_1).append(entry.getCount());
            i++;
            if(i >= SENDER_SIZE){
                sendToKafka(sbr.toString());
                sbr.setLength(0);
                i = 0;
            }
        }
        if(sbr.length() > 0){
            sendToKafka(sbr.toString());
            sbr.setLength(0);
        }
        multiset.clear();
        lastBatchTime = System.currentTimeMillis();
        logger.info("lighthouse ice service batch processed {} messages!",sendSize);
    }

    private void sendToKafka(String text) {
        KafkaSender.send(text);
    }

    private final class ClearThread implements Runnable{
        @Override
        public void run() {
            if(System.currentTimeMillis() - lastBatchTime < TimeUnit.SECONDS.toMillis(10)){
                return;
            }
            boolean is = lock.tryLock();
            if(is){
                try{
                    consumer();
                }catch (Exception ex){
                    logger.error("ice consumer error.",ex);
                }finally {
                    lock.unlock();
                }
            }
        }
    }
}

