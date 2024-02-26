package com.dtstep.lighthouse.common.entity;

import com.dtstep.lighthouse.common.enums.AuthRoleTypeEnum;

public class Role {

    private AuthRoleTypeEnum authRoleTypeEnum;

    public Role(AuthRoleTypeEnum authRoleTypeEnum){
        this.authRoleTypeEnum = authRoleTypeEnum;
    }

    public AuthRoleTypeEnum getRoleType() {
        return authRoleTypeEnum;
    }

    public void setRoleType(AuthRoleTypeEnum authRoleTypeEnum) {
        this.authRoleTypeEnum = authRoleTypeEnum;
    }
}
