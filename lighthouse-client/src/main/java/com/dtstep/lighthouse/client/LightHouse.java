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
import com.dtstep.lighthouse.common.constant.SysConst;
import com.dtstep.lighthouse.common.entity.event.SimpleSlotEvent;
import com.dtstep.lighthouse.common.entity.group.GroupVerifyEntity;
import com.dtstep.lighthouse.common.entity.monitor.ClusterInfo;
import com.dtstep.lighthouse.common.entity.stat.StatVerifyEntity;
import com.dtstep.lighthouse.common.entity.view.LimitValue;
import com.dtstep.lighthouse.common.entity.view.StatValue;
import com.dtstep.lighthouse.common.enums.RunningMode;
import com.dtstep.lighthouse.common.exception.StatisticNotFoundException;
import com.dtstep.lighthouse.common.fusing.FusingToken;
import com.dtstep.lighthouse.common.fusing.FusingSwitch;
import com.dtstep.lighthouse.common.enums.fusing.FusingRules;
import com.dtstep.lighthouse.common.exception.InitializationException;
import com.dtstep.lighthouse.common.exception.LightSendException;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.common.util.OkHttpUtil;
import com.dtstep.lighthouse.common.util.StringUtil;
import com.zeroc.Ice.NotRegisteredException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;


public final class LightHouse {

    private static final Logger logger = LoggerFactory.getLogger(LightHouse.class);

    private static int consumerFrequency = 500;

    private static int consumerBatchSize = 100;

    private static final EventPool<SimpleSlotEvent> eventPool = new BlockingEventPool<>("ClientEventPool",3,200000);

    private static final Producer _producer = new Producer(eventPool);

    static final AtomicBoolean _InitFlag = new AtomicBoolean(false);

    private static final AtomicBoolean _LightSwitch = new AtomicBoolean(true);

    private static final String KEY_PROCESS_FREQUENCY = "lighthouse_process_frequency";

    private static final String KEY_PROCESS_BATCH = "lighthouse_process_batch";

    private static RunningMode _runningMode = RunningMode.CLUSTER;

    private LightHouse(){}

    public static boolean isInit() throws Exception {
        return _InitFlag.get();
    }

    public static synchronized void init(final String locators) throws Exception {
        if(!_InitFlag.get()){
            ClusterInfo clusterInfo = getRunningMode(locators);
            if(clusterInfo == null){
                throw new InitializationException(String.format("lighthouse remote service not available,locators:%s",locators));
            }
            _runningMode = clusterInfo.getRunningMode();
            boolean result = RPCClientProxy.instance().init(locators);
            if(!result){
                throw new InitializationException(String.format("lighthouse remote service not available,locators:%s",locators));
            }
            _InitFlag.set(true);
            Consumer consumer = new Consumer(eventPool, consumerFrequency, consumerBatchSize);
            consumer.start();
        }
    }

    public static void init(final String locators,Properties properties) throws Exception{
        if(properties.containsKey(KEY_PROCESS_FREQUENCY)){
            consumerFrequency = Integer.parseInt(properties.getProperty(KEY_PROCESS_FREQUENCY));
        }
        if(properties.containsKey(KEY_PROCESS_BATCH)){
            consumerBatchSize = Integer.parseInt(properties.getProperty(KEY_PROCESS_BATCH));
        }
        init(locators);
    }

