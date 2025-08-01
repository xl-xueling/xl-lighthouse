package com.dtstep.lighthouse.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

public enum AssetTypeEnum {

    BACKGROUND(1),

    DECORATION(2),

    HEADER(3),

    OTHER(99),

    ;

    AssetTypeEnum(int type){
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
    public static AssetTypeEnum forValue(int type){
        AssetTypeEnum[] values = AssetTypeEnum.values();
        return Stream.of(values).filter(it -> it.type == type).findAny().orElse(null);
    }
}
