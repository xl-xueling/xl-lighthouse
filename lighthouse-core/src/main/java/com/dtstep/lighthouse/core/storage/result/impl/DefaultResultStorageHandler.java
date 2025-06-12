package com.dtstep.lighthouse.core.storage.result.impl;
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
import com.dtstep.lighthouse.common.constant.StatConst;
import com.dtstep.lighthouse.common.entity.calculate.MicroBucket;
import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.entity.state.StatState;
import com.dtstep.lighthouse.common.entity.view.StatValue;
import com.dtstep.lighthouse.common.modal.MetaTable;
import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.common.util.StringUtil;
import com.dtstep.lighthouse.core.batch.BatchAdapter;
import com.dtstep.lighthouse.core.expression.embed.AviatorHandler;
import com.dtstep.lighthouse.core.rowkey.KeyGenerator;
import com.dtstep.lighthouse.core.rowkey.impl.DefaultKeyGenerator;
import com.dtstep.lighthouse.core.storage.common.*;
import com.dtstep.lighthouse.core.storage.warehouse.WarehouseStorageEngineProxy;
import com.dtstep.lighthouse.core.storage.result.ResultStorageHandler;
import com.dtstep.lighthouse.core.wrapper.MetaTableWrapper;
import com.dtstep.lighthouse.core.wrapper.StatDBWrapper;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

public class DefaultResultStorageHandler implements ResultStorageHandler<MicroBucket, StatValue> {

    private static final Logger logger = LoggerFactory.getLogger(DefaultResultStorageHandler.class);

    private static final KeyGenerator keyGenerator = new DefaultKeyGenerator();

    @Override
    public void increment(List<MicroBucket> bucketList) throws Exception {
        Map<String,List<MicroBucket>> resultMap = bucketList.stream().collect(Collectors.groupingBy(MicroBucket::getMetaName));
        for (String metaName : resultMap.keySet()) {
            List<MicroBucket> list = resultMap.get(metaName);
            Map<String, List<MicroBucket>> subMap = list.stream().collect(Collectors.groupingBy(x -> x.getRowKey() + "_" + x.getColumn()));
            List<LdpIncrement> ldpIncrements = Lists.newArrayList();
            for (String key : subMap.keySet()) {
                List<MicroBucket> subList = subMap.get(key);
                MicroBucket bucket = subList.get(0);
                String rowKey = bucket.getRowKey();
                String column = bucket.getColumn();
                long ttl = bucket.getTtl();
                long value = subList.stream().map(MicroBucket::getValue).mapToLong(x -> x).sum();
                if(logger.isDebugEnabled()){
                    logger.debug("lighthouse debug,batch increment,statId:{},batchTime:{},meta:{},rowKey:{},dimens:{},column:{},functionIndex:{},value:{},ttl:{}",
                            bucket.getStatId(), DateUtil.formatTimeStamp(bucket.getBatchTime(),"yyyy-MM-dd HH:mm:ss"),bucket.getMetaName(),bucket.getRowKey(),bucket.getDimensValue(),bucket.getColumn(),bucket.getFunctionIndex(),value,bucket.getTtl());
                }
                LdpIncrement ldpIncrement = LdpIncrement.with(rowKey,column,value,ttl);
                ldpIncrements.add(ldpIncrement);
            }
            try {
                WarehouseStorageEngineProxy.getInstance().increments(metaName,ldpIncrements);
            } catch (Exception ex) {
                logger.error("data increment exception!", ex);
            }
        }
    }

