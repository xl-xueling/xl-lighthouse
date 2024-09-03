package com.dtstep.lighthouse.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

public enum RollbackStateEnum {

    UNPUBLISHED(0),

    PUBLISHED(1),

    ;

    @JsonValue
    private Integer state;

    RollbackStateEnum(int state){
        this.state = state;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    @JsonCreator
    public static RollbackStateEnum forValue(int state){
        RollbackStateEnum[] values = RollbackStateEnum.values();
        return Stream.of(values).filter(it -> it.getState() == state).findAny().orElse(null);
    }
}
