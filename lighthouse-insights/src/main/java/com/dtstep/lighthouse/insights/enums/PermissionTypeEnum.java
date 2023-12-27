package com.dtstep.lighthouse.insights.enums;

public enum PermissionTypeEnum {

    FULL_MANAGE_PERMISSION(1),

    DEPARTMENT_MANAGE_PERMISSION(2),

    PROJECT_MANAGE_PERMISSION(3),

    PROJECT_ACCESS_PERMISSION(4),

    METRIC_MANAGE_PERMISSION(5),

    METRIC_ACCESS_PERMISSION(6),

    STAT_ACCESS_PERMISSION(7),

    OPT_MANAGE_PERMISSION(8),

    ;

    PermissionTypeEnum(int permissionType){
        this.permissionType = permissionType;
    }

    private int permissionType;

    public int getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(int permissionType) {
        this.permissionType = permissionType;
    }
}
