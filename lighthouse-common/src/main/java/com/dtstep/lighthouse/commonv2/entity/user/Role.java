package com.dtstep.lighthouse.commonv2.entity.user;

import com.dtstep.lighthouse.commonv2.enums.AuthRoleTypeEnum;

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
