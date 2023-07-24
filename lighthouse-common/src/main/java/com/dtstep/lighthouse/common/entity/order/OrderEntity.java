package com.dtstep.lighthouse.common.entity.order;
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

@DBNameAnnotation(name="ldp_order")
public class OrderEntity implements Serializable, IListEntity {

    private static final long serialVersionUID = -8244408692770246222L;

    @DBColumnAnnotation(basic="id")
    private int id;

    @DBColumnAnnotation(basic="user_id")
    private int userId;

    @DBColumnAnnotation(basic="user_name")
    private String userName;

    @DBColumnAnnotation(basic="order_type")
    private int orderType;

    @DBColumnAnnotation(basic="privilege_kid")
    private int privilegeKId;

    @DBColumnAnnotation(basic="privilege_type")
    private int privilegeType;

    @DBColumnAnnotation(basic="approve_user")
    private int approveUserId;

    @DBColumnAnnotation(basic="state")
    private int state;

    @DBColumnAnnotation(basic="hash")
    private String hash;

    @DBColumnAnnotation(basic="params")
    private String params;

    @DBColumnAnnotation(basic="desc")
    private String desc;

    @DBColumnAnnotation(basic="create_time")
    private Date createTime;

    @DBColumnAnnotation(basic="process_time")
    private Date processTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public int getPrivilegeKId() {
        return privilegeKId;
    }

    public void setPrivilegeKId(int privilegeKId) {
        this.privilegeKId = privilegeKId;
    }

    public int getPrivilegeType() {
        return privilegeType;
    }

    public void setPrivilegeType(int privilegeType) {
        this.privilegeType = privilegeType;
    }

    public int getApproveUserId() {
        return approveUserId;
    }

    public void setApproveUserId(int approveUserId) {
        this.approveUserId = approveUserId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getProcessTime() {
        return processTime;
    }

    public void setProcessTime(Date processTime) {
        this.processTime = processTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
