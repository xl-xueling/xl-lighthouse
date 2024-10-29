package com.dtstep.lighthouse.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

public enum AlarmStateEnum {

    DISABLE(0),

    ENABLE(1),

    ;

    AlarmStateEnum(int state){
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

    @JsonCreator
    public static AlarmStateEnum forValue(int state){
        AlarmStateEnum[] values = AlarmStateEnum.values();
        return Stream.of(values).filter(it -> it.getState() == state).findAny().orElse(null);
    }
}
