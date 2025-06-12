package com.dtstep.lighthouse.insights.dto;
/*
 * Copyright (C) 2022-2025 XueLing.雪灵
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
