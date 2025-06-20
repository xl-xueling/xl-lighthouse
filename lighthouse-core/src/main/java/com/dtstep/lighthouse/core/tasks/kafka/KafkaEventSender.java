package com.dtstep.lighthouse.core.tasks.kafka;
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
import com.dtstep.lighthouse.common.random.RandomID;
import com.dtstep.lighthouse.common.util.StringUtil;
import com.dtstep.lighthouse.core.config.LDPConfig;
import com.dtstep.lighthouse.core.kafka.KafkaProducerPool;
import com.dtstep.lighthouse.core.tasks.EventSender;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

public class KafkaEventSender implements EventSender {

    private static final Logger logger = LoggerFactory.getLogger(KafkaEventSender.class);

    private static final KafkaProducerPool producerPool;

    private static final String KAFKA_TOPIC_NAME;

    static {
        producerPool = new KafkaProducerPool();
        KAFKA_TOPIC_NAME = LDPConfig.getVal(LDPConfig.KEY_KAFKA_TOPIC_NAME);
    }

    @Override
    public void send(String text) throws Exception {
        if(StringUtil.isEmpty(text)){
            return;
        }
        KafkaProducer<byte[],byte[]> producer = producerPool.getInstance();
        try{
            producer.send(new ProducerRecord<>(KAFKA_TOPIC_NAME, RandomID.id(15).getBytes(StandardCharsets.UTF_8), text.getBytes(StandardCharsets.UTF_8)));
        }catch (Exception ex){
            logger.error("send message failed!",ex);
        }finally {
            producerPool.release(producer);
        }
    }

    @Deprecated
    @Override
    public void syncSend(String text) throws Exception{
        KafkaProducer<byte[],byte[]> producer = producerPool.getInstance();
        producer.send(new ProducerRecord<>(KAFKA_TOPIC_NAME, RandomID.id(15).getBytes(StandardCharsets.UTF_8),text.getBytes(StandardCharsets.UTF_8)),(recordMetadata,ex) ->  {
            if (ex != null) {
                logger.error("send message failed!",ex);
            }
        }).get();
        producerPool.release(producer);
    }
}
