package com.dtstep.lighthouse.insights.dto;

import com.dtstep.lighthouse.insights.enums.RecordTypeEnum;
import com.dtstep.lighthouse.insights.enums.ResourceTypeEnum;

import java.util.List;

public class RecordQueryParam {

    private Integer resourceId;

    private ResourceTypeEnum resourceType;

    private List<RecordTypeEnum> recordTypes;

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

    public List<RecordTypeEnum> getRecordTypes() {
        return recordTypes;
    }

    public void setRecordTypes(List<RecordTypeEnum> recordTypes) {
        this.recordTypes = recordTypes;
    }
}
