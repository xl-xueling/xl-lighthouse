package com.dtstep.lighthouse.insights.dto;

import com.dtstep.lighthouse.common.enums.LinkTypeEnum;
import com.dtstep.lighthouse.common.enums.ResourceTypeEnum;

public class LinkQueryParam {

    private Integer resourceId;

    private ResourceTypeEnum resourceType;

    private LinkTypeEnum linkType;

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

    public LinkTypeEnum getLinkType() {
        return linkType;
    }

    public void setLinkType(LinkTypeEnum linkType) {
        this.linkType = linkType;
    }
}
