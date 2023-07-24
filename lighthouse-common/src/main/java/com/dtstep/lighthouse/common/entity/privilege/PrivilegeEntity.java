package com.dtstep.lighthouse.common.entity.privilege;
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

import com.dtstep.lighthouse.common.entity.annotation.DBNameAnnotation;
import com.dtstep.lighthouse.common.enums.role.PrivilegeTypeEnum;
import com.dtstep.lighthouse.common.entity.annotation.DBColumnAnnotation;

import java.io.Serializable;
import java.util.Date;


@DBNameAnnotation(name="ldp_privilege")
public class PrivilegeEntity implements Serializable {

    private static final long serialVersionUID = -5222509820346701485L;

    @DBColumnAnnotation(basic="id")
    private int id;

    @DBColumnAnnotation(basic="privilege_type")
    private int privilegeType;

    private PrivilegeTypeEnum privilegeTypeEnum;

    @DBColumnAnnotation(basic="relation_a")
    private int relationA;

    @DBColumnAnnotation(basic="relation_b")
    private int relationB;

    @DBColumnAnnotation(basic="create_time")
    private Date createTime;

    public PrivilegeTypeEnum getRoleTypeEnum() {
        return privilegeTypeEnum;
    }

    public void setRoleTypeEnum(PrivilegeTypeEnum privilegeTypeEnum) {
        this.privilegeType = privilegeTypeEnum.getPrivilegeType();
        this.privilegeTypeEnum = privilegeTypeEnum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPrivilegeType() {
        return privilegeType;
    }

    public void setPrivilegeType(int privilegeType) {
        this.privilegeType = privilegeType;
    }

    public int getRelationA() {
        return relationA;
    }

    public void setRelationA(int relationA) {
        this.relationA = relationA;
    }

    public int getRelationB() {
        return relationB;
    }

    public void setRelationB(int relationB) {
        this.relationB = relationB;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
