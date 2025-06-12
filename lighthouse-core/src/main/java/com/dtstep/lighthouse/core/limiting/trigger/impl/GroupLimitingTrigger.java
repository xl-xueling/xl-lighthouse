package com.dtstep.lighthouse.core.limiting.trigger.impl;
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
import com.dtstep.lighthouse.common.enums.LimitingStrategyEnum;
import com.dtstep.lighthouse.common.enums.RecordTypeEnum;
import com.dtstep.lighthouse.common.enums.ResourceTypeEnum;
import com.dtstep.lighthouse.common.modal.Record;
import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.common.constant.StatConst;
import com.dtstep.lighthouse.common.entity.group.GroupExtEntity;
import com.dtstep.lighthouse.common.enums.GroupStateEnum;
import com.dtstep.lighthouse.core.limiting.trigger.Trigger;
import com.dtstep.lighthouse.core.lock.RedissonLock;
import com.dtstep.lighthouse.core.wrapper.GroupDBWrapper;
import com.dtstep.lighthouse.core.wrapper.LimitingWrapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

public class GroupLimitingTrigger implements Trigger<GroupExtEntity> {

    private static final Logger logger = LoggerFactory.getLogger(GroupLimitingTrigger.class);

    @Override
    public void execute(GroupExtEntity groupExtEntity) throws Exception{
        String lockKey = "lock_group_limited_" + groupExtEntity.getId();
        boolean isLock = RedissonLock.tryLock(lockKey,30,120, TimeUnit.SECONDS);
        if(isLock){
            try{
                LocalDateTime localDateTime = LocalDateTime.now();
                int result = GroupDBWrapper.changeState(groupExtEntity.getId(),GroupStateEnum.LIMITING,localDateTime);
                if(result == 1){
                    logger.info("group changed to the limiting state,groupId:{}", groupExtEntity.getId());
                    Record limitingRecord = new Record();
                    long startTime = DateUtil.translateToTimeStamp(localDateTime);
                    limitingRecord.setRecordType(RecordTypeEnum.GROUP_MESSAGE_LIMITING);
                    limitingRecord.setResourceId(groupExtEntity.getId());
                    limitingRecord.setResourceType(ResourceTypeEnum.Group);
                    ObjectNode extendNode = JsonUtil.createObjectNode();
                    extendNode.put("strategy", LimitingStrategyEnum.GROUP_MESSAGE_SIZE_LIMITING.getStrategy());
                    extendNode.put("startTime", startTime);
                    extendNode.put("endTime",DateUtil.getMinuteAfter(startTime,StatConst.LIMITING_EXPIRE_MINUTES));
                    limitingRecord.setExtend(extendNode.toString());
                    limitingRecord.setCreateTime(localDateTime);
                    LimitingWrapper.record(limitingRecord);
                }
            }catch (Exception ex){
                logger.error("group limiting trigger process error!",ex);
            }finally {
                RedissonLock.unLock(lockKey);
            }
        }
    }
}
