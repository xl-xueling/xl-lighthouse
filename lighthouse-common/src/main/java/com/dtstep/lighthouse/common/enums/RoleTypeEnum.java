package com.dtstep.lighthouse.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

public enum RoleTypeEnum {

    FULL_MANAGE_PERMISSION(1),

    FULL_ACCESS_PERMISSION(2),

    OPT_MANAGE_PERMISSION(3),

    OPT_ACCESS_PERMISSION(4),

    DOMAIN_MANAGE_PERMISSION(5),

    DOMAIN_ACCESS_PERMISSION(6),

    DEPARTMENT_MANAGE_PERMISSION(7),

    DEPARTMENT_ACCESS_PERMISSION(8),

    PROJECT_MANAGE_PERMISSION(9),

    PROJECT_ACCESS_PERMISSION(10),

    GROUP_MANAGE_PERMISSION(11),

    GROUP_ACCESS_PERMISSION(12),

    STAT_MANAGE_PERMISSION(13),

    STAT_ACCESS_PERMISSION(14),

    METRIC_MANAGE_PERMISSION(15),

    METRIC_ACCESS_PERMISSION(16),

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
