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
import com.dtstep.lighthouse.common.entity.group.GroupVerifyEntity;
import com.dtstep.lighthouse.common.entity.stat.StatVerifyEntity;
import com.dtstep.lighthouse.common.entity.view.LimitValue;
import com.dtstep.lighthouse.common.entity.view.StatValue;
import com.dtstep.lighthouse.common.enums.RunningMode;
import com.dtstep.lighthouse.common.fusing.FusingToken;
import com.dtstep.lighthouse.common.fusing.FusingSwitch;
import com.dtstep.lighthouse.common.enums.fusing.FusingRules;
import com.dtstep.lighthouse.common.exception.InitializationException;
import com.dtstep.lighthouse.common.exception.LightSendException;
import com.dtstep.lighthouse.common.util.StringUtil;
import com.zeroc.Ice.NotRegisteredException;
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

    private static RunningMode _runningMode = RunningMode.CLUSTER;

    private LightHouse(){}

    public static void init(final String locators) throws Exception {
        init(locators,RunningMode.CLUSTER);
    }

    public static void init(final String locators,Properties properties) throws Exception{
        if(properties.containsKey(KEY_PROCESS_FREQUENCY)){
            consumerFrequency = Integer.parseInt(properties.getProperty(KEY_PROCESS_FREQUENCY));
        }
        if(properties.containsKey(KEY_PROCESS_BATCH)){
            consumerBatchSize = Integer.parseInt(properties.getProperty(KEY_PROCESS_BATCH));
        }
        init(locators,RunningMode.CLUSTER);
    }

    public static synchronized void init(final String locators, RunningMode runningMode) throws Exception {
        if(!_InitFlag.get()){
            boolean result = RPCClientProxy.instance().init(locators);
            if(!result){
                throw new InitializationException(String.format("lighthouse remote service not available,locators:%s",locators));
            }
            _runningMode = runningMode;
            _InitFlag.set(true);
            Consumer consumer = new Consumer(eventPool, consumerFrequency, consumerBatchSize);
            consumer.start();
        }
    }

    public static void init(final String locators,Properties properties,RunningMode runningMode) throws Exception{
        if(properties.containsKey(KEY_PROCESS_FREQUENCY)){
            consumerFrequency = Integer.parseInt(properties.getProperty(KEY_PROCESS_FREQUENCY));
        }
        if(properties.containsKey(KEY_PROCESS_BATCH)){
            consumerBatchSize = Integer.parseInt(properties.getProperty(KEY_PROCESS_BATCH));
        }
        init(locators,runningMode);
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
        }catch (NotRegisteredException ex){
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

    public static GroupVerifyEntity queryGroupInfo(String token) throws Exception {
        return AuxHandler.queryGroupInfo(token);
    }

    public static StatVerifyEntity queryStatInfo(int statId) throws Exception {
        return AuxHandler.queryStatInfo(statId);
    }

    public static List<StatValue> dataQuery(int statId, String secretKey, String dimensValue, List<Long> batchList) throws Exception {
        StatVerifyEntity statVerifyEntity = AuxHandler.queryStatInfo(statId);
        if(statVerifyEntity == null){
            logger.error("statistic({}) not exist!",statId);
            return null;
        }
        String md5 = AuxHandler.cacheGetMd5(secretKey);
        if(!statVerifyEntity.getVerifyKey().equals(md5)){
            logger.error("client secret-key validation failed,id:{},key:{}",statId,secretKey);
            return null;
        }
        return RPCClientProxy.instance().dataQuery(statId,dimensValue,batchList);
    }

    public static List<StatValue> dataQuery(int statId, String secretKey, String dimensValue, long startTime, long endTime) throws Exception {
        StatVerifyEntity statVerifyEntity = AuxHandler.queryStatInfo(statId);
        if(statVerifyEntity == null){
            logger.error("statistic({}) not exist!",statId);
            return null;
        }
        String md5 = AuxHandler.cacheGetMd5(secretKey);
        if(!statVerifyEntity.getVerifyKey().equals(md5)){
            logger.error("client secret-key validation failed,id:{},key:{}",statId,secretKey);
            return null;
        }
        return RPCClientProxy.instance().dataDurationQuery(statId,dimensValue,startTime,endTime);
    }

    public static Map<String,List<StatValue>> dataQueryWithDimensList(int statId, String secretKey, List<String> dimensValueList, List<Long> batchList) throws Exception {
        StatVerifyEntity statVerifyEntity = AuxHandler.queryStatInfo(statId);
        if(statVerifyEntity == null){
            logger.error("statistic({}) not exist!",statId);
            return null;
        }
        String md5 = AuxHandler.cacheGetMd5(secretKey);
        if(!statVerifyEntity.getVerifyKey().equals(md5)){
            logger.error("client secret-key validation failed,id:{},key:{}",statId,secretKey);
            return null;
        }
        return RPCClientProxy.instance().dataQueryWithDimensList(statId,dimensValueList,batchList);
    }

    public static Map<String,List<StatValue>> dataQueryWithDimensList(int statId, String secretKey, List<String> dimensValueList, long startTime,long endTime) throws Exception {
        StatVerifyEntity statVerifyEntity = AuxHandler.queryStatInfo(statId);
        if(statVerifyEntity == null){
            logger.error("statistic({}) not exist!",statId);
            return null;
        }
        String md5 = AuxHandler.cacheGetMd5(secretKey);
        if(!statVerifyEntity.getVerifyKey().equals(md5)){
            logger.error("client secret-key validation failed,id:{},key:{}",statId,secretKey);
            return null;
        }
        return RPCClientProxy.instance().dataDurationQueryWithDimensList(statId,dimensValueList,startTime,endTime);
    }


    public static List<LimitValue> limitQuery(int statId, String secretKey, Long batchTime) throws Exception {
        StatVerifyEntity statVerifyEntity = AuxHandler.queryStatInfo(statId);
        if(statVerifyEntity == null){
            logger.error("statistic({}) not exist!",statId);
            return null;
        }
        String md5 = AuxHandler.cacheGetMd5(secretKey);
        if(!statVerifyEntity.getVerifyKey().equals(md5)){
            logger.error("client secret-key validation failed,id:{},key:{}",statId,secretKey);
            return null;
        }
        return RPCClientProxy.instance().limitQuery(statId,batchTime);
    }

    protected static RunningMode getRunningMode() {
        return _runningMode;
    }

    public static void stop() {
        _LightSwitch.set(false);
    }
}
