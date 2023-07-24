package com.dtstep.lighthouse.common.entity.department;
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


@DBNameAnnotation(name="ldp_department")
public class DepartmentEntity implements Serializable, IListEntity {

    private static final long serialVersionUID = 3700420311267418805L;

    @S_NotNull
    @S_Integer
    @DBColumnAnnotation(basic="id")
    private int id;

    @S_NotNull
    @S_Illegal
    @S_Length(min = 3,max = 30)
    @DBColumnAnnotation(basic="name")
    private String name;

    @DBColumnAnnotation(basic="level")
    private int level = 1;

    @S_NotNull
    @S_Integer
    @DBColumnAnnotation(basic="pid")
    private int pid;

    @DBColumnAnnotation(basic="fullpath")
    private String fullPath;

    @DBColumnAnnotation(basic="create_time")
    private Date createTime;

    @DBColumnAnnotation(basic="update_time")
    private Date updateTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getId() {
        return id;
    }

    public int getPid() {
        return pid;
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

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }


}
