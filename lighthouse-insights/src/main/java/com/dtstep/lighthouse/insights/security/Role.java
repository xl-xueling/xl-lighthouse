package com.dtstep.lighthouse.insights.security;

public class Role {

    private RoleTypeEnum roleTypeEnum;

    public Role(RoleTypeEnum roleTypeEnum){
        this.roleTypeEnum = roleTypeEnum;
    }

    public RoleTypeEnum getRoleType() {
        return roleTypeEnum;
    }

    public void setRoleType(RoleTypeEnum roleTypeEnum) {
        this.roleTypeEnum = roleTypeEnum;
    }
}
