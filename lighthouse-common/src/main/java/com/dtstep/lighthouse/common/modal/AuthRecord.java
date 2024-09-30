package com.dtstep.lighthouse.common.modal;

import com.dtstep.lighthouse.common.enums.ResourceTypeEnum;
import com.dtstep.lighthouse.common.util.BeanCopyUtil;

public class AuthRecord extends Permission {

    private Integer id;

    private Integer resourceId;

    private ResourceTypeEnum resourceType;

    private Object extend;

    public AuthRecord(Permission permission){
        assert permission != null;
        BeanCopyUtil.copy(permission,this);
    }

    public AuthRecord(){

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getResourceId() {
        return resourceId;
    }

    public void setResourceId(Integer resourceId) {
        this.resourceId = resourceId;
    }

    public ResourceTypeEnum getResourceType() {
        return resourceType;
    }

    public void setResourceType(ResourceTypeEnum resourceType) {
        this.resourceType = resourceType;
    }

    public Object getExtend() {
        return extend;
    }

    public void setExtend(Object extend) {
        this.extend = extend;
    }


}
