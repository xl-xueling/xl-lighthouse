package com.dtstep.lighthouse.insights.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

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


    @JsonCreator
    public static OrderTypeEnum forValue(int value){
        OrderTypeEnum[] values = OrderTypeEnum.values();
        return Stream.of(values).filter(it -> it.getOrderType() == value).findAny().orElse(null);
    }
}
