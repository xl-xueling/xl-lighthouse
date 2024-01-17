package com.dtstep.lighthouse.insights.dto_bak;

public class RolePair {

    private Integer manageRoleId;

    private Integer accessRoleId;

    public RolePair(Integer manageRoleId,Integer accessRoleId){
        this.manageRoleId = manageRoleId;
        this.accessRoleId = accessRoleId;
    }

    public Integer getManageRoleId() {
        return manageRoleId;
    }

    public void setManageRoleId(Integer manageRoleId) {
        this.manageRoleId = manageRoleId;
    }

    public Integer getAccessRoleId() {
        return accessRoleId;
    }

    public void setAccessRoleId(Integer accessRoleId) {
        this.accessRoleId = accessRoleId;
    }
}
