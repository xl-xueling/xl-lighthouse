package com.dtstep.lighthouse.common.entity.stat;
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
import com.dtstep.lighthouse.common.enums.GroupStateEnum;
import com.dtstep.lighthouse.common.enums.StatStateEnum;
import com.dtstep.lighthouse.common.modal.Stat;
import com.dtstep.lighthouse.common.util.BeanCopyUtil;
import com.dtstep.lighthouse.common.constant.StatConst;

import java.time.ZoneId;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class StatExtEntity extends Stat {

    private static final long serialVersionUID = 1082168950806722079L;

    private int timeParamInterval;

    private boolean isSequence = false;

    private TimeUnit timeUnit;

    private TemplateEntity templateEntity;

    private Set<String> relatedColumnSet;

    private boolean isBuiltIn = false;

    public StatExtEntity(){}

    public StatExtEntity(Stat stat){
        assert stat != null;
        BeanCopyUtil.copy(stat,this);
    }

    public TemplateEntity getTemplateEntity() {
        return templateEntity;
    }

    public void setTemplateEntity(TemplateEntity templateEntity) {
        this.templateEntity = templateEntity;
    }

    public int getTimeParamInterval() {
        return timeParamInterval;
    }

    public void setTimeParamInterval(int timeParamInterval) {
        this.timeParamInterval = timeParamInterval;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    public Set<String> getRelatedColumnSet() {
        return relatedColumnSet;
    }

    public void setRelatedColumnSet(Set<String> relatedColumnSet) {
        this.relatedColumnSet = relatedColumnSet;
    }

    public boolean isSequence() {
        return isSequence;
    }

    public void setSequence(boolean sequence) {
        isSequence = sequence;
    }

    public boolean isBuiltIn() {
        return isBuiltIn;
    }

    public void setBuiltIn(boolean builtIn) {
        isBuiltIn = builtIn;
    }

    public static boolean isLimitedExpired(StatExtEntity statExtEntity){
        return statExtEntity.getState() == StatStateEnum.LIMITING
                && statExtEntity.getLimitingParam().getEndTime() < System.currentTimeMillis();
    }
}
