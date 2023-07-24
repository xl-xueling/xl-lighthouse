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
public enum ComponentsTypeEnum {

    RatioSelectionBox(1,"i18n(ldp_i18n_components_common_1002)","[1]"),

    MultipleSelectionBox(2,"i18n(ldp_i18n_components_common_1003)","[1]"),

    CascadeRatioSelectionBox(3,"i18n(ldp_i18n_components_common_1004)","[2-3]"),

    CascadeMultipleSelectionBox(4,"i18n(ldp_i18n_components_common_1005)","[2-3]"),

    InputBox(5,"i18n(ldp_i18n_components_common_1001)"),

    ;
    private int componentsType;

    private String desc;

    private String levelPattern;

    ComponentsTypeEnum(int componentsType, String desc){
        this.componentsType = componentsType;
        this.desc = desc;
    }

    ComponentsTypeEnum(int componentsType, String desc,String levelPattern){
        this.componentsType = componentsType;
        this.desc = desc;
        this.levelPattern = levelPattern;
    }

    public int getComponentsType() {
        return componentsType;
    }

    public void setComponentsType(int componentsType) {
        this.componentsType = componentsType;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLevelPattern() {
        return levelPattern;
    }

    public void setLevelPattern(String levelPattern) {
        this.levelPattern = levelPattern;
    }

    public static ComponentsTypeEnum getComponentsType(int componentsType){
        for(ComponentsTypeEnum componentsTypeEnum : ComponentsTypeEnum.values()){
            if(componentsTypeEnum.getComponentsType() == componentsType){
                return componentsTypeEnum;
            }
        }
        return null;
    }


}
