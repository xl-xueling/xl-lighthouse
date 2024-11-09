package com.dtstep.lighthouse.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

public enum AlarmChannelEnum {

    SiteMessage(0),

    RemoteService(1),

    ;

    @JsonValue
    private int type;

    AlarmChannelEnum(int type){
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @JsonCreator
    public static AlarmChannelEnum forValue(int type){
        AlarmChannelEnum[] values = AlarmChannelEnum.values();
        return Stream.of(values).filter(it -> it.getType() == type).findAny().orElse(null);
    }
}
