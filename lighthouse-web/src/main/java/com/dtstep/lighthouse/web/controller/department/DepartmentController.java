package com.dtstep.lighthouse.web.controller.department;
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
import com.dtstep.lighthouse.web.manager.project.ProjectManager;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.dtstep.lighthouse.common.constant.SysConst;
import com.dtstep.lighthouse.common.entity.department.DepartmentEntity;
import com.dtstep.lighthouse.common.entity.list.ListViewDataObject;
import com.dtstep.lighthouse.common.entity.tree.ZTreeViewNode;
import com.dtstep.lighthouse.common.enums.result.RequestCodeEnum;
import com.dtstep.lighthouse.common.enums.role.PrivilegeTypeEnum;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.web.manager.user.UserManager;
import com.dtstep.lighthouse.web.param.ParamWrapper;
import com.dtstep.lighthouse.web.controller.annotation.AuthorityPermission;
import com.dtstep.lighthouse.web.controller.base.BaseController;
import com.dtstep.lighthouse.web.service.department.DepartmentService;
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
import java.util.List;

@RestController
@ControllerAdvice
public class DepartmentController extends BaseController{

    private static final Logger logger = LoggerFactory.getLogger(DepartmentController.class);

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private ProjectManager projectManager;

    @Autowired
    private UserManager userManager;

    @AuthorityPermission(roleTypeEnum = PrivilegeTypeEnum.ADMIN)
    @RequestMapping("/department/list.shtml")
    public ModelAndView list(HttpServletRequest request, ModelMap model) throws Exception{
        String search = request.getParameter("search");
        int level = ParamWrapper.getIntValueOrElse(request, "level", -1);
        int page = ParamWrapper.getIntValueOrElse(request,"page",1);
        try{
            ListViewDataObject listObject = departmentService.queryListByPage(page,level, search);
            Validate.notNull(listObject);
            model.addAttribute("listObject",listObject);
        }catch (Exception ex){
            logger.error("query department list error,param[search:{},level:{}]!",search,level,ex);
            throw ex;
        }
        model.addAttribute("level",level);
        model.addAttribute("search", search);
        return new ModelAndView("/department/department_list",model);
    }

    @AuthorityPermission(roleTypeEnum = PrivilegeTypeEnum.ADMIN)
    @RequestMapping("/department/manage/index.shtml")
    public ModelAndView manage(HttpServletRequest request,ModelMap model) throws Exception {
        int pid = ParamWrapper.getIntValueOrElse(request,"pid",0);
        List<ZTreeViewNode> nodeList = departmentService.queryTreeInfo(pid);
        model.addAttribute("zNodeData", JsonUtil.valueToTree(nodeList));
        model.addAttribute("pid",pid);
        return new ModelAndView("/department/department_manage",model);
    }

    @AuthorityPermission(roleTypeEnum = PrivilegeTypeEnum.ADMIN)
    @RequestMapping("/department/create/index.shtml")
    public ModelAndView create(HttpServletRequest request,ModelMap model) throws Exception {
        int pid = ParamWrapper.getIntValueOrElse(request,"pid",0);
        if(pid != 0){
            DepartmentEntity parentDepart = departmentService.queryById(pid);
            Validate.notNull(parentDepart);
            model.addAttribute("parentDepart",parentDepart);
        }
        return new ModelAndView("/department/department_create",model);
    }

