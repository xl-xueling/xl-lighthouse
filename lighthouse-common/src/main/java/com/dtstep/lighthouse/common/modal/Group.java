package com.dtstep.lighthouse.common.modal;
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
import com.dtstep.lighthouse.common.enums.GroupStateEnum;
import com.dtstep.lighthouse.common.enums.SwitchStateEnum;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class Group implements Serializable {

    private Integer id;

    private String token;

    private Integer projectId;

    private List<Column> columns;

    private String secretKey;

    private GroupStateEnum state;

    private String projectTitle;
    
    private String desc;

    private Integer dataVersion = 0;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private LocalDateTime refreshTime;

    private GroupExtendConfig extendConfig = new GroupExtendConfig();

    private LimitingParam limitingParam;

    private DebugParam debugParam;

    private SwitchStateEnum debugMode = SwitchStateEnum.CLOSE;

    private String randomId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public GroupStateEnum getState() {
        return state;
    }

    public void setState(GroupStateEnum state) {
        this.state = state;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public GroupExtendConfig getExtendConfig() {
        return extendConfig;
    }

    public void setExtendConfig(GroupExtendConfig extendConfig) {
        this.extendConfig = extendConfig;
    }

    public SwitchStateEnum getDebugMode() {
        return debugMode;
    }

    public void setDebugMode(SwitchStateEnum debugMode) {
        this.debugMode = debugMode;
    }

    public LocalDateTime getRefreshTime() {
        return refreshTime;
    }

    public void setRefreshTime(LocalDateTime refreshTime) {
        this.refreshTime = refreshTime;
    }

    public String getRandomId() {
        return randomId;
    }

    public void setRandomId(String randomId) {
        this.randomId = randomId;
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
    }

    public Integer getDataVersion() {
        return dataVersion;
    }

    public void setDataVersion(Integer dataVersion) {
        this.dataVersion = dataVersion;
    }

    public LimitingParam getLimitingParam() {
        return limitingParam;
    }

    public void setLimitingParam(LimitingParam limitingParam) {
        this.limitingParam = limitingParam;
    }

    public DebugParam getDebugParam() {
        return debugParam;
    }

    public void setDebugParam(DebugParam debugParam) {
        this.debugParam = debugParam;
    }
}
