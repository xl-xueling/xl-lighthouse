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
import com.dtstep.lighthouse.common.modal.*;
import com.dtstep.lighthouse.common.entity.ListData;
import com.dtstep.lighthouse.common.entity.ResultCode;
import com.dtstep.lighthouse.insights.controller.annotation.AllowCallerAccess;
import com.dtstep.lighthouse.insights.controller.annotation.AuthPermission;
import com.dtstep.lighthouse.insights.dto.ChangeStatStateParam;
import com.dtstep.lighthouse.insights.dto.StatFilterConfigParam;
import com.dtstep.lighthouse.insights.dto.StatQueryParam;
import com.dtstep.lighthouse.common.enums.RoleTypeEnum;
import com.dtstep.lighthouse.insights.dto.StatRenderConfigParam;
import com.dtstep.lighthouse.insights.service.StatService;
import com.dtstep.lighthouse.insights.vo.ResultData;
import com.dtstep.lighthouse.insights.vo.StatVO;
import com.dtstep.lighthouse.insights.vo.StatExtendVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@ControllerAdvice
public class StatController {

    private static final Logger logger = LoggerFactory.getLogger(StatController.class);

    @Autowired
    private StatService statService;

    @RequestMapping("/stat/list")
    public ResultData<ListData<StatVO>> list(@Validated @RequestBody ListSearchObject<StatQueryParam> searchObject) {
        StatQueryParam queryParam = searchObject.getQueryParamOrDefault(new StatQueryParam());
        ListData<StatVO> listData = statService.queryList(queryParam,searchObject.getPagination().getPageNum(),searchObject.getPagination().getPageSize());
        return ResultData.success(listData);
    }

    @AuthPermission(roleTypeEnum = RoleTypeEnum.PROJECT_MANAGE_PERMISSION,relationParam = "projectId")
    @RequestMapping("/stat/create")
    public ResultData<Integer> create(@Validated @RequestBody Stat createParam) throws Exception {
        ResultCode resultCode = statService.create(createParam);
        return ResultData.result(resultCode);
    }

    @AuthPermission(roleTypeEnum = RoleTypeEnum.PROJECT_MANAGE_PERMISSION,relationParam = "projectId")
    @RequestMapping("/stat/update")
    public ResultData<Integer> update(@Validated @RequestBody Stat createParam) {
        ResultCode resultCode = statService.update(createParam);
        return ResultData.result(resultCode);
    }

    @AuthPermission(roleTypeEnum = RoleTypeEnum.PROJECT_MANAGE_PERMISSION,relationParam = "projectId")
    @AuthPermission(roleTypeEnum = RoleTypeEnum.OPT_MANAGE_PERMISSION)
    @RequestMapping("/stat/changeState")
    public ResultData<Integer> changeState(@Validated @RequestBody ChangeStatStateParam changeParam) throws Exception {
        Integer id = changeParam.getId();
        Stat stat = statService.queryById(id);
        Validate.notNull(stat);
        statService.changeState(stat,changeParam.getState());
        return ResultData.success();
    }

    @AuthPermission(roleTypeEnum = RoleTypeEnum.STAT_MANAGE_PERMISSION,relationParam = "id")
    @RequestMapping("/stat/deleteById")
    public ResultData<Integer> deleteById(@Validated @RequestBody IDParam idParam) throws Exception {
        Integer id = idParam.getId();
        Stat stat = statService.queryById(id);
        Validate.notNull(stat);
        int result = statService.delete(stat);
        if(result > 0){
            return ResultData.success(id);
        }else{
            return ResultData.result(ResultCode.systemError);
        }
    }

    @RequestMapping("/stat/queryById")
    public ResultData<StatExtendVO> queryById(@Validated @RequestBody IDParam idParam) throws Exception {
        Integer id = idParam.getId();
        StatVO stat = statService.queryById(id);
        if(stat == null){
            return ResultData.result(ResultCode.elementNotFound);
        }
        RenderConfig renderConfig = statService.getStatRenderConfig(stat);
        StatExtendVO statExtendDto = new StatExtendVO(stat);
        statExtendDto.setRenderConfig(renderConfig);
        Validate.notNull(stat);
        return ResultData.success(statExtendDto);
    }

    @RequestMapping("/stat/queryByIds")
    public ResultData<List<StatVO>> queryByIds(@Validated @RequestBody IDParams idParams) throws Exception {
        List<Integer> ids = idParams.getIds();
        List<StatVO> voList = statService.queryByIds(ids);
        if(CollectionUtils.isEmpty(voList)){
            return ResultData.result(ResultCode.elementNotFound);
        }
        return ResultData.success(voList);
    }

    @AuthPermission(roleTypeEnum = RoleTypeEnum.STAT_MANAGE_PERMISSION,relationParam = "id")
    @RequestMapping("/stat/filterConfig")
    public ResultData<Integer> filterConfig(@Validated @RequestBody StatFilterConfigParam filterConfigParam) throws Exception{
        Integer id = filterConfigParam.getId();
        StatVO stat = statService.queryById(id);
        Validate.notNull(stat);
        List<RenderFilterConfig> configList = filterConfigParam.getFilters();
        ResultCode resultCode = statService.filterConfig(stat,configList);
        return ResultData.result(resultCode);
    }


    @AuthPermission(roleTypeEnum = RoleTypeEnum.STAT_MANAGE_PERMISSION,relationParam = "id")
    @RequestMapping("/stat/filterReset")
    public ResultData<Integer> filterReset(@Validated @RequestBody IDParam idParam) throws Exception{
        Integer id = idParam.getId();
        Stat stat = statService.queryById(id);
        Validate.notNull(stat);
        RenderConfig renderConfig = stat.getRenderConfig();
        renderConfig.setFilters(null);
        ResultCode resultCode = statService.update(stat);
        return ResultData.result(resultCode);
    }

    @AuthPermission(roleTypeEnum = RoleTypeEnum.STAT_MANAGE_PERMISSION,relationParam = "id")
    @RequestMapping("/stat/renderConfig")
    public ResultData<Integer> renderConfig(@Validated @RequestBody StatRenderConfigParam renderConfigParam) throws Exception{
        Integer id = renderConfigParam.getId();
        StatVO stat = statService.queryById(id);
        Validate.notNull(stat);
        List<RenderChartConfig> chartConfigs = renderConfigParam.getCharts();
        ResultCode resultCode;
        if(CollectionUtils.isNotEmpty(chartConfigs)){
            resultCode = statService.chartsConfig(stat,chartConfigs);
        }else{
            resultCode = ResultCode.renderConfigConfigCannotBeEmpty;
        }
        return ResultData.result(resultCode);
    }


    @AuthPermission(roleTypeEnum = RoleTypeEnum.STAT_MANAGE_PERMISSION,relationParam = "id")
    @RequestMapping("/stat/renderReset")
    public ResultData<Integer> renderReset(@Validated @RequestBody IDParam idParam) throws Exception{
        Integer id = idParam.getId();
        Stat stat = statService.queryById(id);
        Validate.notNull(stat);
        RenderConfig renderConfig = stat.getRenderConfig();
        renderConfig.setCharts(null);
        renderConfig.setDatepicker(null);
        ResultCode resultCode = statService.update(stat);
        return ResultData.result(resultCode);
    }
}
