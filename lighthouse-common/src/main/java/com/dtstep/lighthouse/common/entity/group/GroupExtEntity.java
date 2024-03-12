package com.dtstep.lighthouse.common.entity.group;
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
import com.dtstep.lighthouse.common.enums.ColumnTypeEnum;
import com.dtstep.lighthouse.common.enums.GroupStateEnum;
import com.dtstep.lighthouse.common.modal.Column;
import com.dtstep.lighthouse.common.modal.Group;
import com.dtstep.lighthouse.common.util.BeanCopyUtil;
import com.dtstep.lighthouse.common.constant.StatConst;
import com.dtstep.lighthouse.common.entity.stat.TimeParam;

import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class GroupExtEntity extends Group {

    private static final long serialVersionUID = -5705802803071692695L;

    private String verifyKey;

    private Map<String, ColumnTypeEnum> runningRelatedColumns;

    private Map<String, ColumnTypeEnum> allRelatedColumns;

    private boolean isBuiltIn = false;

    private TimeParam minTimeParam;

    private long dataExpire = TimeUnit.DAYS.toMillis(3);

    public GroupExtEntity(){}

    public GroupExtEntity(Group groupEntity){
        assert groupEntity != null;
        BeanCopyUtil.copy(groupEntity,this);
    }

    public Map<String, ColumnTypeEnum> getRunningRelatedColumns() {
        return runningRelatedColumns;
    }

    public void setRunningRelatedColumns(Map<String, ColumnTypeEnum> runningRelatedColumns) {
        this.runningRelatedColumns = runningRelatedColumns;
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
        return groupExtEntity.getState() == GroupStateEnum.LIMITING
                && (System.currentTimeMillis() - groupExtEntity.getUpdateTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() >= TimeUnit.MINUTES.toMillis(StatConst.LIMITED_EXPIRE_MINUTES));
    }

//    public static boolean isDebugModeExpired(GroupExtEntity groupExtEntity){
//        boolean result = false;
//        if(groupExtEntity.getDebugMode() == 1 && StringUtil.isNotEmpty(groupExtEntity.getDebugParams())){
//            try{
//                JsonNode jsonNode = JsonUtil.readTree(groupExtEntity.getDebugParams());
//                if(jsonNode != null){
//                    long startTime = jsonNode.get("startTime").asLong();
//                    long endTime = jsonNode.get("endTime").asLong();
//                    long now = System.currentTimeMillis();
//                    result = (now > endTime || now < startTime);
//                }
//            }catch (Exception ex){
//                ex.printStackTrace();
//            }
//        }
//        return result;
//    }
}
