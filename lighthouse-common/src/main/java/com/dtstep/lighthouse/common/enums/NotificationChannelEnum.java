package com.dtstep.lighthouse.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

public enum NotificationChannelEnum {

    SiteMessage(0),

    RemoteService(1),

    Tencent_Sms(2),

    Tencent_Email(3),

    Tencent_WeiXin(4),

    ;

    @JsonValue
    private int type;

    NotificationChannelEnum(int type){
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @JsonCreator
    public static NotificationChannelEnum forValue(int type){
        NotificationChannelEnum[] values = NotificationChannelEnum.values();
        return Stream.of(values).filter(it -> it.getType() == type).findAny().orElse(null);
    }
}
