package com.dtstep.lighthouse.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

public enum AlarmMatchEnum {

    MATCH_ANY(1),

    MATCH_ALL(2),

    ;

    AlarmMatchEnum(int type){
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

    @JsonCreator
    public static AlarmMatchEnum forValue(int type){
        AlarmMatchEnum[] values = AlarmMatchEnum.values();
        return Stream.of(values).filter(it -> it.type == type).findAny().orElse(null);
    }
}
