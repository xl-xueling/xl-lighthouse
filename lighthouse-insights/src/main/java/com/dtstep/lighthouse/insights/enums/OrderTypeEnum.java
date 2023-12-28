package com.dtstep.lighthouse.insights.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum OrderTypeEnum {

    PROJECT_ACCESS(1),

    STAT_ACCESS(2),

    GROUP_THRESHOLD_ADJUST(3),

    STAT_ITEM_APPROVE(4),


    ;

    OrderTypeEnum(int type){
        this.type = type;
    }

    @JsonValue
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
