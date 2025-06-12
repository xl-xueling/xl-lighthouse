package com.dtstep.lighthouse.insights.dto;
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
import com.dtstep.lighthouse.common.enums.RoleTypeEnum;

import javax.validation.constraints.NotNull;
import java.util.List;

public class PermissionGrantParam {

    @NotNull
    private Integer resourceId;

    @NotNull
    private RoleTypeEnum roleType;

    private List<Integer> usersPermissions;

    private List<Integer> departmentsPermissions;

    public List<Integer> getUsersPermissions() {
        return usersPermissions;
    }

    public void setUsersPermissions(List<Integer> usersPermissions) {
        this.usersPermissions = usersPermissions;
    }

    public List<Integer> getDepartmentsPermissions() {
        return departmentsPermissions;
    }

    public void setDepartmentsPermissions(List<Integer> departmentsPermissions) {
        this.departmentsPermissions = departmentsPermissions;
    }

    public Integer getResourceId() {
        return resourceId;
    }

    public void setResourceId(Integer resourceId) {
        this.resourceId = resourceId;
    }

    public RoleTypeEnum getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleTypeEnum roleType) {
        this.roleType = roleType;
    }
}
