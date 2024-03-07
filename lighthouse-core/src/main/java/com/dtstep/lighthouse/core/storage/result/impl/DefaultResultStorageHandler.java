package com.dtstep.lighthouse.core.storage.result.impl;

import com.dtstep.lighthouse.common.constant.StatConst;
import com.dtstep.lighthouse.common.entity.calculate.MicroBucket;
import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.entity.state.StatState;
import com.dtstep.lighthouse.common.entity.view.StatValue;
import com.dtstep.lighthouse.common.modal.MetaTable;
import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.common.util.StringUtil;
import com.dtstep.lighthouse.core.expression.embed.AviatorHandler;
import com.dtstep.lighthouse.core.rowkey.KeyGenerator;
import com.dtstep.lighthouse.core.rowkey.impl.DefaultKeyGenerator;
import com.dtstep.lighthouse.core.storage.*;
import com.dtstep.lighthouse.core.storage.engine.StorageEngineProxy;
import com.dtstep.lighthouse.core.storage.result.ResultStorageHandler;
import com.dtstep.lighthouse.core.wrapper.MetaTableWrapper;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
                long ttl = bucket.getTTL();
                long value = subList.stream().map(MicroBucket::getValue).mapToLong(x -> x).sum();
                if(logger.isTraceEnabled()){
                    logger.trace("lighthouse trace,batch increment,statId:{},batchTime:{},meta:{},rowKey:{},dimens:{},column:{},functionIndex:{},value:{},ttl:{}",
                            bucket.getStatId(), DateUtil.formatTimeStamp(bucket.getBatchTime(),"yyyy-MM-dd HH:mm:ss"),bucket.getMetaName(),bucket.getRowKey(),bucket.getDimensValue(),bucket.getColumn(),bucket.getFunctionIndex(),value,bucket.getTTL());
                }
                LdpIncrement ldpIncrement = LdpIncrement.with(rowKey,column,value,ttl);
                ldpIncrements.add(ldpIncrement);
            }
            try {
                StorageEngineProxy.getInstance().increments(metaName,ldpIncrements);
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
                long ttl = bucket.getTTL();
                long value = subList.stream().map(MicroBucket::getValue).mapToLong(x -> x).max().getAsLong();
                if(logger.isTraceEnabled()){
                    logger.trace("lighthouse trace,batch maxPut,statId:{},batchTime:{},meta:{},rowKey:{},dimens:{},column:{},functionIndex:{},value:{},ttl:{}",
                            bucket.getStatId(), DateUtil.formatTimeStamp(bucket.getBatchTime(),"yyyy-MM-dd HH:mm:ss"),bucket.getMetaName(),bucket.getRowKey(),bucket.getDimensValue(),bucket.getColumn(),bucket.getFunctionIndex(),value,bucket.getTTL());
                }
                LdpPut ldpPut = LdpPut.with(rowKey,column,value,ttl);
                puts.add(ldpPut);
            }
            try {
                StorageEngineProxy.getInstance().putsWithCompare(metaName, CompareOperator.GREATER,puts);
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
                long ttl = bucket.getTTL();
                long value = subList.stream().map(MicroBucket::getValue).mapToLong(x -> x).min().getAsLong();
                if(logger.isTraceEnabled()){
                    logger.trace("lighthouse trace,batch minPut,statId:{},batchTime:{},meta:{},rowKey:{},dimens:{},column:{},functionIndex:{},value:{},ttl:{}",
                            bucket.getStatId(), DateUtil.formatTimeStamp(bucket.getBatchTime(),"yyyy-MM-dd HH:mm:ss"),bucket.getMetaName(),bucket.getRowKey(),bucket.getDimensValue(),bucket.getColumn(),bucket.getFunctionIndex(),value,bucket.getTTL());
                }
                LdpPut ldpPut = LdpPut.with(rowKey,column,value,ttl);
                puts.add(ldpPut);
            }
            try {
                StorageEngineProxy.getInstance().putsWithCompare(metaName, CompareOperator.LESSER,puts);
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
                long ttl = bucket.getTTL();
                long value = subList.get(0).getValue();
                if(logger.isTraceEnabled()){
                    logger.trace("lighthouse trace,batch put,statId:{},batchTime:{},meta:{},rowKey:{},dimens:{},column:{},functionIndex:{},value:{},ttl:{}",
                            bucket.getStatId(),DateUtil.formatTimeStamp(bucket.getBatchTime(),"yyyy-MM-dd HH:mm:ss"),bucket.getMetaName(),bucket.getRowKey(),bucket.getDimensValue(),bucket.getColumn(),bucket.getFunctionIndex(),value,bucket.getTTL());
                }
                LdpPut ldpPut = LdpPut.with(rowKey,column,value,ttl);
                ldpPuts.add(ldpPut);
            }
            try {
                StorageEngineProxy.getInstance().puts(metaName,ldpPuts);
            } catch (Exception ex) {
                logger.error("data put exception!", ex);
            }
        }
    }

    @Override
    public StatValue query(StatExtEntity statExtEntity, String dimensValue, long batchTime) throws Exception {
        Map<String,List<StatValue>> resultMap = queryWithDimensList(statExtEntity,List.of(dimensValue),List.of(batchTime));
        return MapUtils.isEmpty(resultMap) || CollectionUtils.isEmpty(resultMap.get(dimensValue)) ? null : resultMap.get(dimensValue).get(0);
    }

    @Override
    public List<StatValue> query(StatExtEntity statExtEntity, String dimensValue, List<Long> batchTimeList) throws Exception {
        Validate.isTrue(CollectionUtils.isNotEmpty(batchTimeList));
        Map<String,List<StatValue>> resultMap = queryWithDimensList(statExtEntity,List.of(dimensValue),batchTimeList);
        return MapUtils.isEmpty(resultMap) || CollectionUtils.isEmpty(resultMap.get(dimensValue)) ? null : resultMap.get(dimensValue);
    }

    @Override
    public Map<String, StatValue> queryWithDimensList(StatExtEntity statExtEntity, List<String> dimensValueList, long batchTime) throws Exception {
        Validate.isTrue(CollectionUtils.isNotEmpty(dimensValueList));
        Map<String,List<StatValue>> dbMap = queryWithDimensList(statExtEntity,dimensValueList,List.of(batchTime));
        Map<String,StatValue> resultMap = new HashMap<>();
        for(String key : dbMap.keySet()){
            resultMap.put(key,dbMap.get(key).get(0));
        }
        return resultMap;
    }

    @Override
    public Map<String,List<StatValue>> queryWithDimensList(StatExtEntity statExtEntity, List<String> dimensValueList, List<Long> batchTimeList) throws Exception {
        List<StatState> statStates = statExtEntity.getTemplateEntity().getStatStateList();
        int resMeta = statExtEntity.getMetaId();
        String metaName;
        if(statExtEntity.isBuiltIn()){
            metaName = StatConst.SYSTEM_STAT_RESULT_TABLE;
        }else{
            MetaTable metaTable = MetaTableWrapper.queryById(resMeta);
            metaName = metaTable.getMetaName();
        }
        List<LdpGet> getList = new ArrayList<>();
        for (long batchTime : batchTimeList) {
            for(String dimensValue : dimensValueList) {
                for (StatState statState : statStates) {
                    String aggregateKey = keyGenerator.resultKey(statExtEntity,statState.getFunctionIndex(),dimensValue,batchTime);
                    String [] keyArr = aggregateKey.split(";");
                    LdpGet ldpGet = LdpGet.with(keyArr[0],keyArr[1]);
                    getList.add(ldpGet);
                }
            }
        }
        Validate.isTrue(getList.size() <= StatConst.QUERY_RESULT_LIMIT_SIZE);
        List<LdpResult<Long>> results = StorageEngineProxy.getInstance().gets(metaName,getList,Long.class);
        Map<String,LdpResult<Long>> dbResultMap = results.stream().filter(x -> x.getData() != null).collect(Collectors.toMap(x -> x.getKey() + ";" + x.getColumn(), x -> x));
        Map<String,List<StatValue>> resultMap = new HashMap<>();
        for(String dimensValue : dimensValueList){
            List<StatValue> valueList = new ArrayList<>();
            for(long batchTime : batchTimeList){
                StatValue statValue = calculate(statExtEntity,dimensValue,batchTime,dbResultMap);
                valueList.add(statValue);
            }
            resultMap.put(dimensValue,valueList);
        }
        return resultMap;
    }

    private StatValue calculate(StatExtEntity statExtEntity, String dimensValue, long batchTime,Map<String, LdpResult<Long>> resultMap) {
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
            String aggregateKey = keyGenerator.resultKey(statExtEntity, statState.getFunctionIndex(), dimensValue, batchTime);
            LdpResult<Long> ldpResult = (resultMap == null || resultMap.get(aggregateKey) == null ? null:resultMap.get(aggregateKey));
            if (ldpResult == null) {
                invalidFlag = true;
                statesValue.add(0d);
            } else {
                BigDecimal value = BigDecimal.valueOf(ldpResult.getData()).divide(BigDecimal.valueOf(1000D),3, RoundingMode.HALF_UP);
                String replaceId = String.valueOf((char)variableIndex);
                variableIndex++;
                envMap.put(replaceId,value);
                statesValue.add(value.toString());
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
}
