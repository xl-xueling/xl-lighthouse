package com.dtstep.lighthouse.common.modal;
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
