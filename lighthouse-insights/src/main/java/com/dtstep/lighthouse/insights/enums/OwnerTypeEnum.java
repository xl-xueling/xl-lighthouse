package com.dtstep.lighthouse.insights.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

public enum OwnerTypeEnum {

    USER(1),

    DEPARTMENT(2),

    ;

    OwnerTypeEnum(int ownerType){
        this.ownerType = ownerType;
    }

    @JsonValue
    private int ownerType;

    public int getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(int ownerType) {
        this.ownerType = ownerType;
    }


    @JsonCreator
    public static OwnerTypeEnum forValue(int value){
        OwnerTypeEnum[] values = OwnerTypeEnum.values();
        return Stream.of(values).filter(it -> it.getOwnerType() == value).findAny().orElse(null);
    }
}
