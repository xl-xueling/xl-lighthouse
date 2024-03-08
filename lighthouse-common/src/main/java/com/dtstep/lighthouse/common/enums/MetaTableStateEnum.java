package com.dtstep.lighthouse.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.stream.Stream;

public enum MetaTableStateEnum {

    VALID(1),

    INVALID(2),

    ;

    private int state;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    MetaTableStateEnum(int state){
        this.state = state;
    }

    @JsonCreator
    public static MetaTableStateEnum forValue(int state){
        MetaTableStateEnum[] values = MetaTableStateEnum.values();
        return Stream.of(values).filter(it -> it.getState() == state).findAny().orElse(null);
    }
}
