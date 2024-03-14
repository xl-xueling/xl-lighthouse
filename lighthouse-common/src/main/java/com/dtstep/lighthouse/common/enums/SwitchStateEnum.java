package com.dtstep.lighthouse.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

public enum SwitchStateEnum {

    CLOSE(0),

    OPEN(1),

    ;

    SwitchStateEnum(int state){
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
    public static SwitchStateEnum forValue(int state){
        SwitchStateEnum[] values = SwitchStateEnum.values();
        return Stream.of(values).filter(it -> it.getState() == state).findAny().orElse(null);
    }
}
