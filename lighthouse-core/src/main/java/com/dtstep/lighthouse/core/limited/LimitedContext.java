package com.dtstep.lighthouse.core.limited;
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
import com.dtstep.lighthouse.core.config.LDPConfig;
import com.dtstep.lighthouse.core.limited.strategy.impl.GroupMsgStrategyService;
import com.dtstep.lighthouse.common.entity.group.GroupExtEntity;
import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.enums.stat.GroupStateEnum;
import com.dtstep.lighthouse.common.enums.stat.StatStateEnum;
import com.dtstep.lighthouse.core.callback.CallBackMain;
import com.dtstep.lighthouse.core.limited.strategy.Strategy;
import com.dtstep.lighthouse.core.limited.strategy.impl.StatResultStrategyService;
import com.dtstep.lighthouse.core.limited.trigger.Trigger;
import com.dtstep.lighthouse.core.limited.trigger.impl.GroupLimitedTrigger;
import com.dtstep.lighthouse.core.limited.trigger.impl.StatLimitedTrigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class LimitedContext {

    private static final Logger logger = LoggerFactory.getLogger(LimitedContext.class);

    private static final LimitedContext context = new LimitedContext();

    private LimitedContext(){}

    public static LimitedContext getInstance(){
        return context;
    }

    private static final Strategy<GroupExtEntity> groupLimitedService = new GroupMsgStrategyService();

    private static final Strategy<StatExtEntity> statLimitedService = new StatResultStrategyService();

    private static final Trigger<GroupExtEntity> groupTrigger = new GroupLimitedTrigger();

    private static final Trigger<StatExtEntity> statTrigger = new StatLimitedTrigger();

    public boolean tryAcquire(GroupExtEntity groupExtEntity, int permitsPerSecond, int step){
        if(!LDPConfig.limitedEnable()){
            return true;
        }
        if(groupExtEntity.getState() == GroupStateEnum.LIMITING.getState()){
            return false;
        }
        if(permitsPerSecond == -1){
            return true;
        }
        boolean isAcquire = false;
        try{
            isAcquire =  groupLimitedService.process(groupExtEntity,permitsPerSecond,step);
        }catch (Exception ex){
            logger.error("check group limited error!",ex);
        }
        if(!isAcquire){
            try{
                CallBackMain.getInstance().execute(groupTrigger, groupExtEntity);
            }catch (Exception ex){
                logger.error("group limited callback execute error!",ex);
            }
        }
        return isAcquire;
    }

    public boolean tryAcquire(StatExtEntity statExtEntity, int permitsPerSecond, int step){
        if(!LDPConfig.limitedEnable()){
            return true;
        }
        if(statExtEntity.getState() == StatStateEnum.LIMITING.getState()){
            return false;
        }
        if(permitsPerSecond == -1){
            return true;
        }
        boolean isAcquire = false;
        try{
            isAcquire = statLimitedService.process(statExtEntity,permitsPerSecond,step);
        }catch (Exception ex){
            logger.error("check stat limited error!",ex);
        }
        if(!isAcquire){
            try{
                CallBackMain.getInstance().execute(statTrigger, statExtEntity);
            }catch (Exception ex){
                logger.error("stat limited callback execute error!",ex);
            }
        }
        return isAcquire;
    }
}
