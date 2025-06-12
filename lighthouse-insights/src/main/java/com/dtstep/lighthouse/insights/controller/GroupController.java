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
import com.dtstep.lighthouse.common.constant.SysConst;
import com.dtstep.lighthouse.common.entity.ListData;
import com.dtstep.lighthouse.common.entity.ResultCode;
import com.dtstep.lighthouse.common.enums.LimitingStrategyEnum;
import com.dtstep.lighthouse.common.modal.*;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.insights.controller.annotation.AuthPermission;
import com.dtstep.lighthouse.insights.dto.*;
import com.dtstep.lighthouse.insights.vo.GroupVO;
import com.dtstep.lighthouse.insights.vo.ResultData;
import com.dtstep.lighthouse.common.enums.RoleTypeEnum;
import com.dtstep.lighthouse.insights.service.DomainService;
import com.dtstep.lighthouse.insights.service.GroupService;
import com.dtstep.lighthouse.insights.service.StatService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@ControllerAdvice
public class GroupController {

    @Autowired
    private GroupService groupService;

    @Autowired
    private StatService statService;

    @Autowired
    private DomainService domainService;

    @RequestMapping("/group/list")
    public ResultData<ListData<GroupVO>> list(@Validated @RequestBody ListSearchObject<GroupQueryParam> searchObject) throws Exception{
        GroupQueryParam queryParam = searchObject.getQueryParamOrDefault(new GroupQueryParam());
        ListData<GroupVO> listData = groupService.queryList(queryParam,searchObject.getPagination().getPageNum(),searchObject.getPagination().getPageSize());
        return ResultData.success(listData);
    }

    @AuthPermission(roleTypeEnum = RoleTypeEnum.PROJECT_MANAGE_PERMISSION,relationParam = "projectId")
    @RequestMapping("/group/create")
    public ResultData<ObjectNode> create(@Validated @RequestBody GroupCreateParam createParam) {
        GroupQueryParam countByTokenParam = new GroupQueryParam();
        Domain domain = domainService.queryDefault();
        String token = String.format("%s:%s",domain.getDefaultTokenPrefix(),createParam.getToken());
        countByTokenParam.setToken(token);
        int tokenCount = groupService.count(countByTokenParam);
        if(tokenCount > 0){
            return ResultData.result(ResultCode.createGroupTokenExist,createParam.getToken());
        }
        GroupQueryParam countByProjectParam = new GroupQueryParam();
        countByProjectParam.setProjectId(createParam.getProjectId());
        int groupCount = groupService.count(countByProjectParam);
        if(groupCount >= SysConst.PROJECT_MAX_GROUP_SIZE){
            return ResultData.result(ResultCode.createGroupUnderProjectExceedLimit);
        }
        Group group = new Group();
        group.setToken(token);
        group.setColumns(createParam.getColumns());
        group.setDesc(createParam.getDesc());
        group.setProjectId(createParam.getProjectId());
        int id = groupService.create(group);
        ObjectNode objectNode = JsonUtil.createObjectNode();
        objectNode.put("id",id);
        objectNode.put("token",token);
        if(id > 0){
            return ResultData.success(objectNode);
        }else{
            return ResultData.result(ResultCode.systemError);
        }
    }

    @AuthPermission(roleTypeEnum = RoleTypeEnum.PROJECT_MANAGE_PERMISSION,relationParam = "projectId")
    @RequestMapping("/group/update")
    public ResultData<Integer> update(@Validated @RequestBody GroupUpdateParam updateParam) throws Exception{
        Integer id = updateParam.getId();
        Group group = groupService.queryById(id);
        group.setColumns(updateParam.getColumns());
        group.setDesc(updateParam.getDesc());
        int result = groupService.update(group);
        if(result > 0){
            return ResultData.success(result);
        }else{
            return ResultData.result(ResultCode.systemError);
        }
    }

    @AuthPermission(roleTypeEnum = RoleTypeEnum.GROUP_MANAGE_PERMISSION,relationParam = "id")
    @RequestMapping("/group/queryById")
    public ResultData<GroupVO> queryById(@Validated @RequestBody GroupQueryParam queryParam) throws Exception {
        GroupVO group = groupService.queryById(queryParam.getId());
        return ResultData.success(group);
    }

    @RequestMapping("/group/deleteById")
    public ResultData<Integer> deleteById(@Validated @RequestBody IDParam idParam) throws Exception{
        Group group = groupService.queryById(idParam.getId());
        Validate.notNull(group);
        StatQueryParam queryParam = new StatQueryParam();
        queryParam.setGroupIds(List.of(group.getId()));
        int countStat = statService.count(queryParam);
        if(countStat > 0){
            return ResultData.result(ResultCode.groupDelExistSubStat);
        }
        int result = groupService.delete(group);
        if(result > 0) {
            return ResultData.success(result);
        }else{
            return ResultData.result(ResultCode.systemError);
        }
    }

    @AuthPermission(roleTypeEnum = RoleTypeEnum.GROUP_MANAGE_PERMISSION,relationParam = "id")
    @RequestMapping("/group/getSecretKey")
    public ResultData<String> querySecretKey(@Validated @RequestBody IDParam idParam) {
        Integer id = idParam.getId();
        String secretKey = groupService.getSecretKey(id);
        return ResultData.success(secretKey);
    }

    @RequestMapping("/group/queryByProjectId")
    public ResultData<List<Group>> queryByProjectId(@Validated @RequestBody GroupQueryParam queryParam) {
        List<Group> groupList = groupService.queryByProjectId(queryParam.getProjectId());
        return ResultData.success(groupList);
    }

    @RequestMapping("/group/queryDimensList")
    public ResultData<List<String>> queryDimensList(@Validated @RequestBody IDParam idParam) throws Exception {
        Integer id = idParam.getId();
        List<String> dimensList = groupService.queryDimensList(id);
        return ResultData.success(dimensList);
    }

    @RequestMapping("/group/queryDimensValueList")
    public ResultData<List<String>> queryDimensValueList(@Validated @RequestBody GroupDimensQueryParam queryParam) throws Exception {
        Integer groupId = queryParam.getGroupId();
        String dimens = queryParam.getDimens();
        List<String> dimensValueList = groupService.queryDimensValueList(groupId,dimens);
        return ResultData.success(dimensValueList);
    }

    @RequestMapping("/group/deleteDimensValue")
    public ResultData<Integer> deleteDimensValue(@Validated @RequestBody List<DimensValueDeleteParam> deleteParams) throws Exception {
        groupService.deleteDimensValue(deleteParams);
        return ResultData.success(null);
    }

    @RequestMapping("/group/clearDimensValue")
    public ResultData<Integer> clearDimensValue(@Validated @RequestBody IDParam idParam) throws Exception {
        Integer groupId = idParam.getId();;
        groupService.clearDimensValue(groupId);
        return ResultData.success(null);
    }
}