    @Override
    public void maxPut(List<MicroBucket> bucketList) throws Exception {
        Map<String,List<MicroBucket>> putMap = bucketList.stream().collect(Collectors.groupingBy(MicroBucket::getMetaName));
        for (String metaName : putMap.keySet()) {
            List<MicroBucket> events = putMap.get(metaName);
            Map<String, List<MicroBucket>> subMap = events.stream().collect(Collectors.groupingBy(x -> x.getRowKey() + "_" + x.getColumn()));
            List<LdpPut> puts = Lists.newArrayList();
            for (String key : subMap.keySet()) {
                List<MicroBucket> subList = subMap.get(key);
                MicroBucket bucket = subList.get(0);
                String rowKey = bucket.getRowKey();
                String column = bucket.getColumn();
                long ttl = bucket.getTtl();
                long value = subList.stream().map(MicroBucket::getValue).mapToLong(x -> x).max().getAsLong();
                if(logger.isDebugEnabled()){
                    logger.debug("lighthouse debug,batch maxPut,statId:{},batchTime:{},meta:{},rowKey:{},dimens:{},column:{},functionIndex:{},value:{},ttl:{}",
                            bucket.getStatId(), DateUtil.formatTimeStamp(bucket.getBatchTime(),"yyyy-MM-dd HH:mm:ss"),bucket.getMetaName(),bucket.getRowKey(),bucket.getDimensValue(),bucket.getColumn(),bucket.getFunctionIndex(),value,bucket.getTtl());
                }
                LdpPut ldpPut = LdpPut.with(rowKey,column,value,ttl);
                puts.add(ldpPut);
            }
            try {
                WarehouseStorageEngineProxy.getInstance().putsWithCompare(metaName, CompareOperator.GREATER,puts);
            } catch (Exception ex) {
                logger.error("data put exception!", ex);
            }
        }
    }

    @Override
    public void minPut(List<MicroBucket> bucketList) throws Exception {
        Map<String,List<MicroBucket>> putMap = bucketList.stream().collect(Collectors.groupingBy(MicroBucket::getMetaName));
        for (String metaName : putMap.keySet()) {
            List<MicroBucket> events = putMap.get(metaName);
            Map<String, List<MicroBucket>> subMap = events.stream().collect(Collectors.groupingBy(x -> x.getRowKey() + "_" + x.getColumn()));
            List<LdpPut> puts = Lists.newArrayList();
            for (String key : subMap.keySet()) {
                List<MicroBucket> subList = subMap.get(key);
                MicroBucket bucket = subList.get(0);
                String rowKey = bucket.getRowKey();
                String column = bucket.getColumn();
                long ttl = bucket.getTtl();
                long value = subList.stream().map(MicroBucket::getValue).mapToLong(x -> x).min().getAsLong();
                if(logger.isDebugEnabled()){
                    logger.debug("lighthouse debug,batch minPut,statId:{},batchTime:{},meta:{},rowKey:{},dimens:{},column:{},functionIndex:{},value:{},ttl:{}",
                            bucket.getStatId(), DateUtil.formatTimeStamp(bucket.getBatchTime(),"yyyy-MM-dd HH:mm:ss"),bucket.getMetaName(),bucket.getRowKey(),bucket.getDimensValue(),bucket.getColumn(),bucket.getFunctionIndex(),value,bucket.getTtl());
                }
                LdpPut ldpPut = LdpPut.with(rowKey,column,value,ttl);
                puts.add(ldpPut);
            }
            try {
                WarehouseStorageEngineProxy.getInstance().putsWithCompare(metaName, CompareOperator.LESSER,puts);
            } catch (Exception ex) {
                logger.error("data put exception!", ex);
            }
        }
    }

    @Override
    public void put(List<MicroBucket> bucketList) throws Exception {
        Map<String,List<MicroBucket>> putMap = bucketList.stream().collect(Collectors.groupingBy(MicroBucket::getMetaName));
        for (String metaName : putMap.keySet()) {
            List<MicroBucket> events = putMap.get(metaName);
            Map<String, List<MicroBucket>> tempMap = events.stream().collect(Collectors.groupingBy(x -> x.getRowKey() + "_" + x.getColumn()));
            List<LdpPut> ldpPuts = Lists.newArrayList();
            for (String key : tempMap.keySet()) {
                List<MicroBucket> subList = tempMap.get(key);
                MicroBucket bucket = subList.get(0);
                String rowKey = bucket.getRowKey();
                String column = bucket.getColumn();
                long ttl = bucket.getTtl();
                long value = subList.get(0).getValue();
                if(logger.isDebugEnabled()){
                    logger.debug("lighthouse debug,batch put,statId:{},batchTime:{},meta:{},rowKey:{},dimens:{},column:{},functionIndex:{},value:{},ttl:{}",
                            bucket.getStatId(),DateUtil.formatTimeStamp(bucket.getBatchTime(),"yyyy-MM-dd HH:mm:ss"),bucket.getMetaName(),bucket.getRowKey(),bucket.getDimensValue(),bucket.getColumn(),bucket.getFunctionIndex(),value,bucket.getTtl());
                }
                LdpPut ldpPut = LdpPut.with(rowKey,column,value,ttl);
                ldpPuts.add(ldpPut);
            }
            try {
                WarehouseStorageEngineProxy.getInstance().puts(metaName,ldpPuts);
            } catch (Exception ex) {
                logger.error("data put exception!", ex);
            }
        }
    }

