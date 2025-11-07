package com.dtstep.lighthouse.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

public enum CreationsTypeEnum {

    VIEW_FAVORITE(1),

    VIEW_TEMPLATE(2),

    ;

    @JsonValue
    private int type;

    CreationsTypeEnum(int type){
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @JsonCreator
    public static CreationsTypeEnum forValue(int type){
        CreationsTypeEnum[] values = CreationsTypeEnum.values();
        return Stream.of(values).filter(it -> it.getType() == type).findAny().orElse(null);
    }
}
