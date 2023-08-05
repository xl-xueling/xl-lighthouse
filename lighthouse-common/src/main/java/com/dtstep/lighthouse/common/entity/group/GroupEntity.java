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
import com.dtstep.lighthouse.common.entity.annotation.DBColumnAnnotation;
import com.dtstep.lighthouse.common.entity.annotation.DBNameAnnotation;
import com.dtstep.lighthouse.common.entity.annotation.valid.S_Illegal;
import com.dtstep.lighthouse.common.entity.annotation.valid.S_Integer;
import com.dtstep.lighthouse.common.entity.annotation.valid.S_Length;
import com.dtstep.lighthouse.common.entity.annotation.valid.S_NotNull;

import java.io.Serializable;
import java.util.Date;

@DBNameAnnotation(name="ldp_stat_group")
public class GroupEntity implements Serializable  {

    private static final long serialVersionUID = 2783322605348430486L;

    @DBColumnAnnotation(basic="id")
    private int id;

    @S_NotNull
    @S_Illegal
    @S_Length(min = 5,max = 30)
    @DBColumnAnnotation(basic="token")
    private String token;

    @S_NotNull
    @S_Integer
    @DBColumnAnnotation(basic="project_id")
    private int projectId;

    @DBColumnAnnotation(basic="secret_key")
    private String secretKey;

    @S_NotNull
    @DBColumnAnnotation(basic="columns")
    private String columns;

    @S_NotNull
    @S_Integer
    @DBColumnAnnotation(basic="stat_type")
    private int statType;

    @DBColumnAnnotation(basic="create_time")
    private Date createTime;

    @DBColumnAnnotation(basic="update_time")
    private Date updateTime;

    @DBColumnAnnotation(basic="refresh_time")
    private Date refreshTime;

    @DBColumnAnnotation(basic="debug_mode")
    private int debugMode;

    @DBColumnAnnotation(basic="state")
    private int state;

    @DBColumnAnnotation(basic="create_user")
    private int createUser;

    @DBColumnAnnotation(basic="limited_threshold")
    private String limitedThreshold;

    @DBColumnAnnotation(basic="debug_params")
    private String debugParams;

    @S_Length(max = 300)
    @DBColumnAnnotation(basic="remark")
    private String remark;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getColumns() {
        return columns;
    }

    public void setColumns(String columns) {
        this.columns = columns;
    }

    public int getStatType() {
        return statType;
    }

    public void setStatType(int statType) {
        this.statType = statType;
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

    public int getDebugMode() {
        return debugMode;
    }

    public void setDebugMode(int debugMode) {
        this.debugMode = debugMode;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getLimitedThreshold() {
        return limitedThreshold;
    }

    public void setLimitedThreshold(String limitedThreshold) {
        this.limitedThreshold = limitedThreshold;
    }

    public int getCreateUser() {
        return createUser;
    }

    public void setCreateUser(int createUser) {
        this.createUser = createUser;
    }

    public String getDebugParams() {
        return debugParams;
    }

    public void setDebugParams(String debugParams) {
        this.debugParams = debugParams;
    }

    public Date getRefreshTime() {
        return refreshTime;
    }

    public void setRefreshTime(Date refreshTime) {
        this.refreshTime = refreshTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
