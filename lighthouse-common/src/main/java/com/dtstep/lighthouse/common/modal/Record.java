package com.dtstep.lighthouse.common.modal;

import com.dtstep.lighthouse.common.enums.RecordTypeEnum;
import com.dtstep.lighthouse.common.enums.ResourceTypeEnum;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Record implements Serializable {

    private Integer id;

    private Integer userId;

    private Integer resourceId;

    private ResourceTypeEnum resourceType;

    private RecordTypeEnum recordType;

    private String extend;

    private LocalDateTime createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public RecordTypeEnum getRecordType() {
        return recordType;
    }

    public void setRecordType(RecordTypeEnum recordType) {
        this.recordType = recordType;
    }
}
