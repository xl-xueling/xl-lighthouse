package com.dtstep.lighthouse.insights.controller;
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
import com.dtstep.lighthouse.common.entity.Owner;
import com.dtstep.lighthouse.common.entity.ServiceResult;
import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.enums.PrivateTypeEnum;
import com.dtstep.lighthouse.common.enums.RoleTypeEnum;
import com.dtstep.lighthouse.common.modal.LimitDataObject;
import com.dtstep.lighthouse.common.modal.Role;
import com.dtstep.lighthouse.common.entity.ResultCode;
import com.dtstep.lighthouse.common.modal.Stat;
import com.dtstep.lighthouse.core.batch.BatchAdapter;
import com.dtstep.lighthouse.core.builtin.BuiltinLoader;
import com.dtstep.lighthouse.core.wrapper.StatDBWrapper;
import com.dtstep.lighthouse.insights.config.SoftEdition;
import com.dtstep.lighthouse.insights.dto.LimitStatQueryParam;
import com.dtstep.lighthouse.insights.service.*;
import com.dtstep.lighthouse.insights.vo.ResultData;
import com.dtstep.lighthouse.common.modal.StatDataObject;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.insights.dto.DataStatQueryParam;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@ControllerAdvice
public class DataController {

    @Autowired
    private DataService dataService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private BaseService baseService;

    @Autowired
    private StatService statService;

    @Autowired
    private SoftEdition softEdition;

    @RequestMapping("/data/stat")
    public ResultData<List<StatDataObject>> dataQuery(@Validated @RequestBody DataStatQueryParam queryParam) throws Exception{
        int statId = queryParam.getStatId();
        StatExtEntity stat = statService.queryById(statId);
        Validate.notNull(stat);
        if(softEdition.isPro() && !BuiltinLoader.isBuiltinStat(statId) && stat.getPrivateType() == PrivateTypeEnum.Private){
            Owner owner = baseService.getCurrentOwner();
            Role manageRole = roleService.queryRole(RoleTypeEnum.STAT_MANAGE_PERMISSION,statId);
            boolean hasManagePermission = permissionService.checkOwnerPermission(owner,manageRole.getId());
            if(!hasManagePermission){
                Role accessRole = roleService.queryRole(RoleTypeEnum.STAT_ACCESS_PERMISSION,statId);
                boolean hasAccessPermission = permissionService.checkOwnerPermission(owner,accessRole.getId());
                if(!hasAccessPermission){
                    return ResultData.result(ResultCode.accessDenied);
                }
            }
        }
        List<String> dimensList = dataService.dimensArrangement(stat,queryParam.getDimensParams());
        if(stat.getTemplateEntity().getDimensArray().length > 0 && CollectionUtils.isEmpty(dimensList)){
            return ResultData.result(ResultCode.dataQueryMissingDimensParams);
        }
        ServiceResult<List<StatDataObject>> serviceResult = dataService.dataQuery(stat,queryParam.getStartTime(),queryParam.getEndTime(),dimensList);
        if(serviceResult.isSuccess()){
            return ResultData.success(serviceResult.getData());
        }else{
            return ResultData.result(serviceResult.getResultCode());
        }
    }

    @RequestMapping("/data/limit")
    public ResultData<List<LimitDataObject>> limitQuery(@Validated @RequestBody LimitStatQueryParam queryParam) throws Exception {
        int statId = queryParam.getStatId();
        StatExtEntity stat = statService.queryById(statId);
        Validate.notNull(stat);
        if(softEdition.isPro() && !BuiltinLoader.isBuiltinStat(statId) && stat.getPrivateType() == PrivateTypeEnum.Private){
            Owner owner = baseService.getCurrentOwner();
            Role manageRole = roleService.queryRole(RoleTypeEnum.STAT_MANAGE_PERMISSION,statId);
            boolean hasManagePermission = permissionService.checkOwnerPermission(owner,manageRole.getId());
            if(!hasManagePermission){
                Role accessRole = roleService.queryRole(RoleTypeEnum.STAT_ACCESS_PERMISSION,statId);
                boolean hasAccessPermission = permissionService.checkOwnerPermission(owner,accessRole.getId());
                if(!hasAccessPermission){
                    return ResultData.result(ResultCode.accessDenied);
                }
            }
        }
        List<Long> batchTimeList = queryParam.getBatchTimeList();
        if(CollectionUtils.isEmpty(batchTimeList)){
            batchTimeList = BatchAdapter.queryBatchTimeList(stat.getTimeparam(),0,System.currentTimeMillis(),10);
        }
        List<LimitDataObject> objectList = dataService.limitQuery(stat,batchTimeList);
        return ResultData.success(objectList);
    }
}
