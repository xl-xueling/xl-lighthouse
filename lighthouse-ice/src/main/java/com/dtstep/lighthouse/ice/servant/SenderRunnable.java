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
import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import com.dtstep.lighthouse.common.constant.StatConst;
import com.dtstep.lighthouse.common.constant.SysConst;
import com.dtstep.lighthouse.common.sbr.StringBuilderHolder;
import com.dtstep.lighthouse.common.util.SnappyUtil;
import com.dtstep.lighthouse.core.kafka.KafkaSender;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;


final class SenderRunnable {

    private static final Logger logger = LoggerFactory.getLogger(SenderRunnable.class);

    private final int threadSize;

    private final BlockingDeque<Pair<String,Integer>> queue;

    private final ScheduledExecutorService service;

    private static final long TIMEOUT = TimeUnit.SECONDS.toMillis(3);

    SenderRunnable(BlockingDeque<Pair<String,Integer>> queue, int threadSize){
        this.queue = queue;
        this.threadSize = threadSize;
        service = Executors.newScheduledThreadPool(threadSize,
                new BasicThreadFactory.Builder().namingPattern("ice-sender-schedule-pool-%d").daemon(true).build());
    }

    void start(){
        for(int i = 0; i< threadSize; i++){
            service.scheduleWithFixedDelay(new SendExecutor(queue),0,2, TimeUnit.SECONDS);
        }
    }

    private static class SendExecutor implements Runnable{

        BlockingDeque<Pair<String,Integer>> queue;

        private long lastBatchTime;

        private static final int SENDER_SIZE = 100;

        private static final int PROCESS_BATCH_SIZE = 30000;

        SendExecutor(BlockingDeque<Pair<String,Integer>> queue){
            this.queue = queue;
        }

        @Override
        public void run() {
            StringBuilder sbr = StringBuilderHolder.Bigger.getStringBuilder();
            List<Pair<String,Integer>> list = Lists.newArrayList();
            try{
                int i = 0;
                while (queue.size() > PROCESS_BATCH_SIZE || System.currentTimeMillis() - lastBatchTime > TIMEOUT) {
                    Queues.drain(queue, list, PROCESS_BATCH_SIZE, 1, TimeUnit.MICROSECONDS);
                    if(list.isEmpty()){
                        break;
                    }
                    logger.info("the ice service batch processed {} messages!",list.size());
                    Map<String,Long> counterMap = list.parallelStream().collect(Collectors.groupingBy(Pair::getLeft, Collectors.summingLong(Pair::getRight)));
                    for (Map.Entry<String, Long> entry : counterMap.entrySet()) {
                        String str = entry.getKey() + StatConst.SEPARATOR_LEVEL_1 + entry.getValue();
                        if (i != 0) {
                            sbr.append(StatConst.SEPARATOR_LEVEL_0);
                        }
                        sbr.append(str);
                        i++;
                        if (i > SENDER_SIZE) {
                            KafkaSender.send(SysConst._MESSAGE_PREFIX + "#" + SnappyUtil.compress(sbr.toString()));
                            sbr.setLength(0);
                            i = 0;
                        }
                    }
                    if(sbr.length() > 0){
                        KafkaSender.send(SysConst._MESSAGE_PREFIX + "#" + SnappyUtil.compress(sbr.toString()));
                        sbr.setLength(0);
                        i = 0;
                    }
                    lastBatchTime = System.currentTimeMillis();
                }
            }catch (Exception ex){
                logger.error("ice server process message error!",ex);
            }finally {
                list.clear();
            }
        }
    }
}