    @Override
    public StatValue query(StatExtEntity statExtEntity, String dimensValue, long batchTime) throws Exception {
        if(StringUtil.isEmpty(dimensValue)){
            dimensValue = null;
        }
        Map<String,List<StatValue>> resultMap = queryWithDimensList(statExtEntity,dimensValue == null ? null:List.of(dimensValue),List.of(batchTime));
        return MapUtils.isEmpty(resultMap) || CollectionUtils.isEmpty(resultMap.get(dimensValue)) ? null : resultMap.get(dimensValue).get(0);
    }

    @Override
    public List<StatValue> query(StatExtEntity statExtEntity, String dimensValue, List<Long> batchTimeList) throws Exception {
        if(StringUtil.isEmpty(dimensValue)){
            dimensValue = null;
        }
        Validate.isTrue(CollectionUtils.isNotEmpty(batchTimeList));
        Map<String,List<StatValue>> resultMap = queryWithDimensList(statExtEntity,dimensValue == null ? null:List.of(dimensValue),batchTimeList);
        return MapUtils.isEmpty(resultMap) || CollectionUtils.isEmpty(resultMap.get(dimensValue)) ? null : resultMap.get(dimensValue);
    }

    @Override
    public LinkedHashMap<String, StatValue> queryWithDimensList(StatExtEntity statExtEntity, List<String> dimensValueList, long batchTime) throws Exception {
        LinkedHashMap<String,List<StatValue>> dbMap = queryWithDimensList(statExtEntity,dimensValueList,List.of(batchTime));
        LinkedHashMap<String,StatValue> resultMap = new LinkedHashMap<>();
        for(String key : dbMap.keySet()){
            resultMap.put(key,dbMap.get(key).get(0));
        }
        return resultMap;
    }

    @Override
    public LinkedHashMap<String,List<StatValue>> queryWithDimensList(StatExtEntity statExtEntity, List<String> dimensValueList, List<Long> batchTimeList) throws Exception {
        List<StatState> statStates = statExtEntity.getTemplateEntity().getStatStateList();
        int resMeta = statExtEntity.getMetaId();
        String metaName;
        if(statExtEntity.isBuiltIn()){
            metaName = StatConst.SYSTEM_STAT_RESULT_TABLE;
        }else{
            MetaTable metaTable = MetaTableWrapper.queryById(resMeta);
            metaName = metaTable.getMetaName();
        }
        if(dimensValueList != null){
            Validate.isTrue(dimensValueList.size() <= StatConst.QUERY_RESULT_LIMIT_SIZE);
        }
        List<LdpGet> getList = new ArrayList<>();
        for (long batchTime : batchTimeList) {
            if(dimensValueList == null){
                for (StatState statState : statStates) {
                    String aggregateKey = keyGenerator.resultKey(statExtEntity,statState.getFunctionIndex(),null,batchTime);
                    String [] keyArr = aggregateKey.split(";");
                    String key = keyArr[0];
                    String column = keyArr[1];
                    LdpGet ldpGet = LdpGet.with(key,column);
                    getList.add(ldpGet);
                }
            }else{
                for(String dimensValue : dimensValueList) {
                    for (StatState statState : statStates) {
                        String aggregateKey = keyGenerator.resultKey(statExtEntity,statState.getFunctionIndex(),dimensValue,batchTime);
                        String [] keyArr = aggregateKey.split(";");
                        String key = keyArr[0];
                        String column = keyArr[1];
                        LdpGet ldpGet = LdpGet.with(key,column);
                        getList.add(ldpGet);
                    }
                }
            }
            Validate.isTrue(getList.size() <= StatConst.QUERY_RESULT_LIMIT_SIZE);
        }
        List<LdpResult<Long>> results = WarehouseStorageEngineProxy.getInstance().gets(metaName,getList,Long.class);
        Map<String,LdpResult<Long>> dbResultMap = results.stream().filter(x -> x.getData() != null).collect(Collectors.toMap(x -> x.getKey() + ";" + x.getColumn(), x -> x));
        LinkedHashMap<String,List<StatValue>> resultMap = new LinkedHashMap<>();
        if(dimensValueList == null){
            List<StatValue> valueList = new ArrayList<>();
            for(long batchTime : batchTimeList){
                StatValue statValue = calculate(statExtEntity,null,batchTime,dbResultMap);
                valueList.add(statValue);
            }
            resultMap.put(null,valueList);
        }else{
            for(String dimensValue : dimensValueList){
                List<StatValue> valueList = new ArrayList<>();
                for(long batchTime : batchTimeList){
                    StatValue statValue = calculate(statExtEntity,dimensValue,batchTime,dbResultMap);
                    valueList.add(statValue);
                }
                resultMap.put(dimensValue,valueList);
            }
        }
        return resultMap;
    }

