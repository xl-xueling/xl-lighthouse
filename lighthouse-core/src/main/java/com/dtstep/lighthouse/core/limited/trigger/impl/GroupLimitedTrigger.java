package com.dtstep.lighthouse.core.limited.trigger.impl;
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
import com.dtstep.lighthouse.core.schedule.DelaySchedule;
import com.dtstep.lighthouse.common.constant.StatConst;
import com.dtstep.lighthouse.common.entity.group.GroupExtEntity;
import com.dtstep.lighthouse.common.entity.limiting.LimitingEntity;
import com.dtstep.lighthouse.common.enums.limiting.LimitingStrategyEnum;
import com.dtstep.lighthouse.common.enums.stat.GroupStateEnum;
import com.dtstep.lighthouse.core.limited.trigger.Trigger;
import com.dtstep.lighthouse.core.lock.RedLock;
import com.dtstep.lighthouse.core.wrapper.GroupDBWrapper;
import com.dtstep.lighthouse.core.wrapper.LimitingWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class GroupLimitedTrigger implements Trigger<GroupExtEntity> {

    private static final Logger logger = LoggerFactory.getLogger(GroupLimitedTrigger.class);

    @Override
    public void execute(GroupExtEntity groupExtEntity) throws Exception{
        String lockKey = "lock_group_limited_" + groupExtEntity.getId();
        boolean isLock = RedLock.tryLock(lockKey,30,120, TimeUnit.SECONDS);
        if(isLock){
            try{
                int result = GroupDBWrapper.changeState(groupExtEntity,GroupStateEnum.LIMITING.getState());
                if(result == 1){
                    logger.info("lighthouse limited,the statistics group was changed to the current limiting state,groupId:{}", groupExtEntity.getId());
                    new DelaySchedule().delaySchedule(() -> {
                        try{
                            GroupDBWrapper.changeState(groupExtEntity,GroupStateEnum.RUNNING.getState());
                        }catch (Exception ex){
                            logger.error("change statistics group state error!",ex);
                        }},StatConst.LIMITED_EXPIRE_MINUTES,TimeUnit.MINUTES);
                    Date date = new Date();
                    LimitingEntity limitingEntity = new LimitingEntity();
                    limitingEntity.setStrategy(LimitingStrategyEnum.GROUP_MESSAGE_SIZE_LIMIT.getStrategy());
                    limitingEntity.setCreateTime(date);
                    limitingEntity.setStartTime(date);
                    limitingEntity.setEndTime(new Date(date.getTime() + TimeUnit.MINUTES.toMillis(StatConst.LIMITED_EXPIRE_MINUTES)));
                    limitingEntity.setRelationId(groupExtEntity.getId());
                    limitingEntity.setRelationType(1);
                    LimitingWrapper.record(limitingEntity);
                }
            }catch (Exception ex){
                logger.error("change group state error",ex);
            }finally {
                RedLock.unLock(lockKey);
            }
        }
    }
}
