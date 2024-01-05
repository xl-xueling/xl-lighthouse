package com.dtstep.lighthouse.insights.modal;

import com.dtstep.lighthouse.insights.enums.ResourceTypeEnum;

public class Resource {

    private ResourceTypeEnum resourceType;

    private Integer resourceId;

    private Integer resourcePid;

    public static Resource newResource(ResourceTypeEnum resourceType,Integer resourceId,Integer resourcePid){
        return new Resource(resourceType,resourceId,resourcePid);
    }

    public Resource(){}

    public Resource(ResourceTypeEnum resourceType,Integer resourceId){
        this.resourceType = resourceType;
        this.resourceId = resourceId;
    }

    public Resource(ResourceTypeEnum resourceType,Integer resourceId,Integer resourcePid){
        this.resourceType = resourceType;
        this.resourceId = resourceId;
        this.resourcePid = resourcePid;
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

    public Integer getResourcePid() {
        return resourcePid;
    }

    public void setResourcePid(Integer resourcePid) {
        this.resourcePid = resourcePid;
    }
}
