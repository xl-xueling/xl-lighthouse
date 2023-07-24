package com.dtstep.lighthouse.common.entity.components;
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
import com.dtstep.lighthouse.common.entity.annotation.valid.S_Integer;
import com.dtstep.lighthouse.common.entity.annotation.valid.S_NotNull;
import com.dtstep.lighthouse.common.entity.list.IListEntity;

import java.io.Serializable;
import java.util.Date;

@DBNameAnnotation(name="ldp_components")
public class ComponentsEntity implements Serializable, IListEntity {

    private static final long serialVersionUID = -3814381045541444435L;

    @DBColumnAnnotation(basic="id")
    private int id;

    @DBColumnAnnotation(basic="title")
    private String title;

    @DBColumnAnnotation(basic="components_type")
    private int type;

    @DBColumnAnnotation(basic="data")
    private String data;

    @S_NotNull
    @S_Integer
    @DBColumnAnnotation(basic="private_flag")
    private int privateFlag;

    @DBColumnAnnotation(basic="user_id")
    private int userId;

    @DBColumnAnnotation(basic="create_time")
    private Date createTime;

    @DBColumnAnnotation(basic="level")
    private int level;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPrivateFlag() {
        return privateFlag;
    }

    public void setPrivateFlag(int privateFlag) {
        this.privateFlag = privateFlag;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
