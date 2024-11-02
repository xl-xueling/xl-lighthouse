package com.dtstep.lighthouse.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.List;
import java.util.stream.Stream;

public enum NotificationTypeEnum {

    StatAlarm(1,List.of(NotificationChannelEnum.RemoteService)),

    ;

    @JsonValue
    private int type;

    private List<NotificationChannelEnum> channelEnumList;

    NotificationTypeEnum(int type){
        this.type = type;
    }

    NotificationTypeEnum(int type,List<NotificationChannelEnum> channelEnumList){
        this.type = type;
        this.channelEnumList = channelEnumList;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<NotificationChannelEnum> getChannelEnumList() {
        return channelEnumList;
    }

    public void setChannelEnumList(List<NotificationChannelEnum> channelEnumList) {
        this.channelEnumList = channelEnumList;
    }

    @JsonCreator
    public static NotificationTypeEnum forValue(int type){
        NotificationTypeEnum[] values = NotificationTypeEnum.values();
        return Stream.of(values).filter(it -> it.getType() == type).findAny().orElse(null);
    }
}
