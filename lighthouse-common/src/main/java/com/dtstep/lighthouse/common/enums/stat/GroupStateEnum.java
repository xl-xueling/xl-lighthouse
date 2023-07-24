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


public enum  GroupStateEnum {

    PEND(0,"已创建"),

    RUNNING(1,"正常"),

    STOPPED(2,"已停止"),

    LIMITING(3,"统计限流"),

    DELETED(4,"已删除"),

    FROZEN(5,"已冻结"),

    ;

    private int state;

    private String desc;

    GroupStateEnum(int state,String desc){
        this.state = state;
        this.desc = desc;
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

    public static GroupStateEnum getStateEnum(int state){
        for(GroupStateEnum groupStateEnum : GroupStateEnum.values()){
            if(groupStateEnum.getState() == state){
                return groupStateEnum;
            }
        }
        return null;
    }
}
