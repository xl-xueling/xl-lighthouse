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
import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.enums.StatStateEnum;
import com.dtstep.lighthouse.core.limited.trigger.Trigger;
import com.dtstep.lighthouse.core.lock.RedLock;
import com.dtstep.lighthouse.core.wrapper.LimitingWrapper;
import com.dtstep.lighthouse.core.wrapper.StatDBWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class StatLimitedTrigger implements Trigger<StatExtEntity> {

    private static final Logger logger = LoggerFactory.getLogger(StatLimitedTrigger.class);

    @Override
    public void execute(StatExtEntity statExtEntity) throws Exception {
            String lockKey = "lock_stat_limited_" + statExtEntity.getId();
            boolean isLock = RedLock.tryLock(lockKey,30,120, TimeUnit.SECONDS);
            if(isLock){
                try{
                    int result = StatDBWrapper.changeState(statExtEntity.getId(),StatStateEnum.LIMITING);
                    if(result == 1){
                        logger.info("lighthouse limited,the statistics stat was changed to the current limiting state,statId:{}", statExtEntity.getId());
                        new DelaySchedule().delaySchedule(() -> {
                            try{
                                StatDBWrapper.changeState(statExtEntity.getId(),StatStateEnum.RUNNING);
                            }catch (Exception ex){
                                logger.error("change statistics state error!",ex);
                            }},StatConst.LIMITED_EXPIRE_MINUTES,TimeUnit.MINUTES);
                        Record limitingRecord = new Record();
                        limitingRecord.setRecordType(RecordTypeEnum.STAT_RESULT_LIMITING);
                        limitingRecord.setResourceId(statExtEntity.getId());
                        limitingRecord.setResourceType(ResourceTypeEnum.Stat);
                        limitingRecord.setRecordTime(LocalDateTime.now());
                        LimitingWrapper.record(limitingRecord);
                    }
                }catch (Exception ex){
                    logger.error("change stat state error",ex);
                }finally {
                    RedLock.unLock(lockKey);
                }
            }
    }
}
