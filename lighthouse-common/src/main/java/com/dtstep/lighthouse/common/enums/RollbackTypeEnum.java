package com.dtstep.lighthouse.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

public enum RollbackTypeEnum {

    VISUALIZATION_DESIGN(1),

    ;

    RollbackTypeEnum(int type){
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
    public static RollbackTypeEnum forValue(int type){
        RollbackTypeEnum[] values = RollbackTypeEnum.values();
        return Stream.of(values).filter(it -> it.getType() == type).findAny().orElse(null);
    }
}
