package com.dtstep.lighthouse.common.enums.stat;
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


public enum  StatStateEnum {

    PENDING_APPROVE(0,"i18n(ldp_i18n_display_1066)"),

    RUNNING(1,"i18n(ldp_i18n_display_1067)"),

    STOPPED(2,"i18n(ldp_i18n_display_1068)"),

    LIMITING(3,"i18n(ldp_i18n_display_1069)"),

    @Deprecated
    DELETED(4,"i18n(ldp_i18n_display_1070)"),

    REJECTED(5,"i18n(ldp_i18n_display_1071)"),

    FROZEN(6,"i18n(ldp_i18n_display_1072)"),

    INVALID(7,"i18n(ldp_i18n_display_1077)")

    ;
    private int state;

    private String desc;

    StatStateEnum(int state){
        this.state = state;
    }

    StatStateEnum(int state,String desc){
        this.state = state;this.desc = desc;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static StatStateEnum getByState(int state){
        for(StatStateEnum statStateEnum : StatStateEnum.values()){
            if(state == statStateEnum.getState()){
                return statStateEnum;
            }
        }
        return null;
    }
}
