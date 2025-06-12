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
import com.dtstep.lighthouse.common.enums.PrivateTypeEnum;
import com.dtstep.lighthouse.common.enums.StatStateEnum;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Stat implements Serializable {

    private Integer id;

    private String title;

    private String template;

    private String timeparam;

    private Long expired;

    private StatStateEnum state;

    private Integer projectId;

    private Integer groupId;

    private String projectTitle;

    private String token;

    private Integer departmentId;

    private String groupColumns;

    private RenderConfig renderConfig = new RenderConfig();

    private LimitingParam limitingParam;

    private LocalDateTime createTime;

    private LocalDateTime refreshTime;

    private Integer metaId;

    private LocalDateTime updateTime;

    private String desc;

    private String randomId;

    private PrivateTypeEnum privateType;

    private Integer dataVersion = 0;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getTimeparam() {
        return timeparam;
    }

    public void setTimeparam(String timeparam) {
        this.timeparam = timeparam;
    }

    public Long getExpired() {
        return expired;
    }

    public void setExpired(Long expired) {
        this.expired = expired;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public StatStateEnum getState() {
        return state;
    }

    public void setState(StatStateEnum state) {
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

    public String getRandomId() {
        return randomId;
    }

    public void setRandomId(String randomId) {
        this.randomId = randomId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public RenderConfig getRenderConfig() {
        return renderConfig;
    }

    public void setRenderConfig(RenderConfig renderConfig) {
        this.renderConfig = renderConfig;
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public Integer getMetaId() {
        return metaId;
    }

    public void setMetaId(Integer metaId) {
        this.metaId = metaId;
    }

    public Integer getDataVersion() {
        return dataVersion;
    }

    public void setDataVersion(Integer dataVersion) {
        this.dataVersion = dataVersion;
    }

    public String getGroupColumns() {
        return groupColumns;
    }

    public void setGroupColumns(String groupColumns) {
        this.groupColumns = groupColumns;
    }

    public LocalDateTime getRefreshTime() {
        return refreshTime;
    }

    public void setRefreshTime(LocalDateTime refreshTime) {
        this.refreshTime = refreshTime;
    }

    public LimitingParam getLimitingParam() {
        return limitingParam;
    }

    public void setLimitingParam(LimitingParam limitingParam) {
        this.limitingParam = limitingParam;
    }

    public PrivateTypeEnum getPrivateType() {
        return privateType;
    }

    public void setPrivateType(PrivateTypeEnum privateType) {
        this.privateType = privateType;
    }
}
