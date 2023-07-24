package com.dtstep.lighthouse.common.entity.user;
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
import com.dtstep.lighthouse.common.entity.annotation.valid.*;
import com.dtstep.lighthouse.common.entity.list.IListEntity;

import java.io.Serializable;
import java.util.Date;


@DBNameAnnotation(name="ldp_user")
public class UserEntity implements Serializable, IListEntity {

    private static final long serialVersionUID = 6684609943914153311L;

    @S_NotNull
    @S_Integer
    @DBColumnAnnotation(basic="id")
    private int id;

    @S_NotNull
    @S_Length(min = 4,max = 20)
    @S_Pattern(pattern = "[a-zA-Z0-9_]+")
    @DBColumnAnnotation(basic="user_name")
    private String userName;

    @S_Length(min = 4,max = 20)
    @DBColumnAnnotation(basic="phone")
    private String phone;

    @S_NotNull
    @S_Pattern(pattern = "^([a-fA-F0-9]{32})$")
    @DBColumnAnnotation(basic="password")
    private String password;

    @S_NotNull
    @S_Integer
    @DBColumnAnnotation(basic="department_id")
    private int departmentId;

    @DBColumnAnnotation(basic="create_time")
    private Date createTime;

    @DBColumnAnnotation(basic="last_time")
    private Date lastTime;

    @S_Email
    @DBColumnAnnotation(basic="email")
    private String email;

    @DBColumnAnnotation(basic="state")
    private int state;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public Date getLastTime() {
        return lastTime;
    }

    public void setLastTime(Date lastTime) {
        this.lastTime = lastTime;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
