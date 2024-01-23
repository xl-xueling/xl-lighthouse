package com.dtstep.lighthouse.insights.dto;

import com.dtstep.lighthouse.common.enums.RoleTypeEnum;

import javax.validation.constraints.NotNull;

public class PermissionReleaseParam {

    @NotNull
    private Integer resourceId;

    @NotNull
    private RoleTypeEnum roleType;

    @NotNull
    private Integer permissionId;

    public Integer getResourceId() {
        return resourceId;
    }

    public void setResourceId(Integer resourceId) {
        this.resourceId = resourceId;
    }

    public RoleTypeEnum getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleTypeEnum roleType) {
        this.roleType = roleType;
    }

    public Integer getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Integer permissionId) {
        this.permissionId = permissionId;
    }
}
