package com.dtstep.lighthouse.core.kafka;
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
import com.dtstep.lighthouse.core.config.LDPConfig;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;


public final class KafkaProducerPool {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerPool.class);

    private final GenericObjectPool<KafkaProducer<byte[], byte[]>> connectionPool;

    KafkaProducerPool() {
        Properties properties = getConfig();
        KafkaProducerFactory producerFactory = new KafkaProducerFactory(properties);
        GenericObjectPoolConfig<KafkaProducer<byte[],byte[]>> config = new GenericObjectPoolConfig<>();
        config.setMaxTotal(5);
        config.setMaxIdle(3);
        config.setMinIdle(1);
        config.setMaxWaitMillis(5000);
        config.setBlockWhenExhausted(true);
        config.setTestOnReturn(false);
        config.setMinEvictableIdleTimeMillis(60000L);
        config.setTestWhileIdle(true);
        config.setTimeBetweenEvictionRunsMillis(3000);
        config.setNumTestsPerEvictionRun(10);
        connectionPool = new GenericObjectPool<>(producerFactory, config);
    }

    public static Properties getConfig() {
        String bootstrapServers = null;
        try{
            bootstrapServers = LDPConfig.getVal(LDPConfig.KEY_KAFKA_BOOTSTRAP_SERVERS);
        }catch (Exception ex){
            logger.error("ice process exit,get param [{}] fail.", LDPConfig.KEY_KAFKA_BOOTSTRAP_SERVERS, ex);
        }
        assert bootstrapServers != null;
        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapServers);
        props.put("batch.size", 131072);
        props.put("linger.ms", 100);
        props.put("acks","all");
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
        props.put("compression.type", "gzip");
        return props;
    }

    public KafkaProducer<byte[], byte[]> getInstance() {
        try {
            return connectionPool.borrowObject();
        } catch (Exception e) {
            throw new RuntimeException("lighthouse ice get kafka connection exception!", e);
        }
    }

    public void release(KafkaProducer<byte[], byte[]> producer) {
        try {
            connectionPool.returnObject(producer);
        } catch (Exception e) {
            throw new RuntimeException("release Kafka connection exception", e);
        }
    }
}
