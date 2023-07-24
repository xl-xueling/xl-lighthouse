package com.dtstep.lighthouse.core.storage.engine.impl;
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
import com.dtstep.lighthouse.core.batch.BatchAdapter;
import com.dtstep.lighthouse.core.expression.embed.AviatorHandler;
import com.dtstep.lighthouse.core.hbase.HBaseClient;
import com.dtstep.lighthouse.core.wrapper.MetaTableWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.dtstep.lighthouse.common.constant.StatConst;
import com.dtstep.lighthouse.common.entity.calculate.MicroBucket;
import com.dtstep.lighthouse.common.entity.meta.MetaTableEntity;
import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.entity.state.StatState;
import com.dtstep.lighthouse.common.entity.view.StatValue;
import com.dtstep.lighthouse.common.entity.view.StateValue;
import com.dtstep.lighthouse.common.exception.TableNotExistException;
import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.common.util.StringUtil;
import com.dtstep.lighthouse.core.storage.engine.ResultStorageEngine;
import org.apache.hadoop.hbase.CompareOperator;
import org.javatuples.Quartet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

public class HBaseResultStorageEngine extends ResultStorageEngine<MicroBucket, StatValue> {

    private static final Logger logger = LoggerFactory.getLogger(HBaseResultStorageEngine.class);

    @Override
    public void maxPut(List<MicroBucket> eventList) throws Exception {
        Map<String,List<MicroBucket>> putMap = eventList.stream().collect(Collectors.groupingBy(MicroBucket::getMetaName));
        for (String metaName : putMap.keySet()) {
            List<MicroBucket> events = putMap.get(metaName);
            Map<String, List<MicroBucket>> subMap = events.stream().collect(Collectors.groupingBy(x -> x.getRowKey() + "_" + x.getColumn()));
            List<Quartet<String, String, Long, Long>> batchList = Lists.newArrayList();
            for (String key : subMap.keySet()) {
                List<MicroBucket> subList = subMap.get(key);
                MicroBucket bucket = subList.get(0);
                String rowKey = bucket.getRowKey();
                String column = bucket.getColumn();
                long ttl = bucket.getTTL();
                long value = subList.stream().map(MicroBucket::getValue).mapToLong(x -> x).max().getAsLong();
                if(logger.isTraceEnabled()){
                    logger.trace("lighthouse trace,batch put,statId:{},batchTime:{},meta:{},rowKey:{},dimens:{},column:{},functionIndex:{},value:{},ttl:{}",
                            bucket.getStatId(), DateUtil.formatTimeStamp(bucket.getBatchTime(),"yyyy-MM-dd HH:mm:ss"),bucket.getMetaName(),bucket.getRowKey(),bucket.getDimensValue(),bucket.getColumn(),bucket.getFunctionIndex(),value,bucket.getTTL());
                }
                Quartet<String,String,Long,Long> quartet = Quartet.with(rowKey, column, value, ttl);
                batchList.add(quartet);
            }
            try {
                HBaseClient.batchPut(metaName, CompareOperator.GREATER, batchList);
            } catch (Exception ex) {
                logger.error("data put exception!", ex);
            }
        }
    }

