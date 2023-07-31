package com.dtstep.lighthouse.client;
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
import Ice.Communicator;
import com.dtstep.lighthouse.common.aggregator.BlockingEventPool;
import com.dtstep.lighthouse.common.aggregator.EventPool;
import com.dtstep.lighthouse.common.entity.event.SimpleSlotEvent;
import com.dtstep.lighthouse.common.fusing.FusingToken;
import com.dtstep.lighthouse.common.fusing.FusingSwitch;
import com.dtstep.lighthouse.common.entity.view.StatValue;
import com.dtstep.lighthouse.common.enums.fusing.FusingRules;
import com.dtstep.lighthouse.common.exception.InitializationException;
import com.dtstep.lighthouse.common.exception.LightSendException;
import com.dtstep.lighthouse.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;


public final class LightHouse {

    private static final Logger logger = LoggerFactory.getLogger(LightHouse.class);

    private static int consumerFrequency = 500;

    private static int consumerBatchSize = 200;

    private static Producer _producer;

    private static Consumer _consumer;

    private static final EventPool<SimpleSlotEvent> eventPool = new BlockingEventPool<>("ClientEventPool",3,200000);

    private static DataQueryHandler _dataQueryHandler;

    static final AtomicBoolean _InitFlag = new AtomicBoolean(false);

    private static final AtomicBoolean _LightSwitch = new AtomicBoolean(true);

    static volatile int _limiterTimeOut = 5000;

    private static final String KEY_PROCESS_FREQUENCY = "process_frequency";

    private static final String KEY_BATCH_SIZE = "batch_size";

    private static final String KEY_TIMEOUT = "timeout";

    private static final List<Communicator> communicatorList = new ArrayList<>();

    private static String[] initParams;

    static {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleWithFixedDelay(new ReinitializeThread(),0,5, TimeUnit.MINUTES);
    }

    private LightHouse(){}

    public static void init(final String locators) {
        if (_InitFlag.get()) {
            return;
        }
        if(StringUtil.isEmpty(locators)){
            throw new InitializationException("lighthouse client init failed,locators cannot be empty!");
        }
        StringBuilder locatorSbr = new StringBuilder();
        try {
            String[] locatorArr = locators.split(",");
            for (String conf : locatorArr) {
                String[] arr = conf.split(":");
                String ip = arr[0];
                String port = arr[1];
                locatorSbr.append(":").append("tcp -h ").append(ip).append(" -p ").append(port);
            }
        }catch (Exception ex){
            throw new InitializationException("lighthouse client init failed,locators format error!");
        }
        try {
            String cfg = String.format("--Ice.Default.Locator=LightHouseIceGrid/Locator %s -z", locatorSbr.toString());
            initParams = new String[]{cfg};
            doInit(initParams);
        }catch (Exception ex){
            throw new InitializationException(String.format("lighthouse remote service not available,locators:%s",locators));
        }
    }

    private synchronized static void doInit(String[] initParams){
        if (_InitFlag.get()) {
            return;
        }
        FusingToken fusingToken = FusingSwitch.entry(FusingRules.CLIENT_MULTI_EXECUTE_RULE);
        if(fusingToken == null){
            logger.debug("lighthouse init ignored,the number of executions exceeds the threshold, the call is blocked!");
            return;
        }
        FusingSwitch.track(fusingToken);
        clearIceResource();
        Ice.Properties iceProperties = Ice.Util.createProperties();
        initProperties(iceProperties);
        Ice.InitializationData initData = new Ice.InitializationData();
        initData.properties = iceProperties;
        Ice.Communicator ic = Ice.Util.initialize(initParams, initData);
        communicatorList.add(ic);
        _producer = new Producer(ic, eventPool);
        _consumer = new Consumer(ic, eventPool, consumerFrequency, consumerBatchSize);
        _dataQueryHandler = new DataQueryHandler(ic);
        _consumer.start();
        _InitFlag.set(true);
        logger.info("lighthouse client init success!");
    }

    private static void initProperties(Ice.Properties iceProperties) {
        iceProperties.setProperty("Ice.ThreadPool.Client.Size", "50");
        iceProperties.setProperty("Ice.ThreadPool.Client.SizeMax", "300");
        iceProperties.setProperty("Ice.MessageSizeMax", "1409600");
    }

    public static synchronized void config(final String key,final Object value) throws Exception{
        if(_InitFlag.get()){
            return;
        }
        if(StringUtil.isEmpty(key) || value == null){
            throw new InterruptedException("lighthouse client configuration failed,required parameters missing!");
        }
        if(KEY_PROCESS_FREQUENCY.equalsIgnoreCase(key)){
            consumerFrequency = Integer.parseInt(String.valueOf(value));
        }else if(KEY_BATCH_SIZE.equalsIgnoreCase(key)){
            consumerBatchSize = Integer.parseInt(String.valueOf(value));
        }else if(KEY_TIMEOUT.equals(key)){
            _limiterTimeOut = Integer.parseInt(String.valueOf(value));
        }
    }

    public static void stat(final String token,final String secretKey,Map<String,Object> paramMap,final long timestamp) throws Exception{
        stat(token,secretKey,paramMap,1,timestamp);
    }

    public static void syncStat(final String token,final String secretKey,Map<String,Object> paramMap,final long timestamp) throws Exception{
        syncStat(token,secretKey,paramMap,1,timestamp);
    }

