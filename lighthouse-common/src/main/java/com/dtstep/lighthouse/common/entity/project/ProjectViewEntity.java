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

import com.dtstep.lighthouse.common.enums.role.PrivilegeTypeEnum;
import com.dtstep.lighthouse.common.entity.user.UserEntity;
import com.dtstep.lighthouse.common.util.BeanCopyUtil;
import java.util.List;


public class ProjectViewEntity extends ProjectEntity {

    private static final long serialVersionUID = -1661670143953755109L;

    private List<UserEntity> admins;

    private String departmentName;

    private String fullDepartmentName;

    private PrivilegeTypeEnum privilegeTypeEnum;

    private boolean isApply;

    private boolean isFavorite;

    private List<Integer> privilegeIds;

    public ProjectViewEntity(ProjectEntity projectEntity){
        assert projectEntity != null;
        BeanCopyUtil.copy(projectEntity,this);
    }

    public List<UserEntity> getAdmins() {
        return admins;
    }

    public void setAdmins(List<UserEntity> admins) {
        this.admins = admins;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public PrivilegeTypeEnum getRoleTypeEnum() {
        return privilegeTypeEnum;
    }

    public void setRoleTypeEnum(PrivilegeTypeEnum privilegeTypeEnum) {
        this.privilegeTypeEnum = privilegeTypeEnum;
    }

    public boolean isApply() {
        return isApply;
    }

    public void setApply(boolean apply) {
        isApply = apply;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public PrivilegeTypeEnum getPrivilegeTypeEnum() {
        return privilegeTypeEnum;
    }

    public void setPrivilegeTypeEnum(PrivilegeTypeEnum privilegeTypeEnum) {
        this.privilegeTypeEnum = privilegeTypeEnum;
    }

    public List<Integer> getPrivilegeIds() {
        return privilegeIds;
    }

    public void setPrivilegeIds(List<Integer> privilegeIds) {
        this.privilegeIds = privilegeIds;
    }

    public String getFullDepartmentName() {
        return fullDepartmentName;
    }

    public void setFullDepartmentName(String fullDepartmentName) {
        this.fullDepartmentName = fullDepartmentName;
    }
}
