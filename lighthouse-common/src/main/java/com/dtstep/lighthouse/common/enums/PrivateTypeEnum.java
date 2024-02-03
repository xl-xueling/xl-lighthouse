package com.dtstep.lighthouse.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

public enum PrivateTypeEnum {

    Private(0),

    Public(1),
    ;

    PrivateTypeEnum(int privateType){
        this.privateType = privateType;
    }

    @JsonValue
    private Integer privateType;

    public Integer getPrivateType() {
        return privateType;
    }

    public void setPrivateType(Integer privateType) {
        this.privateType = privateType;
    }

    @JsonCreator
    public static PrivateTypeEnum forValue(int privateType){
        PrivateTypeEnum[] values = PrivateTypeEnum.values();
        return Stream.of(values).filter(it -> it.getPrivateType() == privateType).findAny().orElse(null);
    }
}
