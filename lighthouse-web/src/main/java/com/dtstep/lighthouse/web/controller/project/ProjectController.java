package com.dtstep.lighthouse.web.controller.project;
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
import com.dtstep.lighthouse.web.manager.department.DepartmentManager;
import com.dtstep.lighthouse.web.manager.privilege.PrivilegeManager;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.dtstep.lighthouse.common.entity.department.DepartmentEntity;
import com.dtstep.lighthouse.common.entity.department.DepartmentViewEntity;
import com.dtstep.lighthouse.common.entity.group.GroupExtEntity;
import com.dtstep.lighthouse.common.entity.list.ListViewDataObject;
import com.dtstep.lighthouse.common.entity.project.ProjectEntity;
import com.dtstep.lighthouse.common.entity.tree.ZTreeViewNode;
import com.dtstep.lighthouse.common.entity.user.UserEntity;
import com.dtstep.lighthouse.common.enums.result.RequestCodeEnum;
import com.dtstep.lighthouse.common.enums.role.PrivilegeTypeEnum;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.common.util.StringUtil;
import com.dtstep.lighthouse.web.manager.group.GroupManager;
import com.dtstep.lighthouse.web.manager.stat.StatManager;
import com.dtstep.lighthouse.web.param.ParamWrapper;
import com.dtstep.lighthouse.web.controller.base.BaseController;
import com.dtstep.lighthouse.web.service.project.ProjectService;
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
import java.util.*;
import java.util.stream.Collectors;

