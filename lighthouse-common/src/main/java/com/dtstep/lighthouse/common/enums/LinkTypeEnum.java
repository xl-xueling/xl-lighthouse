package com.dtstep.lighthouse.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

public enum LinkTypeEnum {

    VIEW_SHARE_LINK(1),

    ;

    @JsonValue
    private int type;

    LinkTypeEnum(int type){
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @JsonCreator
    public static LinkTypeEnum forValue(int type){
        LinkTypeEnum[] values = LinkTypeEnum.values();
        return Stream.of(values).filter(it -> it.getType() == type).findAny().orElse(null);
    }

}
