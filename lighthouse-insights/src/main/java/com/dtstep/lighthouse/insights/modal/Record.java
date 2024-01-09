package com.dtstep.lighthouse.insights.modal;

import com.dtstep.lighthouse.insights.enums.RecordTypeEnum;
import com.dtstep.lighthouse.insights.enums.ResourceTypeEnum;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Record implements Serializable {

    private Integer id;

    private Integer resourceId;

    private ResourceTypeEnum resourceType;

    private RecordTypeEnum recordType;

    private String desc;

    private LocalDateTime recordTime;

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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public LocalDateTime getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(LocalDateTime recordTime) {
        this.recordTime = recordTime;
    }

    public RecordTypeEnum getRecordType() {
        return recordType;
    }

    public void setRecordType(RecordTypeEnum recordType) {
        this.recordType = recordType;
    }
}
