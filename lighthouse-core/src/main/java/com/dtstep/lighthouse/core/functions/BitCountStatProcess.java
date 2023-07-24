package com.dtstep.lighthouse.core.functions;
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

import com.google.common.base.Splitter;
import com.dtstep.lighthouse.common.constant.RedisConst;
import com.dtstep.lighthouse.common.constant.StatConst;
import com.dtstep.lighthouse.common.entity.group.GroupExtEntity;
import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.entity.stat.TemplateEntity;
import com.dtstep.lighthouse.common.entity.state.StatState;
import com.dtstep.lighthouse.common.entity.calculate.MicroBucket;
import com.dtstep.lighthouse.common.entity.calculate.MicroCalculateEnum;
import com.dtstep.lighthouse.common.enums.limiting.LimitingStrategyEnum;
import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.common.util.StringUtil;
import com.dtstep.lighthouse.common.aggregator.BlockingEventPool;
import com.dtstep.lighthouse.common.aggregator.EventPool;
import com.dtstep.lighthouse.core.roaring.BitSetFilterSupplier;
import com.dtstep.lighthouse.core.distinct.RedisRoaringFilter;
import com.dtstep.lighthouse.core.formula.FormulaCalculate;
import com.dtstep.lighthouse.core.wrapper.GroupDBWrapper;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static com.dtstep.lighthouse.common.constant.StatConst.ILLEGAL_VAL;
import static com.dtstep.lighthouse.common.constant.StatConst.NIL_VAL;


public final class BitCountStatProcess extends StatProcess<Pair<String,Long>> {

    private static final long serialVersionUID = 7650995793688292626L;

    private static final Logger logger = LoggerFactory.getLogger(BitCountStatProcess.class);

    private static final int poolSlotSize = StatConst.DEFAULT_POOL_SLOT_SIZE;

    private static final EventPool<MicroBucket> eventPool = new BlockingEventPool<>("BitCountStorageEventPool",poolSlotSize,400000);

    static {
        final int threadSize = 2;
        ScheduledExecutorService service = Executors.newScheduledThreadPool(threadSize,
                new BasicThreadFactory.Builder().namingPattern("bitcount-consumer-schedule-pool-%d").daemon(true).build());
        for (int i = 0; i < threadSize; i++) {
            service.scheduleWithFixedDelay(new ResultStorageThread(eventPool, batchSize), 0, 5, TimeUnit.SECONDS);
        }
    }

    public BitCountStatProcess(StatExtEntity statExtEntity, String metaName, String aggregateKey, String dimensValue){
        this.statExtEntity = statExtEntity;
        this.metaName = metaName;
        this.aggregateKey = aggregateKey;
        int tempIndex = aggregateKey.indexOf(";");
        String rowKey = aggregateKey.substring(0,tempIndex);
        String delta = aggregateKey.substring(tempIndex + 1);
        this.rowKey = rowKey;
        this.delta = delta;
        this.dimensValue = dimensValue;
        this.ttl = getTTL(statExtEntity.getDataExpire());
    }

    @Override
    public void evaluate(StatState statState, List<Pair<String,Long>> messageList, long batchTime) throws Exception {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        TemplateEntity templateEntity = statExtEntity.getTemplateEntity();
        if(logger.isTraceEnabled()){
            logger.trace("lighthouse trace,bitcount stat:{},batchTime:{},messageList:{}"
                    , statExtEntity.getId(), DateUtil.formatTimeStamp(batchTime,"yyyy-MM-dd HH:mm:ss"), JsonUtil.toJSONString(messageList));
        }
        Object[] distinctArray = messageList.parallelStream().map(x -> {
            Map<String,Object> envMap;
            if(!StringUtil.isEmptyOrNullStr(x.getKey())) {
                Map<String,String> paramMap = Splitter.on(StatConst.SEPARATOR_LEVEL_2).withKeyValueSeparator(StatConst.SEPARATOR_LEVEL_3).split(x.getKey());
                envMap = new HashMap<>(paramMap);
            }else{
                envMap = new HashMap<>();
            }
            long calculate = FormulaCalculate.calculate(statState, envMap, batchTime);
            boolean check = calculate != NIL_VAL && calculate != ILLEGAL_VAL;
            String distinctValue = null;
            if(check){
                distinctValue = String.valueOf(envMap.get(StatConst.DISTINCT_COLUMN_NAME));
            }
            return Pair.of(check,distinctValue);
        }).filter(Pair::getKey).map(Pair::getValue).distinct().toArray();
        List distinctList = Arrays.stream(distinctArray).collect(Collectors.toList());
        GroupExtEntity groupExtEntity = GroupDBWrapper.queryById(statExtEntity.getGroupId());
        if(groupExtEntity == null){
            return;
        }
        String distinctKey = RedisRoaringFilter.getInstance().getBloomFilterKey(statExtEntity,dimensValue,statState.getFunctionIndex(),batchTime);
        BitSetFilterSupplier bitSetFilterSupplier = BitSetFilterSupplier.getInstance();
        Iterator<String> iterator = distinctList.iterator();
        while (iterator.hasNext()) {
            String temp = iterator.next();
            long hash = Long.parseLong(temp,36);
            if(bitSetFilterSupplier.check(distinctKey,hash)){
                iterator.remove();
            }
        }
        if(distinctList.isEmpty()){
            return;
        }
        int limit = groupExtEntity.getLimitedThresholdMap().getOrDefault(LimitingStrategyEnum.GROUP_MESSAGE_SIZE_LIMIT.getStrategy(),-1);
        int part = (limit > RedisConst.DISTINCT_LIMIT_THRESHOLD)? 5 : 1;
        long expireSeconds = statExtEntity.getTimeUnit() == TimeUnit.DAYS ? TimeUnit.DAYS.toSeconds(2) : TimeUnit.HOURS.toSeconds(3);
        long result = RedisRoaringFilter.getInstance().filterWithRoaringMap(distinctKey,distinctList,expireSeconds,part);
        if(logger.isTraceEnabled()){
            logger.trace("lighthouse trace,distinct evaluate,statId:{},bloomDistinctKey:{},dimens:{},distinctList:{},bloom filter result:{}"
                    , statExtEntity.getId(),distinctKey,dimensValue,JsonUtil.toJSONString(distinctList),result);
        }
        if(result == 0L){
            return;
        }
        MicroBucket microBucket = new MicroBucket.Builder()
                .setStatId(statExtEntity.getId())
                .setBatchTime(batchTime)
                .setRowKey(rowKey)
                .setDimensValue(dimensValue)
                .setCalculateEnum(MicroCalculateEnum.IncCalculate)
                .isLimit(templateEntity.isLimitFlag())
                .setMetaName(metaName)
                .setFunctionIndex(statState.getFunctionIndex())
                .setColumn(delta)
                .setTTL(ttl)
                .setValue(result).create();
        produce(eventPool,microBucket);
        if(logger.isTraceEnabled()){
            logger.trace("lighthouse trace,bit count evaluate,stat:{},formula:{},dimens:{},message size:{},cost:{}"
                    , statExtEntity.getId(),templateEntity.getStat(),dimensValue,messageList.size(),stopWatch.getTime());
        }
    }

}