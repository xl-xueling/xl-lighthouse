package com.dtstep.lighthouse.insights.dto;

import com.dtstep.lighthouse.common.enums.NotificationTypeEnum;
import com.dtstep.lighthouse.common.enums.ResourceTypeEnum;

import java.time.LocalDateTime;

public class NotificationQueryParam {

    private Integer userId;

    private NotificationTypeEnum notificationType;

    private Integer resourceId;

    private ResourceTypeEnum resourceType;

    private String search;

    private LocalDateTime createStartTime;

    private LocalDateTime createEndTime;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public NotificationTypeEnum getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationTypeEnum notificationType) {
        this.notificationType = notificationType;
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

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public LocalDateTime getCreateStartTime() {
        return createStartTime;
    }

    public void setCreateStartTime(LocalDateTime createStartTime) {
        this.createStartTime = createStartTime;
    }

    public LocalDateTime getCreateEndTime() {
        return createEndTime;
    }

    public void setCreateEndTime(LocalDateTime createEndTime) {
        this.createEndTime = createEndTime;
    }
}
