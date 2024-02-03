package com.dtstep.lighthouse.common.modal;

import com.dtstep.lighthouse.common.enums.ResourceTypeEnum;

import javax.validation.constraints.NotNull;

public class MetricBindElement {

    @NotNull
    private Integer resourceId;

    @NotNull
    private ResourceTypeEnum resourceType;

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
}
