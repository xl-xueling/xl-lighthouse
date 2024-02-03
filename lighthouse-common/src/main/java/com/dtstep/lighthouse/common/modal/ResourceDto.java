package com.dtstep.lighthouse.common.modal;

import com.dtstep.lighthouse.common.enums.ResourceTypeEnum;

public class ResourceDto<T extends Object> {

    private ResourceTypeEnum resourceType;

    private Integer resourceId;

    private ResourceTypeEnum parentResourceType;

    private Integer resourcePid;

    private T data;

    public static ResourceDto newResource(ResourceTypeEnum resourceType, Integer resourceId, ResourceTypeEnum parentResourceType, Integer parentResourceId){
        return new ResourceDto(resourceType,resourceId,parentResourceType,parentResourceId);
    }

    private ResourceDto(){}

    public ResourceDto(ResourceTypeEnum resourceType, Integer resourceId){
        this.resourceType = resourceType;
        this.resourceId = resourceId;
    }

    public ResourceDto(ResourceTypeEnum resourceType, Integer resourceId, ResourceTypeEnum parentResourceType, Integer resourcePid){
        this.resourceType = resourceType;
        this.resourceId = resourceId;
        this.parentResourceType = parentResourceType;
        this.resourcePid = resourcePid;
    }

    public ResourceDto(ResourceTypeEnum resourceType, Integer resourceId, T data){
        this.resourceType = resourceType;
        this.resourceId = resourceId;
        this.data = data;
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ResourceTypeEnum getParentResourceType() {
        return parentResourceType;
    }

    public void setParentResourceType(ResourceTypeEnum parentResourceType) {
        this.parentResourceType = parentResourceType;
    }
}
