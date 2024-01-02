package com.dtstep.lighthouse.insights.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

public enum RoleTypeEnum {

    DEPARTMENT_MANAGE_PERMISSION(1),

    DEPARTMENT_ACCESS_PERMISSION(2),

    PROJECT_MANAGE_PERMISSION(3),

    PROJECT_ACCESS_PERMISSION(4),

    METRIC_MANAGE_PERMISSION(5),

    METRIC_ACCESS_PERMISSION(6),

    STAT_MANAGE_PERMISSION(7),

    STAT_ACCESS_PERMISSION(8),

    OPT_MANAGE_PERMISSION(50),

    ;

    RoleTypeEnum(int roleType){
        this.roleType = roleType;
    }

    @JsonValue
    private int roleType;

    public int getRoleType() {
        return roleType;
    }

    public void setRoleType(int roleType) {
        this.roleType = roleType;
    }


    @JsonCreator
    public static RoleTypeEnum forValue(int value){
        RoleTypeEnum[] values = RoleTypeEnum.values();
        return Stream.of(values).filter(it -> it.getRoleType() == value).findAny().orElse(null);
    }
}
