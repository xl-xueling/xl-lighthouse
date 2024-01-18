package com.dtstep.lighthouse.common.enums;
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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;


public enum UserStateEnum {

    /**
     * 待审核用户
     */
    USER_PEND(0),

    /**
     * 已删除用户
     */
    USER_DELETED(1),

    /**
     * 正常用户
     */
    USER_NORMAL(2),

    /**
     * 冻结用户
     */
    USER_FROZEN(3),

    /**
     * 审核未通过
     */
    USER_REJECT(4),

    ;

    @JsonValue
    private int state;

    UserStateEnum(int state){
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @JsonCreator
    public static UserStateEnum forValue(int state){
        UserStateEnum[] values = UserStateEnum.values();
        return Stream.of(values).filter(it -> it.getState() == state).findAny().orElse(null);
    }
}
