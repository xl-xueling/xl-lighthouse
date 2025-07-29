package com.dtstep.lighthouse.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

public enum DefinitionsEnum {

    VIEW_CATEGORY(1),

    ;

    DefinitionsEnum(int type){
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
    public static DefinitionsEnum forValue(int type){
        DefinitionsEnum[] values = DefinitionsEnum.values();
        return Stream.of(values).filter(it -> it.getType() == type).findAny().orElse(null);
    }
}
