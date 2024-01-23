package com.dtstep.lighthouse.insights.vo;

import com.dtstep.lighthouse.common.enums.RoleTypeEnum;
import com.dtstep.lighthouse.common.util.BeanCopyUtil;
import com.dtstep.lighthouse.insights.modal.Permission;

public class PermissionVO extends Permission {

    private Object extend;

    private RoleTypeEnum roleType;

    public PermissionVO(Permission permission){
        assert permission != null;
        BeanCopyUtil.copy(permission,this);
    }

    public Object getExtend() {
        return extend;
    }

    public void setExtend(Object extend) {
        this.extend = extend;
    }

    public RoleTypeEnum getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleTypeEnum roleType) {
        this.roleType = roleType;
    }
}
