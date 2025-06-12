package com.dtstep.lighthouse.insights.service;
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
import com.dtstep.lighthouse.common.entity.ResultCode;
import com.dtstep.lighthouse.common.modal.RolePair;
import com.dtstep.lighthouse.common.enums.OwnerTypeEnum;
import com.dtstep.lighthouse.common.enums.RoleTypeEnum;
import com.dtstep.lighthouse.common.modal.ResourceDto;

public interface ResourceService {

    RolePair addResourceCallback(ResourceDto resource);

    void updateResourcePidCallback(ResourceDto resource);

    ResultCode deleteResourceCallback(ResourceDto resource);

    int grantPermission(Integer ownerId, OwnerTypeEnum ownerTypeEnum, Integer resourceId,RoleTypeEnum roleTypeEnum);

    int releasePermission(Integer ownerId, OwnerTypeEnum ownerTypeEnum, Integer resourceId,RoleTypeEnum roleTypeEnum);

    ResourceDto queryByRoleId(Integer roleId);

}
