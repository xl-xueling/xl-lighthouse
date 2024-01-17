package com.dtstep.lighthouse.insights.dto_bak;

import com.dtstep.lighthouse.common.util.BeanCopyUtil;
import com.dtstep.lighthouse.insights.modal.Permission;

public class PermissionDto extends Permission {

    private Object extend;

    public PermissionDto(Permission permission){
        assert permission != null;
        BeanCopyUtil.copy(permission,this);
    }

    public Object getExtend() {
        return extend;
    }

    public void setExtend(Object extend) {
        this.extend = extend;
    }
}
