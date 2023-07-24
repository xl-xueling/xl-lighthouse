package com.dtstep.lighthouse.core.consumer;
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
import com.dtstep.lighthouse.core.functions.*;
import com.google.common.base.Splitter;
import com.google.common.collect.*;
import com.dtstep.lighthouse.common.aggregator.SlotsGroup;
import com.dtstep.lighthouse.common.constant.StatConst;
import com.dtstep.lighthouse.common.entity.event.SimpleSlotEvent;
import com.dtstep.lighthouse.common.entity.meta.MetaTableEntity;
import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.entity.state.StatState;
import com.dtstep.lighthouse.common.enums.function.FunctionEnum;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.common.util.StringUtil;
import com.dtstep.lighthouse.common.aggregator.EventPool;
import com.dtstep.lighthouse.core.functions.*;
import com.dtstep.lighthouse.core.wrapper.MetaTableWrapper;
import com.dtstep.lighthouse.core.wrapper.StatDBWrapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public final class ExpandedEventRunnable implements Runnable{

    private static final Logger logger = LoggerFactory.getLogger(ExpandedEventRunnable.class);

    private final EventPool<SimpleSlotEvent> eventPool;

    ExpandedEventRunnable(EventPool<SimpleSlotEvent> eventPool){
        this.eventPool = eventPool;
    }

    private static final int batchLimitSize = 15000;

    @Override
    public void run() {
        IntStream.range(0, eventPool.slotSize()).forEach(this::consumer);
    }

    private void consumer(int slot){
        try{
            SlotsGroup.SlotWrapper<SimpleSlotEvent> slotWrapper = eventPool.take(slot);
            while (slotWrapper.size() > batchLimitSize * StatConst.backlog_factor || System.currentTimeMillis() - slotWrapper.getLastAccessTime() > TimeUnit.SECONDS.toMillis(15)){
                StopWatch stopWatch = new StopWatch();
                stopWatch.start();
                List<SimpleSlotEvent> events = slotWrapper.getEvents(batchLimitSize);
                if(CollectionUtils.isEmpty(events)){
                    break;
                }
                if(logger.isTraceEnabled()){
                    events.forEach(z -> logger.trace("event consumer message:[{}],repeat:[{}]",z.getMessage(),z.getRepeat()));
                }
                List<AggregateEvent> aggregateEvents = Lists.newArrayListWithCapacity(10000);
                Map<String,Long> counterMap = events.parallelStream().collect(Collectors.groupingBy(SimpleSlotEvent::getMessage, Collectors.summingLong(SimpleSlotEvent::getRepeat)));
                for (Map.Entry<String, Long> entry : counterMap.entrySet()) {
                    Iterator<String> iterator = Splitter.on(StatConst.SEPARATOR_LEVEL_1).split(entry.getKey()).iterator();
                    AggregateEvent aggregateEvent = new AggregateEvent(iterator.next(),Integer.parseInt(iterator.next()),
                            iterator.next(),iterator.next(),
                            Integer.parseInt(iterator.next()),
                            Long.parseLong(iterator.next()),
                            entry.getValue());
                    aggregateEvents.add(aggregateEvent);
                }
                Map<String,List<AggregateEvent>> eventMap = aggregateEvents.parallelStream().collect(Collectors.groupingBy(AggregateEvent::getAggregateKey));
                for (Map.Entry<String, List<AggregateEvent>> stringListEntry : eventMap.entrySet()) {
                    List<AggregateEvent> lists = stringListEntry.getValue();
                    AggregateEvent entity = lists.get(0);
                    List<Pair<String,Long>> dataList = lists.stream().map(x -> Pair.of(x.getData(),x.getRepeat())).collect(Collectors.toList());
                    consumer(entity.getStatId(), entity.getAggregateKey(), entity.getFunctionIndex(), entity.getDimensValue(), entity.getBatchTime(), dataList);
                }
                logger.info("process expanded events,thread:{},slot:{},process size:{},remaining size:{},eventMap size:{},accessTime:{},cost:{}ms",
                        Thread.currentThread().getName(),slot,events.size(),slotWrapper.size(),eventMap.size(),slotWrapper.getLastAccessTime(),stopWatch.getTime());
            }
        }catch (Exception ex){
            logger.error("process expanded events error!",ex);
        }
    }


    private void consumer(final int statId,final String aggregateKey,final int functionIndex,final String dimensValue,final long batchTime,List<Pair<String,Long>> eventList) throws Exception{
        StatExtEntity statExtEntity = StatDBWrapper.queryById(statId);
        if(statExtEntity == null){
            return;
        }
        if(!StringUtil.isEmpty(statExtEntity.getTemplateEntity().getDimens())){
            try{
                DimensStatProcess.getInstance().process(statExtEntity,dimensValue);
            }catch (Exception ex){
                logger.error("save dimens info error,statId:{},dimensValue:{}", statExtEntity.getId(),dimensValue,ex);
            }
        }
        StatState statState = statExtEntity.getTemplateEntity().getStatStateList().get(functionIndex);
        FunctionEnum functionEnum = statState.getFunctionEnum();
        int metaId = statExtEntity.getResMeta();
        String metaName;
        if(metaId == -1){
            metaName = StatConst.SYSTEM_STAT_RESULT_TABLE;
        }else{
            MetaTableEntity metaTableEntity = MetaTableWrapper.queryById(metaId);
            if(metaTableEntity != null){
                metaName = metaTableEntity.getMetaName();
            }else{
                logger.error("get meta info error,statId:{},metaId:{}",statId,metaId);
                return;
            }
        }
        if(logger.isTraceEnabled()){
            logger.trace("event consumer runnable,statId:{},aggregateKey:{},dimensValue:{},batchTime:{},eventList:{}"
                    ,statId,aggregateKey,dimensValue,batchTime, JsonUtil.toJSONString(eventList));
        }
        switch (functionEnum){
            case COUNT:
                CountStatProcess countStatProcess = new CountStatProcess(statExtEntity,metaName,aggregateKey,dimensValue);
                countStatProcess.evaluate(statState,eventList,batchTime);
                break;
            case BITCOUNT:
                BitCountStatProcess bitStatProcess = new BitCountStatProcess(statExtEntity,metaName,aggregateKey,dimensValue);
                bitStatProcess.evaluate(statState,eventList,batchTime);
                break;
            case SUM:
                SumStatProcess sumStatProcess = new SumStatProcess(statExtEntity,metaName,aggregateKey,dimensValue);
                sumStatProcess.evaluate(statState,eventList,batchTime);
                break;
            case MAX:
                MaxStatProcess maxStatProcess = new MaxStatProcess(statExtEntity,metaName,aggregateKey,dimensValue);
                maxStatProcess.evaluate(statState,eventList,batchTime);
                break;
            case MIN:
                MinStatProcess minStatProcess = new MinStatProcess(statExtEntity,metaName,aggregateKey,dimensValue);
                minStatProcess.evaluate(statState,eventList,batchTime);
                break;
            case SEQ:
                SeqStatProcess seqStatProcess = new SeqStatProcess(statExtEntity,metaName,aggregateKey,dimensValue);
                seqStatProcess.evaluate(statState,eventList,batchTime);
                break;
            default:
                break;
        }
    }
}
