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
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Properties;


public final class KafkaProducerFactory implements PooledObjectFactory<KafkaProducer<byte[],byte[]>>, Serializable {

    private static final long serialVersionUID = -480300753690256599L;

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerFactory.class);

    private final Properties properites;

    public KafkaProducerFactory(Properties properites){
        this.properites = properites;
    }

    @Override
    public PooledObject<KafkaProducer<byte[], byte[]>> makeObject(){
        KafkaProducer<byte[],byte[]> kafkaProducer = new KafkaProducer<>(properites);
        return new DefaultPooledObject<>(kafkaProducer);
    }

    @Override
    public void destroyObject(PooledObject<KafkaProducer<byte[],byte[]>> pooledObject){
        KafkaProducer<byte[],byte[]> producer = pooledObject.getObject();
        producer.close();
    }

    @Override
    public boolean validateObject(PooledObject<KafkaProducer<byte[],byte[]>> pooledObject) {
        return true;
    }

    @Override
    public void activateObject(PooledObject<KafkaProducer<byte[],byte[]>> pooledObject) throws Exception {

    }

    @Override
    public void passivateObject(PooledObject<KafkaProducer<byte[],byte[]>> pooledObject) throws Exception {

    }
}
