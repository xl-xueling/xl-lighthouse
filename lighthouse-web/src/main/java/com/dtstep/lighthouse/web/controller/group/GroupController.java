package com.dtstep.lighthouse.web.controller.group;
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
import com.dtstep.lighthouse.web.manager.project.ProjectManager;
import com.dtstep.lighthouse.web.manager.stat.StatManager;
import com.dtstep.lighthouse.web.service.group.GroupService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.dtstep.lighthouse.common.constant.StatConst;
import com.dtstep.lighthouse.common.entity.group.GroupEntity;
import com.dtstep.lighthouse.common.entity.group.GroupExtEntity;
import com.dtstep.lighthouse.common.entity.meta.MetaColumn;
import com.dtstep.lighthouse.common.entity.project.ProjectEntity;
import com.dtstep.lighthouse.common.entity.sitemap.SiteMapEntity;
import com.dtstep.lighthouse.common.entity.stat.StatEntity;
import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.entity.stat.TemplateEntity;
import com.dtstep.lighthouse.common.enums.function.FunctionEnum;
import com.dtstep.lighthouse.common.enums.result.RequestCodeEnum;
import com.dtstep.lighthouse.common.enums.role.PrivilegeTypeEnum;
import com.dtstep.lighthouse.common.enums.stat.StatStateEnum;
import com.dtstep.lighthouse.common.exception.ProcessException;
import com.dtstep.lighthouse.common.exception.TemplateParseException;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.common.util.StringUtil;
import com.dtstep.lighthouse.core.template.TemplateContext;
import com.dtstep.lighthouse.core.template.TemplateParser;
import com.dtstep.lighthouse.web.param.ParamWrapper;
import com.dtstep.lighthouse.web.controller.base.BaseController;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@ControllerAdvice
public class GroupController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(GroupController.class);

    @Autowired
    private GroupService groupService;

    @Autowired
    private ProjectManager projectManager;

    @Autowired
    private StatManager statManager;

    @AuthorityPermission(relationParam = "projectId",roleTypeEnum = PrivilegeTypeEnum.STAT_PROJECT_ADMIN)
    @RequestMapping("/group/create/index.shtml")
    public ModelAndView create(HttpServletRequest request,ModelMap model) throws Exception{
        int projectId = ParamWrapper.getIntValue(request,"projectId");
        ProjectEntity projectEntity = projectManager.queryById(projectId);
        Validate.notNull(projectEntity);
        model.addAttribute("projectEntity", projectEntity);
        return new ModelAndView("/group/group_create",model);
    }

    @AuthorityPermission(relationParam = "projectId",roleTypeEnum = PrivilegeTypeEnum.STAT_PROJECT_ADMIN)
    @RequestMapping("/group/create/submit.shtml")
    public @ResponseBody
    ObjectNode createSubmit(HttpServletRequest request) throws Exception{
        String token = ParamWrapper.getValue(request, "token");
        int projectId = ParamWrapper.getIntValue(request,"projectId");
        int statType = ParamWrapper.getIntValue(request,"statType");
        String remark = ParamWrapper.getValue(request,"remark");
        String columns = request.getParameter("columnArray");
        ParamWrapper.valid(GroupExtEntity.class,"token",token);
        ParamWrapper.valid(GroupExtEntity.class,"token",token);
        ParamWrapper.valid(GroupExtEntity.class,"projectId",projectId);
        ParamWrapper.valid(GroupExtEntity.class,"columns",columns);
        List<MetaColumn> columnList = JsonUtil.toJavaObjectList(columns,MetaColumn.class);
        Validate.isTrue(CollectionUtils.isNotEmpty(columnList));
        for (MetaColumn metaColumn : columnList) {
            String columnName = metaColumn.getColumnName();
            int columnType = metaColumn.getColumnType();
            ParamWrapper.valid(MetaColumn.class, "columnName", columnName);
            ParamWrapper.valid(MetaColumn.class,"columnType",columnType);
        }
        Set<String> columnSets = columnList.stream().map(MetaColumn::getColumnName).collect(Collectors.toSet());
        Set<String> interSect = Sets.intersection(columnSets,StatConst._KeyWordsSet);
        if(CollectionUtils.isNotEmpty(interSect)){
            String columnName =interSect.iterator().next();
            return RequestCodeEnum.toJSON(RequestCodeEnum.GROUP_COLUMN_PROHIBITED,columnName);
        }
        String itemArrayStr = ParamWrapper.getValue(request, "itemArray");
        JsonNode itemArray = JsonUtil.readTree(itemArrayStr);
        if(itemArray == null || itemArray.size() == 0){
            logger.info("create group,param[stat item] is missing!");
            return RequestCodeEnum.toJSON(RequestCodeEnum.REQUEST_PARAM_MISSING);
        }
        if(groupService.isExist(token)){
            logger.info("create group,token[{}] already exist!",token);
            return RequestCodeEnum.toJSON(RequestCodeEnum.GROUP_TOKEN_EXIST);
        }
        int userId = ParamWrapper.getCurrentUserId(request);
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setToken(token);
        groupEntity.setStatType(statType);
        groupEntity.setRemark(remark);
        groupEntity.setProjectId(projectId);
        groupEntity.setColumns(columns);
        List<StatEntity> statList = Lists.newArrayList();
        for(int i=0;i<itemArray.size();i++){
            JsonNode itemObj = itemArray.get(i);
            long dataExpire = itemObj.get("dataExpire").asLong();
            String timeParam = itemObj.get("timeParam").asText();
            String template = itemObj.get("template").asText();
            Validate.notNull(template);
            Validate.isTrue(dataExpire != -1);
            StatEntity statEntity = new StatEntity();
            statEntity.setTemplate(template);
            statEntity.setDataExpire(dataExpire);
            statEntity.setProjectId(projectId);
            statEntity.setTimeParam(timeParam);
            statEntity.setState(StatStateEnum.RUNNING.getState());
            try{
                TemplateEntity templateEntity = TemplateParser.parse(new TemplateContext(template,timeParam, columnList));
                if(templateEntity.getStatStateList().get(0).getFunctionEnum() == FunctionEnum.SEQ){
                    statEntity.setSequenceFlag(1);
                }
                String title = templateEntity.getTitle();
                statEntity.setTitle(title);
            }catch (TemplateParseException ex){
                logger.error("template parse error,template:{}",template);
                return RequestCodeEnum.toJSON(ex);
            } catch (Exception ex){
                logger.error("template parse error,template:{}",template);
                return RequestCodeEnum.toJSON(RequestCodeEnum.TEMPLATE_FORMAT_ERROR);
            }
            statList.add(statEntity);
        }
        if(CollectionUtils.isEmpty(statList)){
            logger.info("create group,stat item at least one.");
            return RequestCodeEnum.toJSON(RequestCodeEnum.STAT_AT_LEAST_ONE);
        }
        if(statList.size() > StatConst.GROUP_MAX_STAT_SIZE){
            logger.info("create group,stat items exceeds group limit.");
            return RequestCodeEnum.toJSON(RequestCodeEnum.GROUP_ITEMS_EXCEEDS_LIMIT);
        }
        int groupId;
        try{
            groupId = groupService.save(userId, groupEntity,statList);
        }catch (Exception ex){
            logger.info("create group,save group info system error.",ex);
            return RequestCodeEnum.toJSON(RequestCodeEnum.SYSTEM_ERROR);
        }
        logger.info("create group,save group info success,groupId:{}",groupId);
        return RequestCodeEnum.toJSON(RequestCodeEnum.SUCCESS);
    }

    @RequestMapping("/group/update/index.shtml")
    public ModelAndView update(HttpServletRequest request, ModelMap model) throws Exception{
        int groupId = ParamWrapper.getIntValue(request,"id");
        GroupExtEntity groupExtEntity = groupService.queryById(groupId);
        Validate.notNull(groupExtEntity);
        model.addAttribute("groupEntity", groupExtEntity);
        int projectId = groupExtEntity.getProjectId();
        ProjectEntity projectEntity = projectManager.queryById(projectId);
        model.addAttribute("projectEntity", projectEntity);
        try{
            List<StatExtEntity> statList = statManager.actualQueryListByGroupId(groupId);
            model.addAttribute("statList", statList);
        }catch (Exception ex){
            logger.error("group update,query group stat list error.groupId:{}",groupId,ex);
            throw new ProcessException();
        }
        return new ModelAndView("/group/group_update",model);
    }


    @AuthorityPermission(relationParam = "projectId",roleTypeEnum = PrivilegeTypeEnum.STAT_PROJECT_ADMIN)
    @RequestMapping("/group/update/submit.shtml")
    public @ResponseBody
    ObjectNode updateSubmit(HttpServletRequest request) throws Exception{
        int groupId = ParamWrapper.getIntValue(request,"groupId");
        String token = ParamWrapper.getValue(request, "groupToken");
        int statType = ParamWrapper.getIntValue(request,"statType");
        int projectId = ParamWrapper.getIntValue(request,"projectId");
        String remark = ParamWrapper.getValue(request,"remark");
        ParamWrapper.valid(GroupExtEntity.class,"token",token);
        String columns = request.getParameter("columnArray");
        ParamWrapper.valid(GroupExtEntity.class,"columns",columns);
        ObjectMapper objectMapper = new ObjectMapper();
        List<MetaColumn> columnList = objectMapper.readValue(columns, new TypeReference<>() {});
        Validate.isTrue(CollectionUtils.isNotEmpty(columnList));
        for (MetaColumn metaColumn : columnList) {
            String columnName = metaColumn.getColumnName();
            int columnType = metaColumn.getColumnType();
            ParamWrapper.valid(MetaColumn.class, "columnName", columnName);
            ParamWrapper.valid(MetaColumn.class,"columnType",columnType);
        }
        Set<String> columnSets = columnList.stream().map(MetaColumn::getColumnName).collect(Collectors.toSet());
        Set<String> interSect = Sets.intersection(columnSets,StatConst._KeyWordsSet);
        if(CollectionUtils.isNotEmpty(interSect)){
            String columnName =interSect.iterator().next();
            return RequestCodeEnum.toJSON(RequestCodeEnum.GROUP_COLUMN_PROHIBITED,columnName);
        }
        String itemArrayStr = ParamWrapper.getValue(request, "itemArray");
        if(StringUtil.isEmpty(itemArrayStr)){
            logger.info("update group,param is missing,items:{}",itemArrayStr);
            return RequestCodeEnum.toJSON(RequestCodeEnum.REQUEST_PARAM_MISSING);
        }
        JsonNode itemArray = objectMapper.readTree(itemArrayStr);
        if(itemArray == null || itemArray.size() == 0){
            logger.info("update group,param is missing,items:{}",itemArrayStr);
            return RequestCodeEnum.toJSON(RequestCodeEnum.REQUEST_PARAM_MISSING);
        }
        GroupExtEntity groupExtEntity = groupService.queryById(groupId);
        Validate.notNull(groupExtEntity);
        int userId = ParamWrapper.getCurrentUserId(request);
        groupExtEntity.setId(groupId);
        groupExtEntity.setToken(token);
        groupExtEntity.setStatType(statType);
        groupExtEntity.setRemark(remark);
        groupExtEntity.setProjectId(projectId);
        groupExtEntity.setColumns(columns);
        groupExtEntity.setColumnList(columnList);
        groupExtEntity.setCreateUser(userId);
        List<StatEntity> statList = Lists.newArrayList();
        for(int i=0;i<itemArray.size();i++){
            JsonNode itemObj = itemArray.get(i);
            int itemId = itemObj.get("itemId").asInt();
            long dataExpire = itemObj.get("dataExpire").asLong();
            String timeParam = itemObj.get("timeParam").asText();
            String template = itemObj.get("template").asText();
            Validate.notNull(template);
            Validate.isTrue(dataExpire != -1);
            StatEntity statEntity = new StatEntity();
            statEntity.setId(itemId);
            statEntity.setGroupId(groupId);
            statEntity.setTemplate(template);
            statEntity.setDataExpire(dataExpire);
            statEntity.setProjectId(projectId);
            statEntity.setTimeParam(timeParam);
            statEntity.setState(StatStateEnum.RUNNING.getState());
            try{
                TemplateEntity templateEntity = TemplateParser.parse(new TemplateContext(template,timeParam, groupExtEntity.getColumnList()));
                if(templateEntity.getStatStateList().get(0).getFunctionEnum() == FunctionEnum.SEQ){
                    statEntity.setSequenceFlag(1);
                }
                String title = templateEntity.getTitle();
                statEntity.setTitle(title);
            }catch (TemplateParseException ex){
                logger.info("update group information,template valid failed,id:{},template:{}",groupId,template);
                return RequestCodeEnum.toJSON(ex);
            } catch (Exception ex){
                logger.error("update group information,template valid failed,id:{},template:{}",groupId,template);
                return RequestCodeEnum.toJSON(RequestCodeEnum.TEMPLATE_FORMAT_ERROR);
            }
            statList.add(statEntity);
        }
        if(CollectionUtils.isEmpty(statList)){
            logger.info("update group,stat item at least one!");
            return RequestCodeEnum.toJSON(RequestCodeEnum.STAT_AT_LEAST_ONE);
        }
        if(statList.size() > StatConst.GROUP_MAX_STAT_SIZE){
            logger.info("update group,stat items exceeds group limit.");
            return RequestCodeEnum.toJSON(RequestCodeEnum.GROUP_ITEMS_EXCEEDS_LIMIT);
        }
        try{
            groupService.update(userId, groupExtEntity, statList);
        }catch (Exception ex){
            logger.error("update group,update system error.",ex);
            return RequestCodeEnum.toJSON(RequestCodeEnum.SYSTEM_ERROR);
        }
        return RequestCodeEnum.toJSON(RequestCodeEnum.SUCCESS);
    }

    @RequestMapping("/group/manage/index.shtml")
    public ModelAndView manage(HttpServletRequest request, ModelMap model) throws Exception {
        int groupId = ParamWrapper.getIntValue(request,"id");
        GroupExtEntity groupExtEntity = groupService.queryById(groupId);
        Validate.notNull(groupExtEntity);
        List<StatExtEntity> itemList = statManager.actualQueryListByGroupId(groupId);
        model.addAttribute("itemList",itemList);
        model.addAttribute("groupEntity", groupExtEntity);
        return new ModelAndView("/group/group_manage",model);
    }

    @AuthorityPermission(relationParam = "projectId",roleTypeEnum = PrivilegeTypeEnum.STAT_PROJECT_ADMIN)
    @RequestMapping("/group/delete/submit.shtml")
    public @ResponseBody
    ObjectNode deleteSubmit(HttpServletRequest request) throws Exception {
        int groupId = ParamWrapper.getIntValue(request,"groupId");
        if(statManager.countByGroupId(groupId) > 0){
            logger.info("delete group,group have stat item exist.groupId:{}", groupId);
            return RequestCodeEnum.toJSON(RequestCodeEnum.GROUP_HAVE_STAT_ITEM);
        }
        try{
            groupService.delete(groupId);
        }catch (Exception ex){
            logger.error("group delete submit,delete group system error.",ex);
            return RequestCodeEnum.toJSON(RequestCodeEnum.SYSTEM_ERROR);
        }
        return RequestCodeEnum.toJSON(RequestCodeEnum.SUCCESS);
    }

}