    public static void syncStat(final String token, final String secretKey, Map<String,Object> paramMap, final int repeat, final long timestamp) throws Exception{
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
            _producer.send(token,secretKey,paramMap,repeat,timestamp,true);
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
            _producer.send(token,secretKey,paramMap,repeat,timestamp,false);
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



    public static List<StatValue> dataQuery(int statId,String secretKey,long startTime,long endTime) throws Exception{
        return dataQueryWithDimens(statId,secretKey,null,startTime,endTime);
    }

    public static List<StatValue> dataQueryWithDimens(int statId,String secretKey,String dimens,long startTime,long endTime) throws Exception{
        if(!_LightSwitch.get()){
            return null;
        }
        if(StringUtil.isEmpty(secretKey) || startTime == 0L || endTime == 0L){
            throw new IllegalArgumentException("lighthouse failed to send message,required parameters missing!");
        }
        if(!_InitFlag.get()){
            logger.info("lighthouse send message error,not initialized or connection exception, request ignored!");
            return null;
        }

        if(checkDataQueryFusing()){
            return null;
        }
        FusingToken fusingToken = null;
        try{
            fusingToken = FusingSwitch.entry(FusingRules.CLIENT_EXCEPTION_RULE);
            if(fusingToken == null){
                logger.error("number of exceptions reaches the threshold, the call is blocked!");
                return null;
            }
            return _dataQueryHandler.dataQueryWithDimens(statId,secretKey,dimens,startTime,endTime);
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

    public static StatValue dataQuery(int statId, String secretKey,long batchTime) throws Exception{
        return dataQueryWithDimens(statId,secretKey,null,batchTime);
    }

    public static StatValue dataQueryWithDimens(int statId, String secretKey, String dimens, long batchTime) throws Exception{
        if(!_LightSwitch.get()){
            return null;
        }
        if(StringUtil.isEmpty(secretKey) || batchTime == 0L){
            throw new IllegalArgumentException("lighthouse failed to send message,required parameters missing!");
        }
        if(!_InitFlag.get()){
            logger.info("lighthouse send message error,not initialized or connection exception, request ignored!");
            return null;
        }
        if(checkDataQueryFusing()){
            return null;
        }
        FusingToken fusingToken = null;
        try{
            fusingToken = FusingSwitch.entry(FusingRules.CLIENT_EXCEPTION_RULE);
            if(fusingToken == null){
                logger.info("lighthouse send message error,the number of exceptions or timeouts reaches the threshold, the call is blocked!");
                return null;
            }
            return _dataQueryHandler.dataQueryWithDimens(statId,secretKey,dimens,batchTime);
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


    public static Map<String,StatValue> dataQueryWithDimensList(int statId, String secretKey, List<String> dimensList, long batchTime) throws Exception{
        if(!_LightSwitch.get()){
            return null;
        }
        if(StringUtil.isEmpty(secretKey) || batchTime == 0L || dimensList == null || dimensList.size() == 0){
            throw new IllegalArgumentException("lighthouse failed to send message,required parameters missing!");
        }
        if(!_InitFlag.get()){
            logger.info("lighthouse send message error,not initialized or connection exception, request ignored!");
            return null;
        }
        if(checkDataQueryFusing()){
            return null;
        }
        FusingToken fusingToken = null;
        try{
            fusingToken = FusingSwitch.entry(FusingRules.CLIENT_EXCEPTION_RULE);
            if(fusingToken == null){
                logger.error("lighthouse send message error,the number of exceptions or timeouts reaches the threshold, the call is blocked!");
                return null;
            }
            return _dataQueryHandler.dataQueryWithMultiDimens(statId,secretKey,dimensList,batchTime);
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

    public static long getBatchTime(String timeParam,long timestamp){
        return _dataQueryHandler.getBatchTime(timeParam,timestamp);
    }

    public static List<Long> getBatchTimeList(String timeParam,long startTime,long endTime) throws Exception{
        if(!_LightSwitch.get()){
            return null;
        }
        if(StringUtil.isEmpty(timeParam) || startTime == 0L || endTime == 0L){
            throw new IllegalArgumentException("lighthouse failed to get batch time list,required parameters missing!");
        }
        if(startTime >= endTime){
            return null;
        }else{
            return _dataQueryHandler.getBatchTime(timeParam,startTime,endTime,10000);
        }
    }

    private static void reinitialize() {
        if(!_InitFlag.get() && initParams != null){
            doInit(initParams);
            logger.info("lighthouse client reinitialize success!");
        }
    }

    public static void destroy() throws Exception{
        _consumer.destroy();
    }

    public static void stop(){
        _LightSwitch.set(false);
        _InitFlag.set(false);
        _consumer.shutdown();
        clearIceResource();
    }

    public static void restart() throws Exception{
        _consumer.shutdown();
        _InitFlag.set(false);
        if(initParams != null){
            doInit(initParams);
        }else{
            throw new InterruptedException("lighthouse client restart error,initialize configuration not found!");
        }
        _LightSwitch.set(true);
    }

    private static class ReinitializeThread implements Runnable{
        @Override
        public void run() {
            try{
                if(_LightSwitch.get()){
                    reinitialize();
                }
            }catch (Exception ex){
                logger.error("lighthouse client reinitialize error!",ex);
            }
        }
    }

    private static void clearIceResource() {
        if(!communicatorList.isEmpty()){
            Iterator<Communicator> iterator = communicatorList.iterator();
            while (iterator.hasNext()){
                Communicator communicator = iterator.next();
                try{
                    communicator.destroy();
                }catch (Exception ex){
                    ex.printStackTrace();
                }
                iterator.remove();
            }
        }
    }

    private static boolean checkDataQueryFusing(){
        FusingToken fusingToken = FusingSwitch.entry(FusingRules.CLIENT_DATA_QUERY_RULE);
        if(fusingToken == null){
            logger.error("lighthouse data query is ignored,the number of queries exceeds the maximum limit, the call is blocked!");
            return false;
        }
        FusingSwitch.track(fusingToken);
        return true;
    }

}