    private static ClusterInfo getRunningMode(String locators) {
        String[] locatorArr = locators.split(",");
        ClusterInfo clusterInfo = null;
        for (String conf : locatorArr) {
            String[] arr = conf.split(":");
            String ip = arr[0];
            try{
                String response = OkHttpUtil.post(String.format("http://%s:%s/clusterInfo)", ip, SysConst.CLUSTER_HTTP_SERVICE_PORT),"");
                if(StringUtil.isNotEmpty(response)){
                    clusterInfo = JsonUtil.toJavaObject(response,ClusterInfo.class);
                    if(clusterInfo != null){
                        break;
                    }
                }
            }catch (Exception ex){
                logger.error("Request remote service failed,ip:{},port:{}",ip,SysConst.CLUSTER_HTTP_SERVICE_PORT);
            }
        }
        return clusterInfo;
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
        if(repeat <= 0){
            throw new IllegalArgumentException("Parameter[repeat <= 0] verification failed!");
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
        if(!_InitFlag.get()){
            throw new InitializationException("connection is not initialized or the connection is abnormal, request ignored!");
        }
        return AuxHandler.queryGroupInfo(token);
    }

    public static StatVerifyEntity queryStatInfo(int statId) throws Exception {
        if(!_InitFlag.get()){
            throw new InitializationException("connection is not initialized or the connection is abnormal, request ignored!");
        }
        return AuxHandler.queryStatInfo(statId);
    }

    public static List<StatValue> dataQuery(String callerName,String callerKey,int statId, String dimensValue, List<Long> batchList) throws Exception {
        if(!_InitFlag.get()){
            throw new InitializationException("connection is not initialized or the connection is abnormal, request ignored!");
        }
        StatVerifyEntity statVerifyEntity = AuxHandler.queryStatInfo(statId);
        if(statVerifyEntity == null){
            logger.error("statistic({}) not exist!",statId);
            throw new StatisticNotFoundException("statistic not found,id:" + statId);
        }
        return RPCClientProxy.instance().dataQueryV2(callerName,callerKey,statId,dimensValue,batchList);
    }

    public static List<StatValue> dataDurationQuery(String callerName,String callerKey,int statId, String dimensValue, long startTime, long endTime) throws Exception {
        if(!_InitFlag.get()){
            throw new InitializationException("connection is not initialized or the connection is abnormal, request ignored!");
        }
        if(startTime >= endTime){
            throw new IllegalArgumentException("Parameter[startTime >= endTime] verification failed!");
        }
        StatVerifyEntity statVerifyEntity = AuxHandler.queryStatInfo(statId);
        if(statVerifyEntity == null){
            logger.error("statistic({}) not exist!",statId);
            throw new StatisticNotFoundException("statistic not found,id:" + statId);
        }
        return RPCClientProxy.instance().dataDurationQueryV2(callerName,callerKey,statId,dimensValue,startTime,endTime);
    }

    public static Map<String,List<StatValue>> dataQueryWithDimensList(String callerName, String callerKey,int statId, List<String> dimensValueList, List<Long> batchList) throws Exception {
        if(!_InitFlag.get()){
            throw new InitializationException("connection is not initialized or the connection is abnormal, request ignored!");
        }
        StatVerifyEntity statVerifyEntity = AuxHandler.queryStatInfo(statId);
        if(statVerifyEntity == null){
            logger.error("statistic({}) not exist!",statId);
            throw new StatisticNotFoundException("statistic not found,id:" + statId);
        }
        return RPCClientProxy.instance().dataQueryWithDimensListV2(callerName,callerKey,statId,dimensValueList,batchList);
    }

    public static Map<String,List<StatValue>> dataDurationQueryWithDimensList(String callerName,String callerKey,int statId, List<String> dimensValueList, long startTime,long endTime) throws Exception {
        if(!_InitFlag.get()){
            throw new InitializationException("connection is not initialized or the connection is abnormal, request ignored!");
        }
        if(startTime >= endTime){
            throw new IllegalArgumentException("Parameter[startTime >= endTime] verification failed!");
        }
        StatVerifyEntity statVerifyEntity = AuxHandler.queryStatInfo(statId);
        if(statVerifyEntity == null){
            logger.error("statistic({}) not exist!",statId);
            throw new StatisticNotFoundException("statistic not found,id:" + statId);
        }
        return RPCClientProxy.instance().dataDurationQueryWithDimensListV2(callerName,callerKey,statId,dimensValueList,startTime,endTime);
    }

    public static List<LimitValue> limitQuery(String callerName,String callerKey,int statId, Long batchTime) throws Exception {
        if(!_InitFlag.get()){
            throw new InitializationException("connection is not initialized or the connection is abnormal, request ignored!");
        }
        StatVerifyEntity statVerifyEntity = AuxHandler.queryStatInfo(statId);
        if(statVerifyEntity == null){
            logger.error("statistic({}) not exist!",statId);
            throw new StatisticNotFoundException("statistic not found,id:" + statId);
        }
        return RPCClientProxy.instance().limitQueryV2(callerName,callerKey,statId,batchTime);
    }

    protected static RunningMode getRunningMode() {
        return _runningMode;
    }

    public static void stop() {
        _LightSwitch.set(false);
    }
}
