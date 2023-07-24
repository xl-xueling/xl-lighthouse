package com.dtstep.lighthouse.common.enums.order;
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
public enum OrderTypeEnum {

    PROJECT_ACCESS(1,"i18n(ldp_i18n_order_common_1001)"),

    STAT_ACCESS(2,"i18n(ldp_i18n_order_common_1002)"),

    SITEMAP_ACCESS(3,"i18n(ldp_i18n_order_common_1003)"),

    GROUP_THRESHOLD_ADJUST(4,"i18n(ldp_i18n_order_common_1004)"),

    STAT_ITEM_APPROVE(5,"i18n(ldp_i18n_order_common_1005)"),

    PUBLISH_DATA_TO_SITEMAP(6,"i18n(ldp_i18n_order_common_1006)"),

    ;

    private int type;

    private String desc;

    OrderTypeEnum(int type,String desc){
        this.type = type;
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static OrderTypeEnum getOrderType(int orderType){
        for(OrderTypeEnum orderTypeEnum : OrderTypeEnum.values()){
            if(orderTypeEnum.getType() == orderType){
                return orderTypeEnum;
            }
        }
        return null;
    }
}
