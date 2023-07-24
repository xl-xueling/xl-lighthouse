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
import com.dtstep.lighthouse.common.constant.StatConst;
import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.entity.stat.TemplateEntity;
import com.dtstep.lighthouse.common.entity.calculate.MicroCalculateEnum;
import com.dtstep.lighthouse.common.entity.calculate.MicroBucket;
import com.dtstep.lighthouse.common.util.StringUtil;
import com.dtstep.lighthouse.common.entity.state.StatState;
import com.dtstep.lighthouse.common.aggregator.BlockingEventPool;
import com.dtstep.lighthouse.common.aggregator.EventPool;
import com.dtstep.lighthouse.core.formula.FormulaCalculate;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;

import static com.dtstep.lighthouse.common.constant.StatConst.ILLEGAL_VAL;
import static com.dtstep.lighthouse.common.constant.StatConst.NIL_VAL;


public final class CountStatProcess extends StatProcess<Pair<String,Long>> {

    private static final long serialVersionUID = 7650995793688292626L;

    private static final Logger logger = LoggerFactory.getLogger(CountStatProcess.class);

    private static final int poolSlotSize = StatConst.DEFAULT_POOL_SLOT_SIZE;

    private static final EventPool<MicroBucket> eventPool = new BlockingEventPool<>("CountStorageEventPool",poolSlotSize,300000);

    static {
        final int threadSize = 2;
        ScheduledExecutorService service = Executors.newScheduledThreadPool(threadSize,
                new BasicThreadFactory.Builder().namingPattern("count-consumer-schedule-pool-%d").daemon(true).build());
        for (int i = 0; i < threadSize; i++) {
            service.scheduleWithFixedDelay(new ResultStorageThread(eventPool, batchSize), 0, 3, TimeUnit.SECONDS);
        }
    }

    public CountStatProcess(StatExtEntity statExtEntity, String metaName, String aggregateKey, String dimensValue){
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
        long result = messageList.parallelStream().map(x -> {
            Map<String,Object> envMap;
            if(!StringUtil.isEmptyOrNullStr(x.getKey())) {
                Map<String,String> paramMap = Splitter.on(StatConst.SEPARATOR_LEVEL_2).withKeyValueSeparator(StatConst.SEPARATOR_LEVEL_3).split(x.getKey());
                envMap = new HashMap<>(paramMap);
            }else{
                envMap = new HashMap<>();
            }
            return Pair.of(FormulaCalculate.calculate(statState,envMap,batchTime),x.getValue());
        }).filter(x -> x.getLeft() != NIL_VAL && x.getLeft() != ILLEGAL_VAL).mapToLong(x -> x.getLeft() * x.getRight()).sum();
        if(result == 0L){
            return;
        }
        MicroBucket microBucket = new MicroBucket.Builder()
                .setStatId(statExtEntity.getId())
                .setBatchTime(batchTime)
                .setRowKey(rowKey)
                .setColumn(delta)
                .setDimensValue(dimensValue)
                .setCalculateEnum(MicroCalculateEnum.IncCalculate)
                .isLimit(templateEntity.isLimitFlag())
                .setMetaName(metaName)
                .setFunctionIndex(statState.getFunctionIndex())
                .setTTL(ttl)
                .setValue(result).create();
        produce(eventPool,microBucket);
        if(logger.isTraceEnabled()){
            logger.trace("lighthouse trace,count evaluate,stat:{},formula:{},dimens:{},message size:{},cost:{}"
                    , statExtEntity.getId(),templateEntity.getStat(),dimensValue,messageList.size(),stopWatch.getTime());
        }
    }
}