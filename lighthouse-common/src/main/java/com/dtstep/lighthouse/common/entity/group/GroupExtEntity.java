package com.dtstep.lighthouse.common.entity.group;
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
import com.dtstep.lighthouse.common.enums.meta.ColumnTypeEnum;
import com.dtstep.lighthouse.common.enums.stat.GroupStateEnum;
import com.dtstep.lighthouse.common.util.BeanCopyUtil;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.common.util.StringUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.dtstep.lighthouse.common.constant.StatConst;
import com.dtstep.lighthouse.common.entity.stat.TimeParam;
import com.dtstep.lighthouse.common.entity.meta.MetaColumn;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class GroupExtEntity extends GroupEntity {

    private static final long serialVersionUID = -5705802803071692695L;

    private String verifyKey;

    private List<MetaColumn> columnList;

    private Map<String, ColumnTypeEnum> runningRelatedColumns;

    private Map<String, ColumnTypeEnum> allRelatedColumns;

    private boolean isBuiltIn = false;

    private HashMap<String,Integer> limitedThresholdMap = new HashMap<>();

    private TimeParam minTimeParam;

    private GroupStateEnum groupStateEnum;

    private long dataExpire = TimeUnit.DAYS.toMillis(3);

    public GroupExtEntity(){}

    public GroupExtEntity(GroupEntity groupEntity){
        assert groupEntity != null;
        BeanCopyUtil.copy(groupEntity,this);
    }

    public List<MetaColumn> getColumnList() {
        return columnList;
    }

    public void setColumnList(List<MetaColumn> columnList) {
        this.columnList = columnList;
    }

    public Map<String, ColumnTypeEnum> getRunningRelatedColumns() {
        return runningRelatedColumns;
    }

    public void setRunningRelatedColumns(Map<String, ColumnTypeEnum> runningRelatedColumns) {
        this.runningRelatedColumns = runningRelatedColumns;
    }

    public HashMap<String, Integer> getLimitedThresholdMap() {
        return limitedThresholdMap;
    }

    public void setLimitedThresholdMap(HashMap<String, Integer> limitedThresholdMap) {
        this.limitedThresholdMap = limitedThresholdMap;
    }

    public GroupStateEnum getGroupStateEnum() {
        return groupStateEnum;
    }

    public void setGroupStateEnum(GroupStateEnum groupStateEnum) {
        this.groupStateEnum = groupStateEnum;
        if(groupStateEnum != null){
            this.setState(groupStateEnum.getState());
        }
    }

    public TimeParam getMinTimeParam() {
        return minTimeParam;
    }

    public void setMinTimeParam(TimeParam minTimeParam) {
        this.minTimeParam = minTimeParam;
    }

    public long getDataExpire() {
        return dataExpire;
    }

    public void setDataExpire(long dataExpire) {
        this.dataExpire = dataExpire;
    }

    public boolean isBuiltIn() {
        return isBuiltIn;
    }

    public void setBuiltIn(boolean builtIn) {
        isBuiltIn = builtIn;
    }

    public String getVerifyKey() {
        return verifyKey;
    }

    public void setVerifyKey(String verifyKey) {
        this.verifyKey = verifyKey;
    }

    public Map<String, ColumnTypeEnum> getAllRelatedColumns() {
        return allRelatedColumns;
    }

    public void setAllRelatedColumns(Map<String, ColumnTypeEnum> allRelatedColumns) {
        this.allRelatedColumns = allRelatedColumns;
    }

    public static boolean isLimitedExpired(GroupExtEntity groupExtEntity){
        return groupExtEntity.getState() == GroupStateEnum.LIMITING.getState()
                && (System.currentTimeMillis() - groupExtEntity.getUpdateTime().getTime() >= TimeUnit.MINUTES.toMillis(StatConst.LIMITED_EXPIRE_MINUTES));
    }

    public static boolean isDebugModeExpired(GroupExtEntity groupExtEntity){
        boolean result = false;
        if(groupExtEntity.getDebugMode() == 1 && StringUtil.isNotEmpty(groupExtEntity.getDebugParams())){
            try{
                JsonNode jsonNode = JsonUtil.readTree(groupExtEntity.getDebugParams());
                if(jsonNode != null){
                    long startTime = jsonNode.get("startTime").asLong();
                    long endTime = jsonNode.get("endTime").asLong();
                    long now = System.currentTimeMillis();
                    result = (now > endTime || now < startTime);
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
        return result;
    }
}
