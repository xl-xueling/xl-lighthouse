package com.dtstep.lighthouse.insights.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

public enum RoleTypeEnum {

    FULL_MANAGE_PERMISSION(1),

    FULL_ACCESS_PERMISSION(2),

    DEPARTMENT_MANAGE_PERMISSION(3),

    DEPARTMENT_ACCESS_PERMISSION(4),

    PROJECT_MANAGE_PERMISSION(5),

    PROJECT_ACCESS_PERMISSION(6),

    GROUP_MANAGE_PERMISSION(7),

    GROUP_ACCESS_PERMISSION(8),

    STAT_MANAGE_PERMISSION(9),

    STAT_ACCESS_PERMISSION(10),

    METRIC_MANAGE_PERMISSION(11),

    METRIC_ACCESS_PERMISSION(12),

    OPT_MANAGE_PERMISSION(100),

    OPT_ACCESS_PERMISSION(101),

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
