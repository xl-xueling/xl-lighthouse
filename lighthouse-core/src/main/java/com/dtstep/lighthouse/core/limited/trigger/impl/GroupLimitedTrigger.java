package com.dtstep.lighthouse.core.limited.trigger.impl;
/*
 * Copyright (C) 2022-2024 XueLing.雪灵
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
import com.dtstep.lighthouse.common.enums.RecordTypeEnum;
import com.dtstep.lighthouse.common.enums.ResourceTypeEnum;
import com.dtstep.lighthouse.common.modal.Record;
import com.dtstep.lighthouse.core.schedule.DelaySchedule;
import com.dtstep.lighthouse.common.constant.StatConst;
import com.dtstep.lighthouse.common.entity.group.GroupExtEntity;
import com.dtstep.lighthouse.common.enums.GroupStateEnum;
import com.dtstep.lighthouse.core.limited.trigger.Trigger;
import com.dtstep.lighthouse.core.lock.RedLock;
import com.dtstep.lighthouse.core.wrapper.GroupDBWrapper;
import com.dtstep.lighthouse.core.wrapper.LimitingWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

public class GroupLimitedTrigger implements Trigger<GroupExtEntity> {

    private static final Logger logger = LoggerFactory.getLogger(GroupLimitedTrigger.class);

    @Override
    public void execute(GroupExtEntity groupExtEntity) throws Exception{
        String lockKey = "lock_group_limited_" + groupExtEntity.getId();
        boolean isLock = RedLock.tryLock(lockKey,30,120, TimeUnit.SECONDS);
        if(isLock){
            try{
                int result = GroupDBWrapper.changeState(groupExtEntity.getId(),GroupStateEnum.LIMITING);
                if(result == 1){
                    logger.info("lighthouse limited,the statistics group was changed to the current limiting state,groupId:{}", groupExtEntity.getId());
                    new DelaySchedule().delaySchedule(() -> {
                        try{
                            GroupDBWrapper.changeState(groupExtEntity.getId(),GroupStateEnum.RUNNING);
                        }catch (Exception ex){
                            logger.error("change statistics group state error!",ex);
                        }},StatConst.LIMITED_EXPIRE_MINUTES,TimeUnit.MINUTES);
                    Record limitingRecord = new Record();
                    limitingRecord.setRecordType(RecordTypeEnum.GROUP_MESSAGE_LIMITING);
                    limitingRecord.setResourceId(groupExtEntity.getId());
                    limitingRecord.setResourceType(ResourceTypeEnum.Group);
                    limitingRecord.setRecordTime(LocalDateTime.now());
                    LimitingWrapper.record(limitingRecord);
                }
            }catch (Exception ex){
                logger.error("change group state error",ex);
            }finally {
                RedLock.unLock(lockKey);
            }
        }
    }
}
