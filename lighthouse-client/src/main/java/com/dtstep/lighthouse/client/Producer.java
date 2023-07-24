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
import com.dtstep.lighthouse.common.aggregator.EventPool;
import com.dtstep.lighthouse.common.constant.StatConst;
import com.dtstep.lighthouse.common.entity.event.SimpleSlotEvent;
import com.dtstep.lighthouse.common.entity.group.GroupVerifyEntity;
import com.dtstep.lighthouse.common.entity.stat.TimeParam;
import com.dtstep.lighthouse.common.enums.meta.ColumnTypeEnum;
import com.dtstep.lighthouse.common.enums.stat.GroupStateEnum;
import com.dtstep.lighthouse.common.hash.HashUtil;
import com.dtstep.lighthouse.common.ice.AuxInterfacePrx;
import com.dtstep.lighthouse.common.ice.AuxInterfacePrxHelper;
import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.common.util.Md5Util;
import com.dtstep.lighthouse.common.util.MessageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.util.*;


final class Producer {

    private static final Logger logger = LoggerFactory.getLogger(Producer.class);

    private final EventPool<SimpleSlotEvent> eventPool;

    private final AuxInterfacePrx auxInterfacePrx;

    private final MessageSender messageSender;

    Producer(Ice.Communicator ic, EventPool<SimpleSlotEvent> eventPool){
        this.eventPool = eventPool;
        Ice.ObjectPrx auxBasePrx = ic.stringToProxy("identity_aux").ice_connectionId(UUID.randomUUID().toString()).ice_locatorCacheTimeout(1200);
        auxInterfacePrx = AuxInterfacePrxHelper.checkedCast(auxBasePrx);
        this.messageSender = new MessageSender(ic);
    }

    void send(String token, String secretKey, Map<String, Object> paramMap, int repeat, long timestamp,boolean isSync) throws Exception{
        GroupVerifyEntity groupVerifyEntity = AuxHandler.queryStatGroup(auxInterfacePrx,token);
        if(groupVerifyEntity == null){
            logger.error("statistic group({}) not exist!",token);
            return;
        }
        if(!groupVerifyEntity.getVerifyKey().equals(Md5Util.getMD5(secretKey))){
            logger.error("light client key validation failed,token:{},key:{}",token,secretKey);
            return;
        }
        if(groupVerifyEntity.getState() != GroupStateEnum.RUNNING.getState()){
            logger.error("the status of the statistic group is abnormal,token:{}",token);
            return;
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
            if(isSync){
                syncProcess(message,repeat);
            }else{
                process(message,repeat);
            }
        }
    }

    private static final ThreadLocal<DecimalFormat> decimalThreadLocal = ThreadLocal.withInitial(() -> new DecimalFormat("#.###"));

    private void process(String message, int repeat) throws Exception{
        eventPool.put(HashUtil.getHashIndex(message,eventPool.slotSize()),new SimpleSlotEvent(message,repeat));
    }

    private void syncProcess(String message,int repeat) throws Exception{
        String text = message + StatConst.SEPARATOR_LEVEL_1 + repeat;
        messageSender.send(text);
    }
}
