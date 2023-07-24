package com.dtstep.lighthouse.common.entity.stat;
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
import com.dtstep.lighthouse.common.entity.annotation.DBColumnAnnotation;
import com.dtstep.lighthouse.common.entity.annotation.DBNameAnnotation;
import com.dtstep.lighthouse.common.entity.list.IListEntity;

import java.io.Serializable;
import java.util.Date;

@DBNameAnnotation(name="ldp_stat_item")
public class StatEntity implements Serializable , IListEntity {

    private static final long serialVersionUID = 9177463326135012233L;

    @DBColumnAnnotation(basic="id")
    private int id;

    @DBColumnAnnotation(basic="template")
    private String template;

    @DBColumnAnnotation(basic="project_id")
    private int projectId;

    @DBColumnAnnotation(basic="group_id")
    private int groupId;

    @DBColumnAnnotation(extend="token")
    private String token;

    @DBColumnAnnotation(extend="group_columns")
    private String groupColumns;

    @DBColumnAnnotation(basic="create_time")
    private Date createTime;

    @DBColumnAnnotation(basic="update_time")
    private Date updateTime;

    @DBColumnAnnotation(basic="title")
    private String title;

    @DBColumnAnnotation(basic="data_expire")
    private long dataExpire;

    @DBColumnAnnotation(basic="res_meta")
    private int resMeta;

    @DBColumnAnnotation(basic="time_param")
    private String timeParam;

    @DBColumnAnnotation(basic="display_type")
    private int displayType;

    @DBColumnAnnotation(basic="create_user")
    private int createUser;

    @DBColumnAnnotation(basic="filter_config")
    private String filterConfig;

    @DBColumnAnnotation(basic="data_version")
    private int dataVersion;

    @DBColumnAnnotation(basic="state")
    private int state;

    @DBColumnAnnotation(extend="stat_type")
    private int statType;

    @DBColumnAnnotation(basic="sequence_flag")
    private int sequenceFlag;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getDataExpire() {
        return dataExpire;
    }

    public void setDataExpire(long dataExpire) {
        this.dataExpire = dataExpire;
    }

    public int getResMeta() {
        return resMeta;
    }

    public void setResMeta(int resMeta) {
        this.resMeta = resMeta;
    }

    public String getTimeParam() {
        return timeParam;
    }

    public void setTimeParam(String timeParam) {
        this.timeParam = timeParam;
    }

    public int getDisplayType() {
        return displayType;
    }

    public void setDisplayType(int displayType) {
        this.displayType = displayType;
    }

    public int getCreateUser() {
        return createUser;
    }

    public void setCreateUser(int createUser) {
        this.createUser = createUser;
    }

    public String getFilterConfig() {
        return filterConfig;
    }

    public void setFilterConfig(String filterConfig) {
        this.filterConfig = filterConfig;
    }

    public int getDataVersion() {
        return dataVersion;
    }

    public void setDataVersion(int dataVersion) {
        this.dataVersion = dataVersion;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getStatType() {
        return statType;
    }

    public void setStatType(int statType) {
        this.statType = statType;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getGroupColumns() {
        return groupColumns;
    }

    public void setGroupColumns(String groupColumns) {
        this.groupColumns = groupColumns;
    }

    public int getSequenceFlag() {
        return sequenceFlag;
    }

    public void setSequenceFlag(int sequenceFlag) {
        this.sequenceFlag = sequenceFlag;
    }
}
