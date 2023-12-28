package com.dtstep.lighthouse.insights.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum OrderTypeEnum {

    PROJECT_ACCESS(1),

    STAT_ACCESS(2),

    GROUP_THRESHOLD_ADJUST(3),

    STAT_ITEM_APPROVE(4),

    ;

    OrderTypeEnum(int orderType){
        this.orderType = orderType;
    }

    @JsonValue
    private int orderType;

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }
}
