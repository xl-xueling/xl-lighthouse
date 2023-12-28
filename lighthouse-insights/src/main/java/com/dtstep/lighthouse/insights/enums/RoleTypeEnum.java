package com.dtstep.lighthouse.insights.enums;

public enum RoleTypeEnum {

    FULL_MANAGE_PERMISSION(1),

    FULL_ACCESS_PERMISSION(2),

    DEPARTMENT_MANAGE_PERMISSION(3),

    DEPARTMENT_ACCESS_PERMISSION(4),

    PROJECT_MANAGE_PERMISSION(5),

    PROJECT_ACCESS_PERMISSION(6),

    METRIC_MANAGE_PERMISSION(7),

    METRIC_ACCESS_PERMISSION(8),

    STAT_ACCESS_PERMISSION(9),

    OPT_MANAGE_PERMISSION(10),

    ;

    RoleTypeEnum(int roleType){
        this.roleType = roleType;
    }

    private int roleType;

    public int getRoleType() {
        return roleType;
    }

    public void setRoleType(int roleType) {
        this.roleType = roleType;
    }
}