    public LinkedHashMap<String,List<StatValue>> queryWithDimensList0(StatExtEntity statExtEntity, List<String> dimensValueList, List<Long> batchTimeList) throws Exception {
        List<StatState> statStates = statExtEntity.getTemplateEntity().getStatStateList();
        int resMeta = statExtEntity.getMetaId();
        String metaName;
        if(statExtEntity.isBuiltIn()){
            metaName = StatConst.SYSTEM_STAT_RESULT_TABLE;
        }else{
            MetaTable metaTable = MetaTableWrapper.queryById(resMeta);
            metaName = metaTable.getMetaName();
        }
        if(dimensValueList != null){
            Validate.isTrue(dimensValueList.size() <= StatConst.QUERY_RESULT_LIMIT_SIZE);
        }
        List<LdpGet> getList = new ArrayList<>();
        List<LdpGet> compatibleGetList = new ArrayList<>();
        HashMap<String,String> compatibleKeyMap = new HashMap<>();
        for (long batchTime : batchTimeList) {
            if(dimensValueList == null){
                for (StatState statState : statStates) {
                    String aggregateKey = keyGenerator.resultKey(statExtEntity,statState.getFunctionIndex(),null,batchTime);
                    String [] keyArr = aggregateKey.split(";");
                    String key = keyArr[0];
                    String column = keyArr[1];
                    String compatibleKey = getCompatibleKey(key);
                    if(StringUtil.isNotEmpty(compatibleKey)){
                        LdpGet ldpGet = LdpGet.with(compatibleKey,column);
                        compatibleGetList.add(ldpGet);
                        compatibleKeyMap.put(compatibleKey,key);
                    }
                    LdpGet ldpGet = LdpGet.with(key,column);
                    getList.add(ldpGet);
                }
            }else{
                for(String dimensValue : dimensValueList) {
                    for (StatState statState : statStates) {
                        String aggregateKey = keyGenerator.resultKey(statExtEntity,statState.getFunctionIndex(),dimensValue,batchTime);
                        String [] keyArr = aggregateKey.split(";");
                        String key = keyArr[0];
                        String column = keyArr[1];
                        String compatibleKey = getCompatibleKey(key);
                        if(StringUtil.isNotEmpty(compatibleKey)){
                            LdpGet ldpGet = LdpGet.with(compatibleKey,column);
                            compatibleGetList.add(ldpGet);
                            compatibleKeyMap.put(compatibleKey,key);
                        }
                        LdpGet ldpGet = LdpGet.with(key,column);
                        getList.add(ldpGet);
                    }
                }
            }
            Validate.isTrue(getList.size() <= StatConst.QUERY_RESULT_LIMIT_SIZE);
        }
        List<LdpResult<Long>> results = WarehouseStorageEngineProxy.getInstance().gets(metaName,getList,Long.class);
        List<LdpResult<Long>> compatibleResults = WarehouseStorageEngineProxy.getInstance().gets(metaName,compatibleGetList,Long.class);
        Map<String,LdpResult<Long>> dbResultMap = results.stream().filter(x -> x.getData() != null).collect(Collectors.toMap(x -> x.getKey() + ";" + x.getColumn(), x -> x));
        Map<String,LdpResult<Long>> compatibleDBResultMap = compatibleResults.stream().filter(x -> x.getData() != null).collect(Collectors.toMap(x -> x.getKey() + ";" + x.getColumn(), x -> x));
        for(String compatibleAggregateKey : compatibleDBResultMap.keySet()){
            LdpResult<Long> compatibleResult = compatibleDBResultMap.get(compatibleAggregateKey);
            if(compatibleResult != null && compatibleResult.getData() != 0L){
                String [] keyArr = compatibleAggregateKey.split(";");
                String compatibleKey = keyArr[0];
                String column = keyArr[1];
                String mappingKey = compatibleKeyMap.get(compatibleKey);
                compatibleResult.setKey(mappingKey);
                dbResultMap.put(mappingKey+";"+column,compatibleResult);
            }
        }
        LinkedHashMap<String,List<StatValue>> resultMap = new LinkedHashMap<>();
        if(dimensValueList == null){
            List<StatValue> valueList = new ArrayList<>();
            for(long batchTime : batchTimeList){
                StatValue statValue = calculate(statExtEntity,null,batchTime,dbResultMap);
                valueList.add(statValue);
            }
            resultMap.put(null,valueList);
        }else{
            for(String dimensValue : dimensValueList){
                List<StatValue> valueList = new ArrayList<>();
                for(long batchTime : batchTimeList){
                    StatValue statValue = calculate(statExtEntity,dimensValue,batchTime,dbResultMap);
                    valueList.add(statValue);
                }
                resultMap.put(dimensValue,valueList);
            }
        }
        return resultMap;
    }

