package com.dtstep.lighthouse.client;
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
import com.dtstep.lighthouse.client.rpc.RPCClientProxy;
import com.dtstep.lighthouse.common.aggregator.BlockingEventPool;
import com.dtstep.lighthouse.common.aggregator.EventPool;
import com.dtstep.lighthouse.common.entity.event.SimpleSlotEvent;
import com.dtstep.lighthouse.common.fusing.FusingToken;
import com.dtstep.lighthouse.common.fusing.FusingSwitch;
import com.dtstep.lighthouse.common.enums.fusing.FusingRules;
import com.dtstep.lighthouse.common.exception.InitializationException;
import com.dtstep.lighthouse.common.exception.LightSendException;
import com.dtstep.lighthouse.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;


public final class LightHouse {

    private static final Logger logger = LoggerFactory.getLogger(LightHouse.class);

    private static int consumerFrequency = 500;

    private static int consumerBatchSize = 200;

    private static final EventPool<SimpleSlotEvent> eventPool = new BlockingEventPool<>("ClientEventPool",3,200000);

    private static final Producer _producer = new Producer(eventPool);

    static final AtomicBoolean _InitFlag = new AtomicBoolean(false);

    private static final AtomicBoolean _LightSwitch = new AtomicBoolean(true);

    private static final String KEY_PROCESS_FREQUENCY = "lighthouse_process_frequency";

    private static final String KEY_PROCESS_BATCH = "lighthouse_process_batch";

    private LightHouse(){}

    public static synchronized void init(final String locators) throws Exception {
        if(!_InitFlag.get()){
            boolean result = RPCClientProxy.instance().init(locators);
            if(!result){
                throw new InitializationException(String.format("lighthouse remote service not available,locators:%s",locators));
            }
            _InitFlag.set(true);
            Consumer consumer = new Consumer(eventPool, consumerFrequency, consumerBatchSize);
            consumer.start();
        }
    }

    public static synchronized void init(final String locators,Properties properties) throws Exception{
        if(properties.containsKey(KEY_PROCESS_FREQUENCY)){
            consumerFrequency = (Integer) properties.get(KEY_PROCESS_FREQUENCY);
        }else if(properties.containsKey(KEY_PROCESS_BATCH)){
            consumerBatchSize = (Integer) properties.get(KEY_PROCESS_BATCH);
        }
        init(locators);
    }

    public static void stat(final String token,final String secretKey,Map<String,Object> paramMap,final long timestamp) throws Exception{
        stat(token,secretKey,paramMap,1,timestamp);
    }

    public static void stat(final String token, final String secretKey, Map<String,Object> paramMap, final int repeat, final long timestamp) throws Exception{
        if(!_LightSwitch.get()){
            return;
        }
        if(StringUtil.isEmpty(token) || StringUtil.isEmpty(secretKey) || paramMap == null){
            throw new IllegalArgumentException("send message failed,required parameters missing!");
        }
        if(!_InitFlag.get()){
            throw new InitializationException("connection is not initialized or the connection is abnormal, statistics ignored!");
        }
        FusingToken fusingToken = null;
        try{
            fusingToken = FusingSwitch.entry(FusingRules.CLIENT_EXCEPTION_RULE);
            if(fusingToken == null){
                logger.error("number of exceptions reaches the threshold, the call is blocked!");
                return;
            }
            _producer.send(token,secretKey,paramMap,repeat,timestamp);
        }catch (Ice.NotRegisteredException ex){
            LightHouse._InitFlag.set(false);
            logger.error("lighthouse client failed to send message!",ex);
            FusingSwitch.track(fusingToken);
            throw new LightSendException(ex);
        }catch (Exception ex){
            logger.error("lighthouse client failed to send message!",ex);
            FusingSwitch.track(fusingToken);
            throw new LightSendException(ex);
        }
    }


}
