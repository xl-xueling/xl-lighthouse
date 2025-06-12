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
import com.dtstep.lighthouse.common.annotation.BLengthValidation;
import com.dtstep.lighthouse.common.enums.PrivateTypeEnum;
import com.dtstep.lighthouse.common.modal.Project;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

public class ProjectCreateParam {

    @NotEmpty
    @Pattern(regexp = "^[\\u3010\\u3011\\uFF08\\uFF09\\u4E00-\\u9FA5a-zA-Z0-9_\\-()\\[\\]#\\s]+$")
    @BLengthValidation(min = 5,max = 30)
    private String title;

    @NotNull
    private Integer departmentId;

    @NotNull
    private PrivateTypeEnum privateType;

    @NotEmpty
    private String desc;

    private List<Integer> usersPermission;

    private List<Integer> departmentsPermission;

    public List<Integer> getUsersPermission() {
        return usersPermission;
    }

    public void setUsersPermission(List<Integer> usersPermission) {
        this.usersPermission = usersPermission;
    }

    public List<Integer> getDepartmentsPermission() {
        return departmentsPermission;
    }

    public void setDepartmentsPermission(List<Integer> departmentsPermission) {
        this.departmentsPermission = departmentsPermission;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public PrivateTypeEnum getPrivateType() {
        return privateType;
    }

    public void setPrivateType(PrivateTypeEnum privateType) {
        this.privateType = privateType;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
