package com.dtstep.lighthouse.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

public enum ColumnTypeEnum {

    STRING("string"),

    NUMBER("number"),

    ;


    @JsonValue
    private String type;

    ColumnTypeEnum(String type){
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @JsonCreator
    public static ColumnTypeEnum forValue(String value){
        ColumnTypeEnum[] values = ColumnTypeEnum.values();
        return Stream.of(values).filter(it -> it.getType().equals(value)).findAny().orElse(null);
    }
}