    @Override
    public void minPut(List<MicroBucket> eventList) throws Exception {
        Map<String,List<MicroBucket>> putMap = eventList.stream().collect(Collectors.groupingBy(MicroBucket::getMetaName));
        for (String metaName : putMap.keySet()) {
            List<MicroBucket> events = putMap.get(metaName);
            Map<String, List<MicroBucket>> subMap = events.stream().collect(Collectors.groupingBy(x -> x.getRowKey() + "_" + x.getColumn()));
            List<Quartet<String, String, Long, Long>> batchList = Lists.newArrayList();
            for (String key : subMap.keySet()) {
                List<MicroBucket> subList = subMap.get(key);
                MicroBucket bucket = subList.get(0);
                String rowKey = bucket.getRowKey();
                String column = bucket.getColumn();
                long ttl = bucket.getTTL();
                long value = subList.stream().map(MicroBucket::getValue).mapToLong(x -> x).min().getAsLong();
                if(logger.isTraceEnabled()){
                    logger.trace("lighthouse trace,batch put,statId:{},batchTime:{},meta:{},rowKey:{},dimens:{},column:{},functionIndex:{},value:{},ttl:{}",
                            bucket.getStatId(),DateUtil.formatTimeStamp(bucket.getBatchTime(),"yyyy-MM-dd HH:mm:ss"),bucket.getMetaName(),bucket.getRowKey(),bucket.getDimensValue(),bucket.getColumn(),bucket.getFunctionIndex(),value,bucket.getTTL());
                }
                Quartet<String,String,Long,Long> quartet = Quartet.with(rowKey, column, value, ttl);
                batchList.add(quartet);
            }
            try {
                HBaseClient.batchPut(metaName, CompareOperator.LESS, batchList);
            } catch (Exception ex) {
                logger.error("data put exception!", ex);
            }
        }
    }

    @Override
    public void increment(List<MicroBucket> eventList) throws Exception {
        Map<String,List<MicroBucket>> resultMap = eventList.stream().collect(Collectors.groupingBy(MicroBucket::getMetaName));
        for (String metaName : resultMap.keySet()) {
            List<MicroBucket> list = resultMap.get(metaName);
            Map<String, List<MicroBucket>> subMap = list.stream().collect(Collectors.groupingBy(x -> x.getRowKey() + "_" + x.getColumn()));
            List<Quartet<String, String, Long, Long>> batchList = Lists.newArrayList();
            for (String key : subMap.keySet()) {
                List<MicroBucket> subList = subMap.get(key);
                MicroBucket bucket = subList.get(0);
                String rowKey = bucket.getRowKey();
                String column = bucket.getColumn();
                long ttl = bucket.getTTL();
                long value = subList.stream().map(MicroBucket::getValue).mapToLong(x -> x).sum();
                if(logger.isTraceEnabled()){
                    logger.trace("lighthouse trace,batch increment,statId:{},batchTime:{},meta:{},rowKey:{},dimens:{},column:{},functionIndex:{},value:{},ttl:{}",
                            bucket.getStatId(), DateUtil.formatTimeStamp(bucket.getBatchTime(),"yyyy-MM-dd HH:mm:ss"),bucket.getMetaName(),bucket.getRowKey(),bucket.getDimensValue(),bucket.getColumn(),bucket.getFunctionIndex(),value,bucket.getTTL());
                }
                Quartet<String,String,Long,Long> quartet = Quartet.with(rowKey, column, value, ttl);
                batchList.add(quartet);
            }
            try {
                HBaseClient.batchIncrement(metaName, batchList);
            } catch (Exception ex) {
                logger.error("data increment exception!", ex);
            }
        }
    }

    @Override
    public void put(List<MicroBucket> eventList) throws Exception {
        Map<String,List<MicroBucket>> putMap = eventList.stream().collect(Collectors.groupingBy(MicroBucket::getMetaName));
        for (String metaName : putMap.keySet()) {
            List<MicroBucket> events = putMap.get(metaName);
            Map<String, List<MicroBucket>> tempMap = events.stream().collect(Collectors.groupingBy(x -> x.getRowKey() + "_" + x.getColumn()));
            List<Quartet<String, String, Object, Long>> batchList = Lists.newArrayList();
            for (String key : tempMap.keySet()) {
                List<MicroBucket> subList = tempMap.get(key);
                MicroBucket bucket = subList.get(0);
                String rowKey = bucket.getRowKey();
                String column = bucket.getColumn();
                long ttl = bucket.getTTL();
                long value = subList.get(0).getValue();
                if(logger.isTraceEnabled()){
                    logger.trace("lighthouse trace,batch put,statId:{},batchTime:{},meta:{},rowKey:{},dimens:{},column:{},functionIndex:{},value:{},ttl:{}",
                            bucket.getStatId(),DateUtil.formatTimeStamp(bucket.getBatchTime(),"yyyy-MM-dd HH:mm:ss"),bucket.getMetaName(),bucket.getRowKey(),bucket.getDimensValue(),bucket.getColumn(),bucket.getFunctionIndex(),value,bucket.getTTL());
                }
                Quartet<String,String,Object,Long> quartet = Quartet.with(rowKey, column, value, ttl);
                batchList.add(quartet);
            }
            try {
                HBaseClient.batchPut(metaName,batchList);
            } catch (Exception ex) {
                logger.error("data put exception!", ex);
            }
        }
    }

