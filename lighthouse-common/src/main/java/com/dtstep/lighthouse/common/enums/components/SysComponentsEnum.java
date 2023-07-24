package com.dtstep.lighthouse.common.enums.components;
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
public enum SysComponentsEnum {

    SYS_COMP_INPUT(1,ComponentsTypeEnum.InputBox),

    SYS_COMP_SINGLE_SELECTOR(2,ComponentsTypeEnum.RatioSelectionBox),

    SYS_COMP_MULTI_SELECTOR(3,ComponentsTypeEnum.MultipleSelectionBox),

    ;
    private int componentsId;

    private ComponentsTypeEnum componentsTypeEnum;

    SysComponentsEnum(int componentsId,ComponentsTypeEnum componentsTypeEnum){
        this.componentsId = componentsId;
        this.componentsTypeEnum = componentsTypeEnum;
    }

    public int getComponentsId() {
        return componentsId;
    }

    public void setComponentsId(int componentsId) {
        this.componentsId = componentsId;
    }

    public ComponentsTypeEnum getComponentsTypeEnum() {
        return componentsTypeEnum;
    }

    public void setComponentsTypeEnum(ComponentsTypeEnum componentsTypeEnum) {
        this.componentsTypeEnum = componentsTypeEnum;
    }

    public static SysComponentsEnum getById(int componentsId){
        for(SysComponentsEnum sysComponentsEnum : SysComponentsEnum.values()){
            if(sysComponentsEnum.getComponentsId() == componentsId){
                return sysComponentsEnum;
            }
        }
        return null;
    }

    public static boolean isSysComponents(int componentsId){
        for(SysComponentsEnum sysComponentsEnum : SysComponentsEnum.values()){
            if(sysComponentsEnum.getComponentsId() == componentsId){
                return true;
            }
        }
        return false;
    }
}
