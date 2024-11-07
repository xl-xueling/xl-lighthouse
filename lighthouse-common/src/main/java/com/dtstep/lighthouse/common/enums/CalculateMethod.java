package com.dtstep.lighthouse.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

public enum CalculateMethod {

    ADD(1),

    SUB(2),

    MUL(3),

    DIVIDE(4),

    MAX(5),

    MIN(6),

    AVG(7),

    ;

    @JsonValue
    private Integer type;

    CalculateMethod(int type){
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @JsonCreator
    public static CalculateMethod forValue(int type){
        CalculateMethod[] values = CalculateMethod.values();
        return Stream.of(values).filter(it -> it.getType() == type).findAny().orElse(null);
    }
}