    @Override
    public List<StatValue> queryWithDimensList(StatExtEntity statExtEntity, List<String> dimensValueList, List<Long> batchTimeList) throws Exception {
        List<StatValue> list = Lists.newArrayList();
        List<StatState> statStates = statExtEntity.getTemplateEntity().getStatStateList();
        int resMeta = statExtEntity.getResMeta();
        String metaName;
        if(statExtEntity.isBuiltIn()){
            metaName = StatConst.SYSTEM_STAT_RESULT_TABLE;
        }else{
            MetaTableEntity metaTableEntity = MetaTableWrapper.queryById(resMeta);
            metaName = metaTableEntity.getMetaName();
        }
        List<String> aggregateKeyList = Lists.newArrayList();
        for (long batchTime : batchTimeList) {
            for(String dimensValue : dimensValueList) {
                for (StatState statState : statStates) {
                    String aggregateKey = BatchAdapter.generateBatchKey(statExtEntity, statState.getFunctionIndex(), dimensValue, batchTime);
                    aggregateKeyList.add(aggregateKey);
                }
            }
        }
        HashMap<String, StateValue> result = HBaseClient.multiGetByAggregateKey(metaName,aggregateKeyList, statExtEntity.isSequence());
        for (long batchTime : batchTimeList) {
            for(String dimensValue : dimensValueList){
                StatValue statValue;
                try{
                    statValue = statFormulaTransform(statExtEntity, dimensValue, result, batchTime);
                    list.add(statValue);
                }catch (Exception ex){
                    logger.error("data query calculate error!",ex);
                }
            }
        }
        return list;
    }


    private StatValue statFormulaTransform(StatExtEntity statExtEntity, String dimensValue, HashMap<String, StateValue> result, long batchTime) {
        boolean invalidFlag = false;
        String formula = statExtEntity.getTemplateEntity().getCompleteStat();
        StatValue statValue = new StatValue();
        statValue.setBatchTime(batchTime);
        statValue.setDimens(dimensValue);
        statValue.setDisplayName(DateUtil.formatTimeStamp(batchTime, "yyyy-MM-dd HH:mm:ss"));
        long lastUpdateTime = 0;
        HashMap<String,Object> envMap = new HashMap<>();
        int variableIndex = 97;
        List<StatState> statStates = statExtEntity.getTemplateEntity().getStatStateList();
        List<Object> statesValue = new ArrayList<>();
        for (StatState statState : statStates) {
            String stateBody = statState.getStateBody();
            String aggregateKey = BatchAdapter.generateBatchKey(statExtEntity, statState.getFunctionIndex(), dimensValue, batchTime);
            StateValue stateValue = (result == null || result.get(aggregateKey) == null ? null:result.get(aggregateKey));
            if (stateValue == null) {
                invalidFlag = true;
                statesValue.add(0d);
            } else {
                BigDecimal value = StatState.isCountState(statState) || StatState.isBitCountState(statState)
                        ? BigDecimal.valueOf(stateValue.getValue()) : BigDecimal.valueOf(stateValue.getValue()).divide(BigDecimal.valueOf(1000D),3, RoundingMode.HALF_UP);
                String replaceId = String.valueOf((char)variableIndex);
                variableIndex++;
                envMap.put(replaceId,value);
                statesValue.add(value.toString());
                formula = formula.replace(stateBody, replaceId);
                if(lastUpdateTime  < stateValue.getLastUpdateTime()){
                    lastUpdateTime = stateValue.getLastUpdateTime();
                }
            }
        }
        statValue.setStatesValue(statesValue);
        if (!invalidFlag) {
            Object object = AviatorHandler.execute(formula,envMap);
            if (object != null) {
                if(object.getClass() == BigDecimal.class){
                    BigDecimal bigDecimal = (BigDecimal) object;
                    statValue.setValue(bigDecimal.toString());
                }else {
                    statValue.setValue(StringUtil.displayFormat(new BigDecimal(object.toString()).doubleValue()));
                }
            }
        }
        statValue.setLastUpdateTime(lastUpdateTime);
        return statValue;
    }

