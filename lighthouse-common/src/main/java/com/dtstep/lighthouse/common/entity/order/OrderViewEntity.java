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
import com.dtstep.lighthouse.common.util.BeanCopyUtil;
import com.dtstep.lighthouse.common.entity.user.UserEntity;

import java.util.List;

public class OrderViewEntity extends OrderEntity {

    private static final long serialVersionUID = -7305357033326057526L;

    private String orderTypeDesc;

    private String orderStateDesc;

    private List<UserEntity> admins;

    private UserEntity approveUserEntity;

    public OrderViewEntity(OrderEntity orderEntity){
        assert orderEntity != null;
        BeanCopyUtil.copy(orderEntity,this);
    }

    public String getOrderTypeDesc() {
        return orderTypeDesc;
    }

    public void setOrderTypeDesc(String orderTypeDesc) {
        this.orderTypeDesc = orderTypeDesc;
    }

    public List<UserEntity> getAdmins() {
        return admins;
    }

    public void setAdmins(List<UserEntity> admins) {
        this.admins = admins;
    }

    public String getOrderStateDesc() {
        return orderStateDesc;
    }

    public void setOrderStateDesc(String orderStateDesc) {
        this.orderStateDesc = orderStateDesc;
    }

    public UserEntity getApproveUserEntity() {
        return approveUserEntity;
    }

    public void setApproveUserEntity(UserEntity approveUserEntity) {
        this.approveUserEntity = approveUserEntity;
    }
}