@RestController
@ControllerAdvice
public class ProjectController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);

    @Autowired
    private ProjectService projectService;

    @Autowired
    private DepartmentManager departmentManager;

    @Autowired
    private PrivilegeManager privilegeManager;

    @Autowired
    private GroupManager groupManager;

    @Autowired
    private StatManager statManager;

    @RequestMapping("/project/create/index.shtml")
    public ModelAndView createIndex(HttpServletRequest request,ModelMap model) throws Exception {
        UserEntity userEntity = ParamWrapper.getCurrentUser(request);
        int departmentId = userEntity.getDepartmentId();
        try{
            List<DepartmentViewEntity> departmentList = departmentManager.queryListByPid(departmentId);
            model.addAttribute("departmentList",departmentList);
        }catch (Exception ex){
            logger.error("query department info [id:{}] error!",departmentId,ex);
            throw ex;
        }
        return new ModelAndView("/project/project_create",model);
    }

    @RequestMapping("/project/list.shtml")
    public ModelAndView list(HttpServletRequest request, ModelMap model) throws Exception{
        int owner = ParamWrapper.getIntValueOrElse(request,"owner",-1);
        String search = request.getParameter("search");
        int departmentId = ParamWrapper.getIntValueOrElse(request,"departmentId",-1);
        int page = ParamWrapper.getIntValueOrElse(request, "page", 1);
        UserEntity currentUser = ParamWrapper.getCurrentUser(request);
        try{
            ListViewDataObject listObject = projectService.queryListByPage(currentUser,page,owner == 1,departmentId,search);
            model.addAttribute("listObject",listObject);
        }catch (Exception ex){
            logger.error("query project list error!",ex);
            throw ex;
        }
        try{
            List<DepartmentViewEntity> departmentList = departmentManager.queryAllViewInfo();
            model.addAttribute("departmentList",departmentList);
        }catch (Exception ex){
            logger.error("query department list error!",ex);
            throw ex;
        }
        model.addAttribute("departmentId",departmentId);
        model.addAttribute("owner",owner);
        model.addAttribute("search",search);
        return new ModelAndView("/project/project_list",model);
    }

    @RequestMapping("/project/create/submit.shtml")
    public @ResponseBody
    ObjectNode createSubmit(HttpServletRequest request) throws Exception {
        String name = request.getParameter("name");
        String desc = request.getParameter("desc");
        int departmentId = ParamWrapper.getIntValueOrElse(request, "departmentId", -1);
        int privateFlag = ParamWrapper.getIntValueOrElse(request,"privateFlag",-1);
        ParamWrapper.valid(ProjectEntity.class,"name",name);
        ParamWrapper.valid(ProjectEntity.class,"desc",desc);
        ParamWrapper.valid(ProjectEntity.class,"departmentId",departmentId);
        ParamWrapper.valid(ProjectEntity.class,"privateFlag",privateFlag);
        String admins = request.getParameter("admins");
        Set<Integer> adminSet = new HashSet<>(Objects.requireNonNull(JsonUtil.toJavaObjectList(admins, Integer.class)));
        int userId = ParamWrapper.getCurrentUserId(request);
        adminSet.add(userId);
        if(!departmentManager.isExist(departmentId)){
            logger.info("project create,department id [{}] not exist!",departmentId);
            return RequestCodeEnum.toJSON(RequestCodeEnum.DEPARTMENT_ID_NOT_EXIST);
        }
        if(projectService.isExist(name)){
            logger.info("project create,name[{}] already exist!",name);
            return RequestCodeEnum.toJSON(RequestCodeEnum.PROJECT_NAME_ALREADY_EXIST);
        }
        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setUserId(userId);
        projectEntity.setName(name);
        projectEntity.setDesc(desc);
        projectEntity.setDepartmentId(departmentId);
        projectEntity.setPrivateFlag(privateFlag);
        int projectId;
        try{
            projectId = projectService.save(projectEntity,adminSet);
            logger.info("create project[id:{}] success!",projectId);
        }catch (Exception ex){
            logger.error("create project error!",ex);
            throw ex;
        }
        return RequestCodeEnum.toJSON(RequestCodeEnum.SUCCESS);
    }

    @AuthorityPermission(relationParam = "id",roleTypeEnum = PrivilegeTypeEnum.STAT_PROJECT_ADMIN)
    @RequestMapping("/project/update/index.shtml")
    public ModelAndView update(HttpServletRequest request, ModelMap model) throws Exception {
        int id = ParamWrapper.getIntValue(request,"id");
        ProjectEntity projectEntity = projectService.queryById(id);
        Validate.notNull(projectEntity);
        List<UserEntity> adminsList = privilegeManager.queryAdmins(id, PrivilegeTypeEnum.STAT_PROJECT_ADMIN.getPrivilegeType());
        model.addAttribute("adminsList",adminsList);
        UserEntity userEntity = ParamWrapper.getCurrentUser(request);
        Validate.notNull(userEntity);
        int currentUserDepartId = userEntity.getDepartmentId();
        List<DepartmentViewEntity> userDepartmentList = departmentManager.queryListByPid(currentUserDepartId);
        List<DepartmentEntity> departmentList = new ArrayList<>(userDepartmentList);
        int projectDepartmentId = projectEntity.getDepartmentId();
        if(!departmentList.stream().map(DepartmentEntity::getId).collect(Collectors.toList()).contains(projectDepartmentId)){
            DepartmentEntity originDepart = departmentManager.queryViewInfoById(projectDepartmentId);
            departmentList.add(originDepart);
        }
        model.addAttribute("projectEntity", projectEntity);
        model.addAttribute("departmentList",departmentList);
        return new ModelAndView("/project/project_update",model);
    }

    @AuthorityPermission(relationParam = "id",roleTypeEnum = PrivilegeTypeEnum.STAT_PROJECT_ADMIN)
    @RequestMapping("/project/update/submit.shtml")
    public @ResponseBody
    ObjectNode updateSubmit(HttpServletRequest request) throws Exception{
        String name = ParamWrapper.getValue(request, "name");
        int id = ParamWrapper.getIntValueOrElse(request,"id",-1);
        int departmentId = ParamWrapper.getIntValueOrElse(request,"departmentId",-1);
        int privateFlag = ParamWrapper.getIntValueOrElse(request,"privateFlag",-1);
        String desc = ParamWrapper.getValue(request, "desc");
        ParamWrapper.valid(ProjectEntity.class,"name",name);
        ParamWrapper.valid(ProjectEntity.class,"desc",desc);
        ParamWrapper.valid(ProjectEntity.class,"departmentId",departmentId);
        ParamWrapper.valid(ProjectEntity.class,"privateFlag",privateFlag);
        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setName(name);
        projectEntity.setDesc(desc);
        projectEntity.setDepartmentId(departmentId);
        projectEntity.setId(id);
        projectEntity.setPrivateFlag(privateFlag);
        Validate.isTrue(projectService.isExist(id));
        String admins = ParamWrapper.getValue(request,"admins");
        if(StringUtil.isEmpty(admins)){
            logger.info("project update,param[admins] is missing!");
            return RequestCodeEnum.toJSON(RequestCodeEnum.REQUEST_PARAM_MISSING);
        }
        Set<Integer> adminSet = new HashSet<>(Objects.requireNonNull(JsonUtil.toJavaObjectList(admins, Integer.class)));
        UserEntity currentUser = ParamWrapper.getCurrentUser(request);
        if(!privilegeManager.isSysAdmin(currentUser)){
            adminSet.add(currentUser.getId());
        }
        if(!departmentManager.isExist(departmentId)){
            logger.info("project update,department id [{}] not exist!",departmentId);
            return RequestCodeEnum.toJSON(RequestCodeEnum.DEPARTMENT_ID_NOT_EXIST);
        }
        try{
            projectService.update(projectEntity,adminSet);
        }catch (Exception ex){
            logger.error("project update,update project error!",ex);
            return RequestCodeEnum.toJSON(RequestCodeEnum.SYSTEM_ERROR);
        }
        return RequestCodeEnum.toJSON(RequestCodeEnum.SUCCESS);
    }

    @AuthorityPermission(relationParam = "id",roleTypeEnum = PrivilegeTypeEnum.STAT_PROJECT_ADMIN)
    @RequestMapping("/project/delete/submit.shtml")
    public @ResponseBody
    ObjectNode delete(HttpServletRequest request) throws Exception{
        int id = ParamWrapper.getIntValueOrElse(request,"id",-1);
        Validate.isTrue(projectService.isExist(id));
        if(groupManager.countByProjectId(id) > 0){
            logger.info("project delete,project[id:{}] has group exist!",id);
            return RequestCodeEnum.toJSON(RequestCodeEnum.PROJECT_HAVE_STAT_GROUP);
        }
        try{
            projectService.delete(id);
            logger.info("project delete,delete project[id:{}] success!", id);
        }catch (Exception ex){
            logger.info("project delete,delete project[id:{}] error!", id);
            return RequestCodeEnum.toJSON(RequestCodeEnum.SYSTEM_ERROR);
        }
        return RequestCodeEnum.toJSON(RequestCodeEnum.SUCCESS);
    }

    @AuthorityPermission(relationParam = "projectId",roleTypeEnum = PrivilegeTypeEnum.STAT_PROJECT_ADMIN)
    @RequestMapping("/project/manage/index.shtml")
    public ModelAndView manage(HttpServletRequest request,ModelMap model) throws Exception{
        int projectId = ParamWrapper.getIntValueOrElse(request, "projectId", -1);
        int groupId = ParamWrapper.getIntValueOrElse(request,"groupId",-1);
        if(groupId != -1){
            GroupExtEntity groupExtEntity = groupManager.queryById(groupId);
            Validate.notNull(groupExtEntity);
            Validate.isTrue(groupExtEntity.getProjectId() == projectId);
        }
        List<ZTreeViewNode> nodeList = projectService.queryZTreeInfo(projectId);
        model.addAttribute("zNodeData",JsonUtil.valueToTree(nodeList));
        model.addAttribute("projectId",projectId);
        model.addAttribute("groupId",groupId);
        return new ModelAndView("/project/project_manage",model);
    }

    @AuthorityPermission(relationParam = "projectId",roleTypeEnum = PrivilegeTypeEnum.STAT_PROJECT_USER)
    @RequestMapping("/project/hasItem.shtml")
    public @ResponseBody
    ObjectNode hasItem(HttpServletRequest request) throws Exception{
        int projectId = ParamWrapper.getIntValue(request,"projectId");
        int count = statManager.countByProjectId(projectId);
        if(count == 0){
            return RequestCodeEnum.toJSON(RequestCodeEnum.PROJECT_NOT_HAS_STAT_ITEM);
        }else{
            return RequestCodeEnum.toJSON(RequestCodeEnum.SUCCESS);
        }
    }
}
