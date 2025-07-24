package com.dtstep.lighthouse.insights.dto;

import com.dtstep.lighthouse.common.enums.OwnerTypeEnum;
import com.dtstep.lighthouse.common.enums.ResourceTypeEnum;

import java.util.Date;

public class PermissionInfo {

    private Integer ownerId;

    private OwnerTypeEnum ownerType;

    private String title;

    private Integer resourceId;

    private ResourceTypeEnum resourceType;

    private boolean granted;

    private Date createTime;

    private Date expireTime;

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public OwnerTypeEnum getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(OwnerTypeEnum ownerType) {
        this.ownerType = ownerType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public boolean isGranted() {
        return granted;
    }

    public void setGranted(boolean granted) {
        this.granted = granted;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }
}
