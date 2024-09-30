package com.dtstep.lighthouse.insights.controller;
/*
 * Copyright (C) 2022-2024 XueLing.雪灵
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
import com.dtstep.lighthouse.common.modal.AuthRecord;
import com.dtstep.lighthouse.common.modal.ListSearchObject;
import com.dtstep.lighthouse.common.modal.Pagination;
import com.dtstep.lighthouse.insights.vo.ResourceVO;
import com.dtstep.lighthouse.insights.vo.ResultData;
import com.dtstep.lighthouse.common.entity.ListData;
import com.dtstep.lighthouse.insights.dto.PermissionListQueryParam;
import com.dtstep.lighthouse.insights.dto.PermissionQueryParam;
import com.dtstep.lighthouse.common.enums.RoleTypeEnum;
import com.dtstep.lighthouse.common.modal.Role;
import com.dtstep.lighthouse.insights.service.PermissionService;
import com.dtstep.lighthouse.insights.service.RoleService;
import com.dtstep.lighthouse.insights.vo.PermissionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RoleService roleService;

    @RequestMapping("/permission/list")
    public ResultData<ListData<PermissionVO>> list(@Validated @RequestBody ListSearchObject<PermissionListQueryParam> searchObject) {
        PermissionListQueryParam listQueryParam = searchObject.getQueryParams();
        Integer resourceId = listQueryParam.getResourceId();
        RoleTypeEnum roleTypeEnum = listQueryParam.getRoleType();
        Role role = roleService.queryRole(roleTypeEnum,resourceId);
        Pagination pagination = searchObject.getPagination();
        PermissionQueryParam queryParam = new PermissionQueryParam();
        queryParam.setOwnerType(listQueryParam.getOwnerType());
        queryParam.setRoleId(role.getId());
        queryParam.setSearch(listQueryParam.getSearch());
        ListData<PermissionVO> listData = permissionService.queryList(queryParam, pagination.getPageNum(), pagination.getPageSize());
        return ResultData.success(listData);
    }

    @RequestMapping("/permission/ownerAuthList")
    public ResultData<ListData<AuthRecord>> authList(@Validated @RequestBody ListSearchObject<PermissionQueryParam> searchObject) throws Exception {
        PermissionQueryParam listQueryParam = searchObject.getQueryParamOrDefault(new PermissionQueryParam());
        Pagination pagination = searchObject.getPagination();
        ListData<AuthRecord> listData = permissionService.queryOwnerAuthList(listQueryParam, pagination.getPageNum(), pagination.getPageSize());
        return ResultData.success(listData);
    }


}
