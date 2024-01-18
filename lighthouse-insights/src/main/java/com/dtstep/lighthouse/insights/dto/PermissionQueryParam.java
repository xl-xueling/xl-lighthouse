package com.dtstep.lighthouse.insights.dto;

import com.dtstep.lighthouse.insights.enums.OwnerTypeEnum;

public class PermissionQueryParam {

    private Integer roleId;

    private Integer ownerId;

    private OwnerTypeEnum ownerType;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public OwnerTypeEnum getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(OwnerTypeEnum ownerType) {
        this.ownerType = ownerType;
    }
}
