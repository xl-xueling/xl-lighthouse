package com.dtstep.lighthouse.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

public enum ViewStateEnum {

    UNPUBLISHED(0),

    PUBLISHED(1),

    ;

    @JsonValue
    private Integer state;

    ViewStateEnum(int state){
        this.state = state;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    @JsonCreator
    public static ViewStateEnum forValue(int state){
        ViewStateEnum[] values = ViewStateEnum.values();
        return Stream.of(values).filter(it -> it.getState() == state).findAny().orElse(null);
    }
}
