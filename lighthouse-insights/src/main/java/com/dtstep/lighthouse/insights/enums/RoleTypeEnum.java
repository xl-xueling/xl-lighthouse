package com.dtstep.lighthouse.insights.enums;

public enum RoleTypeEnum {

    CHAIRMAN(1,PermissionTypeEnum.FULL_MANAGE_PERMISSION),

    CEO(2,PermissionTypeEnum.FULL_MANAGE_PERMISSION),

    VP(3,PermissionTypeEnum.FULL_MANAGE_PERMISSION),

    SENIOR_DIRECTOR(4,PermissionTypeEnum.DEPARTMENT_MANAGE_PERMISSION),

    DIRECTOR(5,PermissionTypeEnum.DEPARTMENT_MANAGE_PERMISSION),

    SENIOR_MANAGER(6,PermissionTypeEnum.DEPARTMENT_MANAGE_PERMISSION),

    MANAGER(7,PermissionTypeEnum.DEPARTMENT_MANAGE_PERMISSION),

    SUPERVISOR(8,PermissionTypeEnum.DEPARTMENT_MANAGE_PERMISSION),

    STAFF(9),

    ;

    RoleTypeEnum(int roleType){
        this.roleType = roleType;
    }

    RoleTypeEnum(int roleType,PermissionTypeEnum permissionTypeEnum){

    }

    RoleTypeEnum(int roleType, String desc){
        this.roleType = roleType;

    }

    private int roleType;

    private PermissionTypeEnum permissionTypeEnum;

    public int getRoleType() {
        return roleType;
    }

    public void setRoleType(int roleType) {
        this.roleType = roleType;
    }

    public PermissionTypeEnum getPermissionTypeEnum() {
        return permissionTypeEnum;
    }

    public void setPermissionTypeEnum(PermissionTypeEnum permissionTypeEnum) {
        this.permissionTypeEnum = permissionTypeEnum;
    }
}
