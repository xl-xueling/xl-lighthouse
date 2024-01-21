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

import com.dtstep.lighthouse.common.entity.annotation.TemplateAttrAnnotation;
import com.dtstep.lighthouse.common.entity.state.StatState;
import com.dtstep.lighthouse.common.enums.stat.LimitTypeEnum;
import com.dtstep.lighthouse.common.util.StringUtil;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;

public final class TemplateEntity implements Serializable {

    private static final long serialVersionUID = -216878909821657855L;

    private String stat;

    private String completeStat;

    private String title;

    private String dimens;

    private String[] dimensArray;

    private boolean limitFlag = false;

    private String limit;

    private LimitTypeEnum limitTypeEnum;

    private int limitSize;

    private List<StatState> statStateList;

    private List<StatVariableEntity> variableEntityList;

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDimens() {
        return dimens;
    }

    public void setDimens(String dimens) {
        this.dimens = dimens;
    }

    public List<StatVariableEntity> getVariableEntityList() {
        return variableEntityList;
    }

    public void setVariableEntityList(List<StatVariableEntity> variableEntityList) {
        this.variableEntityList = variableEntityList;
    }

    public String getCompleteStat() {
        return completeStat;
    }

    public void setCompleteStat(String completeStat) {
        this.completeStat = completeStat;
    }

    public List<StatState> getStatStateList() {
        return statStateList;
    }

    public void setStatStateList(List<StatState> statStateList) {
        this.statStateList = statStateList;
    }

    public static List<String> getTemplateAttrs(){
        return List.of("stat","title","dimens","limit");
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
        if(!StringUtil.isEmpty(limit)){
            this.limitFlag = true;
        }
    }

    public boolean isLimitFlag() {
        return limitFlag;
    }

    public void setLimitFlag(boolean limitFlag) {
        this.limitFlag = limitFlag;
    }

    public LimitTypeEnum getLimitTypeEnum() {
        return limitTypeEnum;
    }

    public void setLimitTypeEnum(LimitTypeEnum limitTypeEnum) {
        this.limitTypeEnum = limitTypeEnum;
    }

    public int getLimitSize() {
        return limitSize;
    }

    public void setLimitSize(int limitSize) {
        this.limitSize = limitSize;
    }

    public String[] getDimensArray() {
        return dimensArray;
    }

    public void setDimensArray(String[] dimensArray) {
        this.dimensArray = dimensArray;
    }
}
