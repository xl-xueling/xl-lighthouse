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
import com.dtstep.lighthouse.common.modal.*;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.common.entity.ListData;
import com.dtstep.lighthouse.common.entity.ResultCode;
import com.dtstep.lighthouse.insights.dto.ComponentCreateParam;
import com.dtstep.lighthouse.insights.dto.ComponentQueryParam;
import com.dtstep.lighthouse.insights.dto.ComponentUpdateParam;
import com.dtstep.lighthouse.insights.dto.ComponentVerifyParam;
import com.dtstep.lighthouse.insights.service.BaseService;
import com.dtstep.lighthouse.insights.service.ComponentService;
import com.dtstep.lighthouse.insights.vo.ComponentVO;
import com.dtstep.lighthouse.insights.vo.ResultData;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@ControllerAdvice
public class ComponentController {

    @Autowired
    private ComponentService componentService;

    @Autowired
    private BaseService baseService;

    @RequestMapping("/component/verify")
    public ResultData<List<CommonTreeNode>> verify(@Validated @RequestBody ComponentVerifyParam verifyParam) {
        ResultCode resultCode = componentService.verify(verifyParam.getConfiguration());
        return ResultData.result(resultCode);
    }

    @RequestMapping("/component/create")
    public ResultData<Integer> create(@Validated @RequestBody ComponentCreateParam createParam) {
        ResultCode resultCode = componentService.verify(createParam.getConfiguration());
        if(resultCode != ResultCode.success){
            return ResultData.result(resultCode);
        }
        Component component = new Component();
        component.setComponentType(createParam.getComponentType());
        component.setPrivateType(createParam.getPrivateType());
        component.setConfiguration(JsonUtil.toJavaObjectList(createParam.getConfiguration(),TreeNode.class));
        component.setTitle(createParam.getTitle());
        Integer userId = baseService.getCurrentUserId();
        component.setUserId(userId);
        int result = componentService.create(component);
        if(result > 0){
            return ResultData.success();
        }else{
            return ResultData.result(ResultCode.systemError);
        }
    }

    @RequestMapping("/component/update")
    public ResultData<Integer> update(@Validated @RequestBody ComponentUpdateParam createParam) {
        ResultCode resultCode = componentService.verify(createParam.getConfiguration());
        if(resultCode != ResultCode.success){
            return ResultData.result(resultCode);
        }
        Component component = new Component();
        component.setId(createParam.getId());
        component.setComponentType(createParam.getComponentType());
        component.setPrivateType(createParam.getPrivateType());
        component.setConfiguration(JsonUtil.toJavaObjectList(createParam.getConfiguration(),TreeNode.class));
        component.setTitle(createParam.getTitle());
        Integer userId = baseService.getCurrentUserId();
        component.setUserId(userId);
        int result = componentService.update(component);
        if(result > 0){
            return ResultData.success();
        }else{
            return ResultData.result(ResultCode.systemError);
        }
    }


    @RequestMapping("/component/deleteById")
    public ResultData<Integer> deleteById(@Validated @RequestBody IDParam idParam) {
        Integer id = idParam.getId();
        Component component = componentService.queryById(id);
        Validate.notNull(component);
        int result = componentService.delete(component);
        if(result > 0){
            return ResultData.success();
        }else{
            return ResultData.result(ResultCode.systemError);
        }
    }

    @PostMapping("/component/list")
    public ResultData<ListData<ComponentVO>> queryList(@Validated @RequestBody ListSearchObject<ComponentQueryParam> searchObject){
        Pagination pagination = searchObject.getPagination();
        ListData<ComponentVO> listData = componentService.queryList(searchObject.getQueryParams(),pagination.getPageNum(),pagination.getPageSize());
        return ResultData.success(listData);
    }

}
