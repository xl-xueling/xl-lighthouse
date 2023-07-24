package com.dtstep.lighthouse.common.enums.role;
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
public enum PrivilegeTypeEnum {

    ADMIN(1,"系统管理权限","i18n(ldp_i18n_privilege_apply_1013)"),

    USER(2,"系统访问权限","i18n(ldp_i18n_privilege_apply_1014)"),

    @Deprecated
    META_TABLE_ADMIN(3,"元数据管理权限","i18n(ldp_i18n_privilege_apply_1015)"),

    @Deprecated
    META_TABLE_USER(4,"元数据访问权限","i18n(ldp_i18n_privilege_apply_1016)"),

    STAT_PROJECT_ADMIN(5,"统计工程管理权限","i18n(ldp_i18n_privilege_apply_1017)"),

    STAT_PROJECT_USER(6,"统计工程访问权限","i18n(ldp_i18n_privilege_apply_1018)"),

    STAT_ITEM_USER(7,"统计项访问权限","i18n(ldp_i18n_privilege_apply_1019)"),

    @Deprecated
    STAT_ITEM_ADMIN(8,"统计项管理权限","i18n(ldp_i18n_privilege_apply_1020)"),

    SITE_MAP_USER(9,"数据地图访问权限","i18n(ldp_i18n_privilege_apply_1021)"),

    SITE_MAP_ADMIN(10,"数据地图管理权限","i18n(ldp_i18n_privilege_apply_1022)")
    ;

    private int privilegeType;

    private String desc;

    private String i18nCode;

    public static PrivilegeTypeEnum getPrivilegeTypeEnum(int privilegeType){
        for(PrivilegeTypeEnum privilegeTypeEnum : PrivilegeTypeEnum.values()){
            if(privilegeTypeEnum.getPrivilegeType() == privilegeType){
                return privilegeTypeEnum;
            }
        }
        return null;
    }

    PrivilegeTypeEnum(int privilegeType, String desc){
        this.privilegeType = privilegeType;
        this.desc = desc;
    }

    PrivilegeTypeEnum(int privilegeType, String desc,String i18nCode){
        this.privilegeType = privilegeType;
        this.desc = desc;
        this.i18nCode = i18nCode;
    }

    public int getPrivilegeType() {
        return privilegeType;
    }

    public void setPrivilegeType(int privilegeType) {
        this.privilegeType = privilegeType;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getI18nCode() {
        return i18nCode;
    }

    public void setI18nCode(String i18nCode) {
        this.i18nCode = i18nCode;
    }
}
