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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.dtstep.lighthouse.common.entity.stat.TimeParam;

import java.io.Serializable;
import java.util.Map;


@JsonIgnoreProperties(ignoreUnknown = true)
public final class GroupVerifyEntity implements Serializable {

    private static final long serialVersionUID = 7612810352713935350L;

    private int groupId;

    private String token;

    private String verifyKey;

    private int state;

    private TimeParam minTimeParam;

    private Map<String, ColumnTypeEnum> relationColumns;

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getVerifyKey() {
        return verifyKey;
    }

    public void setVerifyKey(String verifyKey) {
        this.verifyKey = verifyKey;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Map<String, ColumnTypeEnum> getRelationColumns() {
        return relationColumns;
    }

    public void setRelationColumns(Map<String, ColumnTypeEnum> relationColumns) {
        this.relationColumns = relationColumns;
    }

    public TimeParam getMinTimeParam() {
        return minTimeParam;
    }

    public void setMinTimeParam(TimeParam minTimeParam) {
        this.minTimeParam = minTimeParam;
    }
}