    @AuthorityPermission(roleTypeEnum = PrivilegeTypeEnum.ADMIN)
    @RequestMapping("/department/create/submit.shtml")
    public @ResponseBody
    ObjectNode createSubmit(HttpServletRequest request) throws Exception{
        String name = request.getParameter("name");
        int pid = ParamWrapper.getIntValue(request,"pid");
        ParamWrapper.valid(DepartmentEntity.class, "name", name);
        DepartmentEntity departmentEntity = new DepartmentEntity();
        departmentEntity.setPid(pid);
        departmentEntity.setName(name);
        int level = 1;
        if(pid != 0){
            DepartmentEntity parent = departmentService.queryById(pid);
            Validate.notNull(parent);
            level = parent.getLevel() + 1;
            departmentEntity.setLevel(level);
        }else{
            departmentEntity.setLevel(level);
        }
        if(level > SysConst.DEPARTMENT_LEVEL_LIMIT){
            logger.info("department level exceeds max level!");
            return RequestCodeEnum.toJSON(RequestCodeEnum.DEPARTMENT_LIMIT_MAX_LEVEL);
        }
        try{
            int id = departmentService.save(departmentEntity);
            logger.info("save department[id:{},name:{}] success!",id,departmentEntity.getName());
        }catch (Exception ex){
            logger.error("save department error!",ex);
            return RequestCodeEnum.toJSON(RequestCodeEnum.SYSTEM_ERROR);
        }
        return RequestCodeEnum.toJSON(RequestCodeEnum.SUCCESS);
    }

    @AuthorityPermission(roleTypeEnum = PrivilegeTypeEnum.ADMIN)
    @RequestMapping("/department/update/index.shtml")
    public ModelAndView update(HttpServletRequest request, ModelMap model) throws Exception{
        int id = ParamWrapper.getIntValue(request,"id");
        DepartmentEntity department = departmentService.queryById(id);
        Assert.notNull(department);
        model.addAttribute("department",department);
        int pid = department.getPid();
        DepartmentEntity parentDepart;
        if(pid != 0){
            parentDepart = departmentService.queryById(pid);
            model.addAttribute("parentDepart",parentDepart);
        }
        model.addAttribute("pid",pid);
        return new ModelAndView("/department/department_update",model);
    }

    @AuthorityPermission(roleTypeEnum = PrivilegeTypeEnum.ADMIN)
    @RequestMapping("/department/update/submit.shtml")
    public @ResponseBody
    ObjectNode updateSubmit(HttpServletRequest request) throws Exception{
        String name = request.getParameter("name");
        int id = ParamWrapper.getIntValue(request,"id");
        int pid = ParamWrapper.getIntValueOrElse(request,"pid",0);
        ParamWrapper.valid(DepartmentEntity.class, "name", name);
        Validate.isTrue(departmentService.isExist(id));
        DepartmentEntity departmentEntity = new DepartmentEntity();
        departmentEntity.setId(id);
        departmentEntity.setPid(pid);
        departmentEntity.setName(name);
        try{
            departmentService.update(departmentEntity);
        }catch (Exception ex){
            logger.error("update department info error!", ex);
            return RequestCodeEnum.toJSON(RequestCodeEnum.SYSTEM_ERROR);
        }
        logger.info("update department info success!");
        return RequestCodeEnum.toJSON(RequestCodeEnum.SUCCESS);
    }

    @AuthorityPermission(roleTypeEnum = PrivilegeTypeEnum.ADMIN)
    @RequestMapping("/department/delete/submit.shtml")
    public @ResponseBody
    ObjectNode delete(HttpServletRequest request) throws Exception {
        int id = ParamWrapper.getIntValue(request, "id");
        if(departmentService.countChild(id) > 0){
            logger.info("department[id:{}] has child nodes!",id);
            return RequestCodeEnum.toJSON(RequestCodeEnum.DEPARTMENT_DELETE_HAS_CHILD_NODE);
        }
        if(projectManager.countByDepartmentId(id) > 0){
            logger.info("department[id:{}] has child nodes!",id);
            return RequestCodeEnum.toJSON(RequestCodeEnum.DEPARTMENT_DELETE_HAS_STAT_PROJECT);
        }
        if(userManager.countByDepartmentId(id) > 0){
            logger.info("department[id:{}] has child nodes!",id);
            return RequestCodeEnum.toJSON(RequestCodeEnum.DEPARTMENT_DELETE_HAS_USER);
        }
        try{
            departmentService.delete(id);
        }catch (Exception ex){
            logger.error("delete department[id:{}] error!", id, ex);
            return RequestCodeEnum.toJSON(RequestCodeEnum.SYSTEM_ERROR);
        }
        logger.info("delete department[id:{}] success!",id);
        return RequestCodeEnum.toJSON(RequestCodeEnum.SUCCESS);
    }

}
