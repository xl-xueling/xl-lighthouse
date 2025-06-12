package com.dtstep.lighthouse.insights.vo;
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
import com.dtstep.lighthouse.common.util.BeanCopyUtil;
import com.dtstep.lighthouse.common.modal.MetricBindElement;
import com.dtstep.lighthouse.common.modal.PermissionEnum;
import com.dtstep.lighthouse.common.modal.MetricSet;
import com.dtstep.lighthouse.common.modal.User;
import org.apache.commons.lang3.Validate;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MetricSetVO extends MetricSet {

    private List<User> admins;

    private List<MetricBindElement> bindElements;

    private boolean isCustomStructure = true;

    private User createUser;

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

    public MetricSetVO(MetricSet metricSet){
        Validate.notNull(metricSet);
        BeanCopyUtil.copy(metricSet,this);
    }

    public List<User> getAdmins() {
        return admins;
    }

    public void setAdmins(List<User> admins) {
        this.admins = admins;
    }

    public List<MetricBindElement> getBindElements() {
        return bindElements;
    }

    public void setBindElements(List<MetricBindElement> bindElements) {
        this.bindElements = bindElements;
    }

    public boolean isCustomStructure() {
        return isCustomStructure;
    }

    public void setCustomStructure(boolean customStructure) {
        isCustomStructure = customStructure;
    }

    public User getCreateUser() {
        return createUser;
    }

    public void setCreateUser(User createUser) {
        this.createUser = createUser;
    }
}