    private String getCompatibleKey(String key){
        String [] arr = key.split("\\|");
        if(arr.length >= 2 && StringUtil.isNumber(arr[1])){
            return arr[0] + "|" + arr[2];
        }else{
            return null;
        }
    }

    public StatValue calculate(StatExtEntity statExtEntity, String dimensValue, long batchTime,Map<String, LdpResult<Long>> resultMap) {
        boolean invalidFlag = false;
        String formula = statExtEntity.getTemplateEntity().getCompleteStat();
        StatValue statValue = new StatValue();
        statValue.setBatchTime(batchTime);
        statValue.setDimensValue(dimensValue);
        statValue.setDisplayBatchTime(BatchAdapter.dateTimeFormat(statExtEntity.getTimeparam(),batchTime));
        long lastUpdateTime = 0;
        HashMap<String,Object> envMap = new HashMap<>();
        int variableIndex = 97;
        List<StatState> statStates = statExtEntity.getTemplateEntity().getStatStateList();
        List<Object> statesValue = new ArrayList<>();
        for (StatState statState : statStates) {
            String stateBody = statState.getStateBody();
            String aggregateKey = keyGenerator.resultKey(statExtEntity, statState.getFunctionIndex(), dimensValue, batchTime);
            LdpResult<Long> ldpResult = (resultMap == null || resultMap.get(aggregateKey) == null ? null:resultMap.get(aggregateKey));
            if (ldpResult == null) {
                invalidFlag = true;
                statesValue.add("0");
            } else {
                BigDecimal value = BigDecimal.valueOf(ldpResult.getData()).divide(BigDecimal.valueOf(1000D),3, RoundingMode.HALF_UP).stripTrailingZeros();
                String replaceId = String.valueOf((char)variableIndex);
                variableIndex++;
                envMap.put(replaceId,value.toPlainString());
                statesValue.add(value.toPlainString());
                formula = formula.replace(stateBody, replaceId);
                if(lastUpdateTime  < ldpResult.getTimestamp()){
                    lastUpdateTime = ldpResult.getTimestamp();
                }
            }
        }
        statValue.setStatesValue(statesValue);
        if (!invalidFlag) {
            Object object = AviatorHandler.execute(formula,envMap);
            if (object != null) {
                if(object.getClass() == BigDecimal.class){
                    BigDecimal bigDecimal = ((BigDecimal) object).stripTrailingZeros();
                    statValue.setValue(bigDecimal.toPlainString());
                }else {
                    statValue.setValue(new BigDecimal(object.toString()).setScale(3,RoundingMode.HALF_UP).stripTrailingZeros().toPlainString());
                }
            }
        }
        statValue.setLastUpdateTime(lastUpdateTime);
        return statValue;
    }

