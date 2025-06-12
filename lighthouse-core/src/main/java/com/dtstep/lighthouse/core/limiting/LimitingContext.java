package com.dtstep.lighthouse.core.limiting;
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
import com.dtstep.lighthouse.core.config.LDPConfig;
import com.dtstep.lighthouse.core.limiting.strategy.impl.GroupMsgStrategyService;
import com.dtstep.lighthouse.common.entity.group.GroupExtEntity;
import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.enums.GroupStateEnum;
import com.dtstep.lighthouse.common.enums.StatStateEnum;
import com.dtstep.lighthouse.core.callback.CallBackMain;
import com.dtstep.lighthouse.core.limiting.strategy.Strategy;
import com.dtstep.lighthouse.core.limiting.strategy.impl.StatResultStrategyService;
import com.dtstep.lighthouse.core.limiting.trigger.Trigger;
import com.dtstep.lighthouse.core.limiting.trigger.impl.GroupLimitingTrigger;
import com.dtstep.lighthouse.core.limiting.trigger.impl.StatLimitingTrigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class LimitingContext {

    private static final Logger logger = LoggerFactory.getLogger(LimitingContext.class);

    private static final LimitingContext context = new LimitingContext();

    private LimitingContext(){}

    public static LimitingContext getInstance(){
        return context;
    }

    private static final Strategy<GroupExtEntity> groupLimitingService = new GroupMsgStrategyService();

    private static final Strategy<StatExtEntity> statLimitingService = new StatResultStrategyService();

    private static final Trigger<GroupExtEntity> groupTrigger = new GroupLimitingTrigger();

    private static final Trigger<StatExtEntity> statTrigger = new StatLimitingTrigger();

    public boolean tryAcquire(GroupExtEntity groupExtEntity, int permitsPerSecond, int step){
        if(!LDPConfig.limitedEnable()){
            return true;
        }
        if(groupExtEntity.getState() == GroupStateEnum.LIMITING){
            return false;
        }
        if(permitsPerSecond == -1){
            return true;
        }
        boolean isAcquire = false;
        try{
            isAcquire =  groupLimitingService.process(groupExtEntity,permitsPerSecond,step);
        }catch (Exception ex){
            logger.error("limiting tryAcquire error!",ex);
        }
        if(!isAcquire){
            try{
                CallBackMain.getInstance().execute(groupTrigger, groupExtEntity);
            }catch (Exception ex){
                logger.error("group limiting callback execute error!",ex);
            }
        }
        return isAcquire;
    }

    public boolean tryAcquire(StatExtEntity statExtEntity, int permitsPerSecond, int step){
        if(!LDPConfig.limitedEnable()){
            return true;
        }
        if(statExtEntity.getState() == StatStateEnum.LIMITING){
            return false;
        }
        if(permitsPerSecond == -1){
            return true;
        }
        boolean isAcquire = false;
        try{
            isAcquire = statLimitingService.process(statExtEntity,permitsPerSecond,step);
        }catch (Exception ex){
            logger.error("check stat limiting error!",ex);
        }
        if(!isAcquire){
            try{
                CallBackMain.getInstance().execute(statTrigger, statExtEntity);
            }catch (Exception ex){
                logger.error("stat limiting callback execute error!",ex);
            }
        }
        return isAcquire;
    }
}
