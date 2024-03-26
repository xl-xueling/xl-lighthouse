package com.dtstep.lighthouse.insights.vo;
/*
 * Copyright (C) 2022-2024 XueLing.雪灵
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
import com.dtstep.lighthouse.common.modal.Order;
import com.dtstep.lighthouse.common.modal.User;
import com.dtstep.lighthouse.common.util.BeanCopyUtil;
import com.dtstep.lighthouse.common.modal.PermissionEnum;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OrderVO extends Order {

    private User user;

    private String desc;

    private HashMap<Integer, List<User>> adminsMap;

    private List<OrderDetailVO> orderDetails;

    private Object extend;

    public OrderVO(Order order){
        assert order != null;
        BeanCopyUtil.copy(order,this);
    }

    private Set<PermissionEnum> permissions = new HashSet<>();

    public Set<PermissionEnum> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<PermissionEnum> permissions) {
        this.permissions = permissions;
    }

    public void addPermission(PermissionEnum permission){
        if(permission != null){
            permissions.add(permission);
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public HashMap<Integer, List<User>> getAdminsMap() {
        return adminsMap;
    }

    public void setAdminsMap(HashMap<Integer, List<User>> adminsMap) {
        this.adminsMap = adminsMap;
    }

    public List<OrderDetailVO> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetailVO> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public Object getExtend() {
        return extend;
    }

    public void setExtend(Object extend) {
        this.extend = extend;
    }
}