    @Override
    public LinkedHashMap<IndicatorGet, Object> query(List<IndicatorGet> indicators) throws Exception {
        LinkedHashMap<IndicatorGet,Object> resultMap = new LinkedHashMap<>();
        if(CollectionUtils.isEmpty(indicators)){
            return resultMap;
        }
        HashMap<String,List<String>> keyMap = new HashMap<>();
        HashMap<Integer,StatExtEntity> statMap = new HashMap<>();
        for(IndicatorGet indicatorGet : indicators){
            int statId = indicatorGet.getStatId();
            StatExtEntity statExtEntity = StatDBWrapper.queryById(statId);
            if(statExtEntity == null){
                continue;
            }
            int metaId = statExtEntity.getMetaId();
            MetaTable metaTable = MetaTableWrapper.queryById(metaId);
            if(metaTable == null){
                continue;
            }
            statMap.put(statExtEntity.getId(),statExtEntity);
            String metaName = metaTable.getMetaName();
            int indicator = indicatorGet.getIndicatorIndex();
            long batchTime = indicatorGet.getBatchTime();
            String dimensValue = indicatorGet.getDimensValue();
            if(indicator == 0){
                for(StatState statState : statExtEntity.getTemplateEntity().getStatStateList()){
                    String rowKey = keyGenerator.resultKey(statExtEntity,statState.getFunctionIndex(),dimensValue,batchTime);
                    List<String> list = keyMap.getOrDefault(metaName,new ArrayList<>());
                    list.add(rowKey);
                    keyMap.put(metaName,list);
                }
            }else{
                String rowKey = keyGenerator.resultKey(statExtEntity,indicator - 1,dimensValue,batchTime);
                List<String> list = keyMap.getOrDefault(metaName,new ArrayList<>());
                list.add(rowKey);
                keyMap.put(metaName,list);
            }
        }
        List<LdpGet> getList = new ArrayList<>();
        Map<String,LdpResult<Long>> allResultMap = new HashMap<>();
        for(String metaName : keyMap.keySet()){
            List<String> keyList = keyMap.get(metaName);
            for(String rowKey : keyList){
                String [] keyArr = rowKey.split(";");
                String key = keyArr[0];
                String column = keyArr[1];
                LdpGet ldpGet = LdpGet.with(key,column);
                if(!getList.contains(ldpGet)){
                    getList.add(ldpGet);
                }
            }
            List<LdpResult<Long>> results = WarehouseStorageEngineProxy.getInstance().gets(metaName,getList,Long.class);
            Map<String,LdpResult<Long>> subResultMap = results.stream().filter(x -> x.getData() != null).collect(Collectors.toMap(x -> x.getKey() + ";" + x.getColumn(), x -> x));
            allResultMap.putAll(subResultMap);
        }
        for(IndicatorGet indicatorGet : indicators){
            int indicator = indicatorGet.getIndicatorIndex();
            StatExtEntity statExtEntity = statMap.get(indicatorGet.getStatId());
            if(indicator == 0){
                StatValue statValue = calculate(statExtEntity, indicatorGet.getDimensValue() , indicatorGet.getBatchTime(),allResultMap);
                resultMap.put(indicatorGet,Double.parseDouble(statValue.getValue().toString()));
            }else{
                String rowKey = keyGenerator.resultKey(statExtEntity,indicator - 1,indicatorGet.getDimensValue(),indicatorGet.getBatchTime());
                LdpResult<Long> ldpResult = allResultMap.get(rowKey);
                if(ldpResult != null){
                    BigDecimal value = BigDecimal.valueOf(ldpResult.getData()).divide(BigDecimal.valueOf(1000D),3, RoundingMode.HALF_UP).stripTrailingZeros();
                    resultMap.put(indicatorGet,Double.parseDouble(value.toPlainString()));
                }else{
                    resultMap.put(indicatorGet,0D);
                }
            }
        }
        return resultMap;
    }
}
