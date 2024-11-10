package com.dtstep.lighthouse.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

public enum LanguageEnum {

    ENGLISH(1),

    CHINESE(2),

    ;

    LanguageEnum(int type){
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
    public static LanguageEnum forValue(int type){
        LanguageEnum[] values = LanguageEnum.values();
        return Stream.of(values).filter(it -> it.type == type).findAny().orElse(null);
    }
}