    @Override
    public List<StatValue> queryWithDimens(StatExtEntity statExtEntity, String dimensValue, List<Long> batchTimeList) throws Exception {
        List<StatState> statStates = statExtEntity.getTemplateEntity().getStatStateList();
        int resMeta = statExtEntity.getResMeta();
        String metaName;
        if(statExtEntity.isBuiltIn()){
            metaName = StatConst.SYSTEM_STAT_RESULT_TABLE;
        }else{
            MetaTableEntity metaTableEntity = MetaTableWrapper.queryById(resMeta);
            if(metaTableEntity == null){
                logger.error("queryWithDimens error,statId:[{}],meta table[{}] not exit!",statExtEntity.getId(),resMeta);
                throw new TableNotExistException(String.format("meta table[%s] not exit!",resMeta));
            }
            metaName = metaTableEntity.getMetaName();
        }
        List<String> aggregateKeyList = Lists.newArrayList();
        for (long batchTime : batchTimeList) {
            for (StatState statState : statStates) {
                String aggregateKey = BatchAdapter.generateBatchKey(statExtEntity, statState.getFunctionIndex(), dimensValue, batchTime);
                aggregateKeyList.add(aggregateKey);
            }
        }
        HashMap<String, StateValue> result = HBaseClient.multiGetByAggregateKey(metaName,aggregateKeyList, statExtEntity.isSequence());
        List<StatValue> list = Lists.newArrayList();
        for (long batchTime : batchTimeList) {
            StatValue statValue;
            try{
                statValue = statFormulaTransform(statExtEntity, dimensValue, result, batchTime);
                list.add(statValue);
            }catch (Exception ex){
                logger.error("data query calculate error!",ex);
            }
        }
        return list;
    }

    @Override
    public StatValue queryWithDimens(StatExtEntity statExtEntity, String dimensValue, long batchTime) throws Exception{
        return queryWithDimens(statExtEntity, dimensValue,Lists.newArrayList(batchTime)).get(0);
    }

    @Override
    public LinkedHashMap<String,StatValue> queryWithDimensList(StatExtEntity statExtEntity, List<String> dimensValueList, long batchTime) throws Exception {
        LinkedHashMap<String,StatValue> map = Maps.newLinkedHashMap();
        List<StatState> statStates = statExtEntity.getTemplateEntity().getStatStateList();
        int resMeta = statExtEntity.getResMeta();
        String metaName;
        if(statExtEntity.isBuiltIn()){
            metaName = StatConst.SYSTEM_STAT_RESULT_TABLE;
        }else{
            MetaTableEntity metaTableEntity = MetaTableWrapper.queryById(resMeta);
            metaName = metaTableEntity.getMetaName();
        }
        List<String> aggregateKeyList = Lists.newArrayList();
        for (String dimensValue : dimensValueList) {
            for (StatState statState : statStates) {
                String aggregateKey = BatchAdapter.generateBatchKey(statExtEntity, statState.getFunctionIndex(), dimensValue, batchTime);
                aggregateKeyList.add(aggregateKey);
            }
        }
        HashMap<String,StateValue> result = HBaseClient.multiGetByAggregateKey(metaName,aggregateKeyList, statExtEntity.isSequence());
        for (String dimensValue : dimensValueList) {
            StatValue statValue;
            try{
                statValue = statFormulaTransform(statExtEntity, dimensValue, result, batchTime);
                map.put(dimensValue,statValue);
            }catch (Exception ex){
                logger.error("calculate error",ex);
            }
        }
        return map;
    }

}
