package com.dtstep.lighthouse.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.List;
import java.util.stream.Stream;

public enum NotificationTypeEnum {

    StatAlarm(1),

    ;

    @JsonValue
    private int type;

    NotificationTypeEnum(int type){
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @JsonCreator
    public static NotificationTypeEnum forValue(int type){
        NotificationTypeEnum[] values = NotificationTypeEnum.values();
        return Stream.of(values).filter(it -> it.getType() == type).findAny().orElse(null);
    }
}
