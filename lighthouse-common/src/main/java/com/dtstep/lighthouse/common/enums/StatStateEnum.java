package com.dtstep.lighthouse.common.enums;
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
import com.fasterxml.jackson.annotation.JsonValue;

public enum StatStateEnum {

    PENDING_APPROVE(0,"待审核"),

    RUNNING(1,"运行中"),

    STOPPED(2,"已停止"),

    LIMITING(3,"已限流"),

    @Deprecated
    DELETED(4,"已删除"),

    REJECTED(5,"审核拒绝"),

    FROZEN(6,"已冻结"),

    INVALID(7,"无效")

    ;

    @JsonValue
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
