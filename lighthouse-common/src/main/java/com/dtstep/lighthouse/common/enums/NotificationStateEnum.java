package com.dtstep.lighthouse.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

public enum NotificationStateEnum {

    Pend(1),

    Sent(2),

    Received(3),

    Failed(4),

    ;

    NotificationStateEnum(int state){
        this.state = state;
    }

    @JsonValue
    private int state;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @JsonCreator
    public static NotificationStateEnum forValue(int state){
        NotificationStateEnum[] values = NotificationStateEnum.values();
        return Stream.of(values).filter(it -> it.getState() == state).findAny().orElse(null);
    }
}
