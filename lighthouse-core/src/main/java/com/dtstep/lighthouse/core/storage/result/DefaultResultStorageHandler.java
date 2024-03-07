package com.dtstep.lighthouse.core.storage.result;

import com.dtstep.lighthouse.common.constant.StatConst;
import com.dtstep.lighthouse.common.entity.calculate.MicroBucket;
import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.entity.state.StatState;
import com.dtstep.lighthouse.common.entity.view.StatValue;
import com.dtstep.lighthouse.common.modal.MetaTable;
import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.core.batch.BatchAdapter;
import com.dtstep.lighthouse.core.rowkey.KeyGenerator;
import com.dtstep.lighthouse.core.rowkey.impl.DefaultKeyGenerator;
import com.dtstep.lighthouse.core.storage.CompareOperator;
import com.dtstep.lighthouse.core.storage.LdpIncrement;
import com.dtstep.lighthouse.core.storage.LdpPut;
import com.dtstep.lighthouse.core.storage.engine.StorageEngineProxy;
import com.dtstep.lighthouse.core.wrapper.MetaTableWrapper;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.Validate;
import org.javatuples.Quartet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DefaultResultStorageHandler implements ResultStorageHandler<MicroBucket, StatValue>{

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
        List<StatValue> valueList = query(statExtEntity,List.of(dimensValue),List.of(batchTime));
        Validate.isTrue(valueList.size() == 1);
        return valueList.get(0);
    }

    @Override
    public List<StatValue> query(StatExtEntity statExtEntity, List<String> dimensValueList, List<Long> batchTimeList) throws Exception {
        List<StatValue> list = Lists.newArrayList();
        List<StatState> statStates = statExtEntity.getTemplateEntity().getStatStateList();
        int resMeta = statExtEntity.getMetaId();
        String metaName;
        if(statExtEntity.isBuiltIn()){
            metaName = StatConst.SYSTEM_STAT_RESULT_TABLE;
        }else{
            MetaTable metaTable = MetaTableWrapper.queryById(resMeta);
            metaName = metaTable.getMetaName();
        }
        List<String> aggregateKeyList = Lists.newArrayList();
        for (long batchTime : batchTimeList) {
            for(String dimensValue : dimensValueList) {
                for (StatState statState : statStates) {
                    String aggregateKey = keyGenerator.resultKey(statExtEntity,statState.getFunctionIndex(),dimensValue,batchTime);
                    aggregateKeyList.add(aggregateKey);
                }
            }
        }

        return null;
    }
}
