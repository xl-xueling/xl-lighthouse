package com.dtstep.lighthouse.insights.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

public enum ApproveStateEnum {

    WAIT(0),

    PENDING(1),

    APPROVED(2),

    REJECTED(3),

    RETRACTED(4),

    SUSPEND(5),

    ;

    @JsonValue
    private Integer state;

    ApproveStateEnum(int state){
        this.state = state;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    @JsonCreator
    public static ApproveStateEnum forValue(int state){
        ApproveStateEnum[] values = ApproveStateEnum.values();
        return Stream.of(values).filter(it -> it.getState() == state).findAny().orElse(null);
    }
}
