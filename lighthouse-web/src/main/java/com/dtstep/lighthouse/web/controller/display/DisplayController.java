package com.dtstep.lighthouse.web.controller.display;
/*
 * Copyright (C) 2022-2023 XueLing.雪灵
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
import com.dtstep.lighthouse.web.controller.annotation.AuthorityPermission;
import com.dtstep.lighthouse.web.manager.components.ComponentsManager;
import com.dtstep.lighthouse.web.manager.department.DepartmentManager;
import com.dtstep.lighthouse.web.manager.privilege.PrivilegeManager;
import com.dtstep.lighthouse.web.manager.project.ProjectManager;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import com.dtstep.lighthouse.common.constant.StatConst;
import com.dtstep.lighthouse.common.entity.department.DepartmentEntity;
import com.dtstep.lighthouse.common.entity.group.GroupExtEntity;
import com.dtstep.lighthouse.common.entity.project.ProjectEntity;
import com.dtstep.lighthouse.common.entity.project.ProjectViewEntity;
import com.dtstep.lighthouse.common.entity.sitemap.SiteMapEntity;
import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.entity.tree.ZTreeViewNode;
import com.dtstep.lighthouse.common.entity.user.UserEntity;
import com.dtstep.lighthouse.common.enums.display.DisplayTypeEnum;
import com.dtstep.lighthouse.common.enums.result.RequestCodeEnum;
import com.dtstep.lighthouse.common.enums.role.PrivilegeTypeEnum;
import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.common.util.StringUtil;
import com.dtstep.lighthouse.core.batch.BatchAdapter;
import com.dtstep.lighthouse.web.manager.group.GroupManager;
import com.dtstep.lighthouse.web.manager.stat.StatManager;
import com.dtstep.lighthouse.web.param.ParamWrapper;
import com.dtstep.lighthouse.web.controller.base.BaseController;
import com.dtstep.lighthouse.web.service.display.DisplayService;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;


@RestController
@ControllerAdvice
public class DisplayController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(DisplayController.class);

    @Autowired
    private DisplayService displayService;

    @Autowired
    private StatManager statManager;

    @Autowired
    private PrivilegeManager privilegeManager;

    @Autowired
    private ProjectManager projectManager;

    @Autowired
    private GroupManager groupManager;

    @Autowired
    private DepartmentManager departmentManager;

    @Autowired
    private ComponentsManager componentsManager;

    @AuthorityPermission(relationParam = "statId",roleTypeEnum = PrivilegeTypeEnum.STAT_ITEM_USER)
    @AuthorityPermission(relationParam = "siteId",roleTypeEnum = PrivilegeTypeEnum.SITE_MAP_USER)
    @RequestMapping("/display/stat.shtml")
    public ModelAndView statView(HttpServletRequest request, ModelMap model) throws Exception{
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        int isSub = ParamWrapper.getIntValueOrElse(request,"isSub",0);
        int statId = ParamWrapper.getIntValue(request,"statId");
        StatExtEntity statExtEntity = statManager.actualQueryById(statId);
        Validate.notNull(statExtEntity);
        int projectId = statExtEntity.getProjectId();
        UserEntity currentUser = ParamWrapper.getCurrentUser(request);
        ProjectViewEntity projectViewEntity = projectManager.queryViewInfoById(currentUser,projectId);
        Validate.notNull(projectViewEntity);
        GroupExtEntity groupExtEntity = groupManager.queryById(statExtEntity.getGroupId());
        Validate.notNull(groupExtEntity);
        int departmentId = projectViewEntity.getDepartmentId();
        DepartmentEntity departmentEntity = departmentManager.queryViewInfoById(departmentId);
        TimeUnit timeParamUnit = statExtEntity.getTimeUnit();
        if(StringUtil.isEmpty(endDate) || StringUtil.isEmpty(startDate)){
            if(timeParamUnit == TimeUnit.SECONDS || timeParamUnit == TimeUnit.MINUTES || timeParamUnit == TimeUnit.HOURS){
                endDate = DateUtil.formatTimeStamp(System.currentTimeMillis(),"yyyy-MM-dd");
                startDate = endDate;
            }else{
                endDate = DateUtil.formatTimeStamp(System.currentTimeMillis(),"yyyy-MM-dd");
                startDate = DateUtil.formatTimeStamp(DateUtil.getDayBefore(System.currentTimeMillis(),14),"yyyy-MM-dd");
            }
        }
        ArrayNode arrayNode = displayService.queryPageFilterConfig(statExtEntity);
        model.addAttribute("filterParams", arrayNode.toString());
        List<DisplayTypeEnum> displayTypeList = displayService.getDisplayTypeList();
        model.addAttribute("startDate",startDate);
        model.addAttribute("endDate",endDate);
        model.addAttribute("projectEntity",projectViewEntity);
        model.addAttribute("groupEntity", groupExtEntity);
        model.addAttribute("statEntity", statExtEntity);
        model.addAttribute("departmentEntity",departmentEntity);
        model.addAttribute("displayTypeList",displayTypeList);
        model.addAttribute("displayTypeEnum",DisplayTypeEnum.getType(statExtEntity.getDisplayType()));
        if(isSub == 1){
            return new ModelAndView("/display/sub_display_stat",model);
        }else{
            return new ModelAndView("/display/display_stat",model);
        }
    }

    @RequestMapping("/display/filterConfig.shtml")
    public ModelAndView limited(HttpServletRequest request, ModelMap model){
        int userId = ParamWrapper.getCurrentUserId(request);
        int totalSize = 0;
        try{
            totalSize = componentsManager.count(userId);
        }catch (Exception ex){
            logger.error("get filter components size error,userId:{}",userId,ex);
        }
        model.addAttribute("filterComponentsSize",totalSize);
        return new ModelAndView("/display/popup_filtercomp",model);
    }


    @RequestMapping("/display/project.shtml")
    public ModelAndView projectView(HttpServletRequest request,ModelMap model) throws Exception{
        int projectId = ParamWrapper.getIntValue(request,"projectId");
        ProjectEntity projectEntity = projectManager.queryById(projectId);
        Validate.notNull(projectEntity);
        List<ZTreeViewNode> nodeList = displayService.queryProjectTreeInfo(projectEntity);
        model.addAttribute("zNodeData",JsonUtil.valueToTree(nodeList));
        model.addAttribute("projectEntity",projectEntity);
        return new ModelAndView("/display/display_project");
    }

    @AuthorityPermission(relationParam = "siteId",roleTypeEnum = PrivilegeTypeEnum.SITE_MAP_USER)
    @AuthorityPermission(relationParam = "statId",roleTypeEnum = PrivilegeTypeEnum.STAT_ITEM_USER)
    @RequestMapping("/display/limit.shtml")
    public ModelAndView limitView(HttpServletRequest request,ModelMap model) throws Exception{
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        int isSub = ParamWrapper.getIntValueOrElse(request,"isSub",0);
        int statId = ParamWrapper.getIntValue(request,"statId");
        StatExtEntity statExtEntity = statManager.queryById(statId);
        Validate.notNull(statExtEntity);
        int projectId = statExtEntity.getProjectId();
        UserEntity currentUser = ParamWrapper.getCurrentUser(request);
        ProjectViewEntity projectViewEntity = projectManager.queryViewInfoById(currentUser,projectId);
        Validate.notNull(projectViewEntity);
        int departmentId = projectViewEntity.getDepartmentId();
        DepartmentEntity departmentEntity = departmentManager.queryViewInfoById(departmentId);
        GroupExtEntity groupExtEntity = groupManager.queryById(statExtEntity.getGroupId());
        Validate.notNull(groupExtEntity);
        long periodStart = DateUtil.getDayStartTime(DateUtil.parseDate(startDate,"yyyy-MM-dd"));
        long periodEnd = DateUtil.getDayEndTime(DateUtil.parseDate(endDate,"yyyy-MM-dd"));
        List<Long> batchList = BatchAdapter.queryBatchTimeList(statExtEntity.getTimeParam(), periodStart, periodEnd);
        long curBatch = BatchAdapter.getBatch(statExtEntity.getTimeParamInterval(),statExtEntity.getTimeUnit(),System.currentTimeMillis());
        int curBatchIndex = batchList.indexOf(curBatch);
        model.addAttribute("curBatchIndex",curBatchIndex);
        List<DisplayTypeEnum> displayTypeList = Lists.newArrayList(DisplayTypeEnum.BarChart);
        model.addAttribute("startDate",startDate);
        model.addAttribute("endDate",endDate);
        model.addAttribute("projectEntity",projectViewEntity);
        model.addAttribute("groupEntity", groupExtEntity);
        model.addAttribute("statEntity", statExtEntity);
        model.addAttribute("departmentEntity",departmentEntity);
        model.addAttribute("displayTypeList",displayTypeList);
        model.addAttribute("displayTypeEnum",DisplayTypeEnum.BarChart);
        List<UserEntity> adminList = privilegeManager.queryAdmins(statExtEntity.getProjectId(), PrivilegeTypeEnum.STAT_PROJECT_ADMIN.getPrivilegeType());
        model.addAttribute("adminList",adminList);
        if(isSub == 1){
            return new ModelAndView("/display/sub_display_limit",model);
        }else{
            return new ModelAndView("/display/display_limit",model);
        }
    }

    @RequestMapping("/display/export/index.shtml")
    public ModelAndView export(ModelMap model){
        return new ModelAndView("/display/exportModal",model);
    }


    @AuthorityPermission(relationParam = "statId",roleTypeEnum = PrivilegeTypeEnum.STAT_ITEM_ADMIN)
    @RequestMapping("/display/changeFilterConfig.shtml")
    public @ResponseBody
    ObjectNode changeDisplay(HttpServletRequest request) throws Exception {
        int id = ParamWrapper.getIntValue(request,"statId");
        StatExtEntity statExtEntity = statManager.queryById(id);
        Assert.notNull(statExtEntity);
        GroupExtEntity groupExtEntity = groupManager.queryById(statExtEntity.getGroupId());
        Assert.notNull(groupExtEntity);
        String[] dimensArr = statExtEntity.getTemplateEntity().getDimensArr();
        if(ArrayUtils.isEmpty(dimensArr)){
            return RequestCodeEnum.toJSON(RequestCodeEnum.NO_FILTER_PARAMS_SELECTED);
        }
        List<String> columnNameList = Arrays.asList(statExtEntity.getTemplateEntity().getDimensArr());
        List<String> tempList = new ArrayList<>();
        String filterConfig = request.getParameter("data");
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(filterConfig);
        for(int i=0;i<jsonNode.size();i++){
            JsonNode subNode = jsonNode.get(i);
            String filter = subNode.get("filter").asText();
            String[] filterNameArr = filter.split(StatConst.DIMENS_SEPARATOR);
            for(String filterName : filterNameArr){
                if(!columnNameList.contains(filterName)){
                    logger.info("filter change,columnName not exist,statId:{},columnName:{}", statExtEntity.getId(),filterName);
                    return RequestCodeEnum.toJSON(RequestCodeEnum.COLUMN_NAME_NOT_EXIST,filterName);
                }else if(tempList.contains(filterName)){
                    logger.info("filter change,columnName duplicate,statId:{},columnName:{}", statExtEntity.getId(),filterName);
                    return RequestCodeEnum.toJSON(RequestCodeEnum.COLUMN_NAME_DUPLICATE,filterName);
                }else{
                    tempList.add(filterName);
                }
            }
        }
        try{
            statManager.changeFilterConfig(id,filterConfig);
        }catch (Exception ex){
            logger.error("change stat filter config info error,statId:{},data:{}",id,filterConfig,ex);
            return RequestCodeEnum.toJSON(RequestCodeEnum.SYSTEM_ERROR);
        }
        return RequestCodeEnum.toJSON(RequestCodeEnum.SUCCESS);
    }

    @AuthorityPermission(relationParam = "statId",roleTypeEnum = PrivilegeTypeEnum.STAT_ITEM_ADMIN)
    @RequestMapping("/display/resetFilterConfig.shtml")
    public @ResponseBody
    ObjectNode reset(HttpServletRequest request) throws Exception{
        int id = ParamWrapper.getIntValue(request,"statId");
        StatExtEntity statExtEntity = statManager.queryById(id);
        Assert.notNull(statExtEntity);
        try{
            statManager.changeFilterConfig(id,null);
        }catch (Exception ex){
            logger.error("reset stat filter config info error,statId:{}",id,ex);
            return RequestCodeEnum.toJSON(RequestCodeEnum.SYSTEM_ERROR);
        }
        return RequestCodeEnum.toJSON(RequestCodeEnum.SUCCESS);
    }
}
