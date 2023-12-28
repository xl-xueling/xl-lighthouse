package com.dtstep.lighthouse.insights.enums;

public enum PositionTypeEnum {

    CHAIRMAN(1, RoleTypeEnum.FULL_MANAGE_PERMISSION),

    CEO(2, RoleTypeEnum.FULL_MANAGE_PERMISSION),

    VP(3, RoleTypeEnum.FULL_MANAGE_PERMISSION),

    SENIOR_DIRECTOR(4, RoleTypeEnum.DEPARTMENT_MANAGE_PERMISSION),

    DIRECTOR(5, RoleTypeEnum.DEPARTMENT_MANAGE_PERMISSION),

    SENIOR_MANAGER(6, RoleTypeEnum.DEPARTMENT_MANAGE_PERMISSION),

    MANAGER(7, RoleTypeEnum.DEPARTMENT_MANAGE_PERMISSION),

    SUPERVISOR(8, RoleTypeEnum.DEPARTMENT_MANAGE_PERMISSION),

    STAFF(9),

    ;

    PositionTypeEnum(int roleType){
        this.roleType = roleType;
    }

    PositionTypeEnum(int roleType, RoleTypeEnum roleTypeEnum){
        this.roleType = roleType;
        this.roleTypeEnum = roleTypeEnum;
    }

    private int roleType;

    private RoleTypeEnum roleTypeEnum;

    public int getRoleType() {
        return roleType;
    }

    public void setRoleType(int roleType) {
        this.roleType = roleType;
    }

    public RoleTypeEnum getPermissionTypeEnum() {
        return roleTypeEnum;
    }

    public void setPermissionTypeEnum(RoleTypeEnum roleTypeEnum) {
        this.roleTypeEnum = roleTypeEnum;
    }
}
