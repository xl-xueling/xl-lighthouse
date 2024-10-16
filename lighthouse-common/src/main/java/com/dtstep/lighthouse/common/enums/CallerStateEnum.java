package com.dtstep.lighthouse.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

public enum CallerStateEnum {

    PENDING(0,"待审核"),

    DELETED(1,"已删除"),

    NORMAL(2,"正常"),

    FROZEN(3,"已冻结"),

    ;

    @JsonValue
    private int state;

    private String desc;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    CallerStateEnum(int state){
        this.state = state;
    }

    CallerStateEnum(int state,String desc){
        this.state = state;this.desc = desc;
    }

    @JsonCreator
    public static CallerStateEnum forValue(int state){
        CallerStateEnum[] values = CallerStateEnum.values();
        return Stream.of(values).filter(it -> it.getState() == state).findAny().orElse(null);
    }
}
