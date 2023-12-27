package com.dtstep.lighthouse.insights.enums;

public enum RoleTypeEnum {

    CHAIRMAN(1),

    CEO(2),

    VP(3),

    SENIOR_DIRECTOR(4),

    DIRECTOR(5),

    SENIOR_MANAGER(6),

    MANAGER(7),

    SUPERVISOR(8),

    TEAM_LEADER(9),

    STAFF(10),

    SYSTEM_ADMIN(11),

    PROJECT_ADMIN(12),

    PROJECT_USER(13),

    METRIC_ADMIN(14),

    METRIC_USER(15),

    STAT_ADMIN(16),

    STAT_USER(17),

    ;

    RoleTypeEnum(int roleType){
        this.roleType = roleType;
    }

    RoleTypeEnum(int roleType, String desc){
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
