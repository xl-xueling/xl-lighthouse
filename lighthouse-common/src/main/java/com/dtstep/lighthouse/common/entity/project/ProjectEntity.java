package com.dtstep.lighthouse.common.entity.project;
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
import com.dtstep.lighthouse.common.entity.list.IListEntity;

import java.io.Serializable;
import java.util.Date;


@DBNameAnnotation(name="ldp_stat_project")
public class ProjectEntity implements Serializable, IListEntity {

    private static final long serialVersionUID = -2127414334727921735L;

    @S_Integer
    @DBColumnAnnotation(basic="id")
    private int id;

    @S_NotNull
    @S_Length(min = 4,max = 40)
    @S_Illegal
    @DBColumnAnnotation(basic="name")
    private String name;

    @S_NotNull
    @S_Integer
    @DBColumnAnnotation(basic="department_id")
    private int departmentId;

    @DBColumnAnnotation(basic="user_id")
    private int userId;

    @DBColumnAnnotation(basic="create_time")
    private Date createTime;

    @DBColumnAnnotation(basic="update_time")
    private Date updateTime;

    @S_NotNull
    @S_Integer
    @DBColumnAnnotation(basic="private_flag")
    private int privateFlag;

    @S_NotNull
    @S_Length(min = 5,max = 50)
    @DBColumnAnnotation(basic="desc")
    private String desc;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPrivateFlag() {
        return privateFlag;
    }

    public void setPrivateFlag(int privateFlag) {
        this.privateFlag = privateFlag;
    }
}
