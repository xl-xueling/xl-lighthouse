package com.dtstep.lighthouse.insights.dao;
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
import com.dtstep.lighthouse.insights.dto.PermissionQueryParam;
import com.dtstep.lighthouse.common.enums.OwnerTypeEnum;
import com.dtstep.lighthouse.common.modal.Permission;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionDao {

    int insert(Permission permission);

    int batchInsert(List<Permission> list);

    Permission queryById(Integer id);

    int delete(@Param("queryParam")PermissionQueryParam queryParam);

    @Deprecated
    boolean checkUserPermission(Integer userId, Integer roleId);

    boolean checkOwnerPermission(Integer ownerId,OwnerTypeEnum ownerType, Integer roleId);

    boolean existPermission(Integer ownerId, OwnerTypeEnum ownerType, Integer roleId);

    int extensionPermission(Integer id, int expire) throws Exception;

    int delete(Integer ownerId, OwnerTypeEnum ownerType, Integer roleId);

    List<Integer> queryUserPermissionsByRoleId(Integer roleId, Integer limit);

    List<Permission> queryUserManagePermission(Integer userId,Integer limit);

    List<Permission> queryList(@Param("queryParam")PermissionQueryParam queryParam);

    List<Permission> queryAuthList(@Param("queryParam")PermissionQueryParam queryParam);
}
