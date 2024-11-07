package com.dtstep.lighthouse.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

public enum NumberCompareType {

    GT(1),

    GE(2),

    LT(3),

    LE(4),

    EQ(5),

    ;

    @JsonValue
    private Integer type;

    NumberCompareType(int type){
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @JsonCreator
    public static NumberCompareType forValue(int type){
        NumberCompareType[] values = NumberCompareType.values();
        return Stream.of(values).filter(it -> it.getType() == type).findAny().orElse(null);
    }

}
