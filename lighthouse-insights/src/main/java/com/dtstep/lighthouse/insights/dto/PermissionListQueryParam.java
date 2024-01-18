package com.dtstep.lighthouse.insights.dto;

import com.dtstep.lighthouse.insights.enums.OwnerTypeEnum;
import com.dtstep.lighthouse.common.enums.RoleTypeEnum;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class PermissionListQueryParam implements Serializable {

    @NotNull
    private Integer resourceId;

    @NotNull
    private RoleTypeEnum roleType;

    @NotNull
    private OwnerTypeEnum ownerType;

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

    public OwnerTypeEnum getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(OwnerTypeEnum ownerType) {
        this.ownerType = ownerType;
    }
}
