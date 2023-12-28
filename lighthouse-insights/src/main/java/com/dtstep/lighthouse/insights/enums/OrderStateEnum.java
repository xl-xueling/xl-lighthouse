package com.dtstep.lighthouse.insights.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum OrderStateEnum {

    PENDING(1),

    APPROVED(2),

    REJECTED(3),

    RETRACTED(4),

    ;

    OrderStateEnum(int state){
        this.state = state;
    }

    @JsonValue
    private int state;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
