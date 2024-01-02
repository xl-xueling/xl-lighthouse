package com.dtstep.lighthouse.insights.modal;

import com.dtstep.lighthouse.insights.enums.ResourceTypeEnum;

public class Resource {

    private ResourceTypeEnum resourceType;

    private Integer resourceId;

    private Integer pid;

    public static Resource newResource(ResourceTypeEnum resourceType,Integer resourceId,Integer pid){
        return new Resource(resourceType,resourceId,pid);
    }

    public Resource(){}

    public Resource(ResourceTypeEnum resourceType,Integer resourceId,Integer pid){
        this.resourceType = resourceType;
        this.resourceId = resourceId;
        this.pid = pid;
    }

    public ResourceTypeEnum getResourceType() {
        return resourceType;
    }

    public void setResourceType(ResourceTypeEnum resourceType) {
        this.resourceType = resourceType;
    }

    public Integer getResourceId() {
        return resourceId;
    }

    public void setResourceId(Integer resourceId) {
        this.resourceId = resourceId;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }
}
