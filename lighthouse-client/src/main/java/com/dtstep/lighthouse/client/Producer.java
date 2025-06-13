package com.dtstep.lighthouse.client;
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
import com.dtstep.lighthouse.common.aggregator.EventPool;
import com.dtstep.lighthouse.common.entity.event.SimpleSlotEvent;
import com.dtstep.lighthouse.common.entity.group.GroupVerifyEntity;
import com.dtstep.lighthouse.common.entity.stat.TimeParam;
import com.dtstep.lighthouse.common.enums.ColumnTypeEnum;
import com.dtstep.lighthouse.common.enums.GroupStateEnum;
import com.dtstep.lighthouse.common.hash.HashUtil;
import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.common.util.MessageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;


final class Producer {

    private static final Logger logger = LoggerFactory.getLogger(Producer.class);

    private final EventPool<SimpleSlotEvent> eventPool;

    Producer(EventPool<SimpleSlotEvent> eventPool){
        this.eventPool = eventPool;
    }

    void send(String token, String secretKey, Map<String, Object> paramMap, int repeat, long timestamp) throws Exception{
        GroupVerifyEntity groupVerifyEntity = AuxHandler.queryGroupInfo(token);
        if(groupVerifyEntity == null){
            throw new IllegalArgumentException(String.format("statistic group(%s) does not exist!",token));
        }
        String md5 = AuxHandler.cacheGetMd5(secretKey);
        if(!groupVerifyEntity.getVerifyKey().equals(md5)){
            throw new IllegalArgumentException(String.format("statistic group(%s) key verification failed!",token));
        }
        if(groupVerifyEntity.getState() != GroupStateEnum.RUNNING){
            throw new IllegalArgumentException(String.format("statistic group(%s) status is abnormal!",token));
        }
        TimeParam timeParam = groupVerifyEntity.getMinTimeParam();
        if(timeParam != null && timeParam.getInterval() != 0){
            LinkedHashMap<String,String> map = new LinkedHashMap<>();
            Map<String, ColumnTypeEnum> relationColumnSet = groupVerifyEntity.getRelationColumns();
            if(relationColumnSet != null){
                for (String column : relationColumnSet.keySet()) {
                    Object columnValue = paramMap.get(column);
                    if(columnValue != null){
                        String columnValueStr = columnValue.toString();
                        map.put(column,columnValueStr);
                    }
                }
            }
            long microTime = DateUtil.batchTime(groupVerifyEntity.getMinTimeParam().getInterval(),groupVerifyEntity.getMinTimeParam().getTimeUnit(),timestamp);
            String message = MessageHelper.serializeOfText(groupVerifyEntity.getGroupId(),map,microTime);
            process(message,repeat);
        }
    }

    private void process(String message, int repeat) throws Exception{
        eventPool.put(HashUtil.getHashIndex(message,eventPool.slotSize()),new SimpleSlotEvent(message,repeat));
    }

}
