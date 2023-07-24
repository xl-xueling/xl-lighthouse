package com.dtstep.lighthouse.web.controller.stat;
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
import com.dtstep.lighthouse.web.manager.project.ProjectManager;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.dtstep.lighthouse.common.entity.department.DepartmentViewEntity;
import com.dtstep.lighthouse.common.entity.project.ProjectEntity;
import com.dtstep.lighthouse.common.entity.list.ListViewDataObject;
import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.entity.user.UserEntity;
import com.dtstep.lighthouse.common.enums.display.DisplayTypeEnum;
import com.dtstep.lighthouse.common.enums.result.RequestCodeEnum;
import com.dtstep.lighthouse.common.enums.role.PrivilegeTypeEnum;
import com.dtstep.lighthouse.common.enums.stat.StatStateEnum;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.web.param.ParamWrapper;
import com.dtstep.lighthouse.web.service.stat.StatService;
import com.dtstep.lighthouse.web.controller.base.BaseController;
import org.apache.commons.lang3.Validate;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
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


@RestController
@ControllerAdvice
public class StatController extends BaseController{

    private static final Logger logger = LoggerFactory.getLogger(StatController.class);

    @Autowired
    private StatService statService;

    @Autowired
    private ProjectManager projectManager;

    @Autowired
    private DepartmentManager departmentManager;

    @RequestMapping("/stat/list.shtml")
    public ModelAndView list(HttpServletRequest request, ModelMap model) throws Exception {
        String search = ParamWrapper.getValue(request,"search");
        int departmentId = ParamWrapper.getIntValueOrElse(request,"departmentId",-1);
        int projectId = ParamWrapper.getIntValueOrElse(request,"projectId",-1);
        int page = ParamWrapper.getIntValueOrElse(request, "page", 1);
        UserEntity currentUser = ParamWrapper.getCurrentUser(request);
        try{
            ListViewDataObject listObject = statService.queryListByPage(currentUser,page,departmentId,projectId,search);
            model.addAttribute("listObject",listObject);
        }catch (Exception ex){
            logger.error("query stat list error!",ex);
        }
        try{
            List<DepartmentViewEntity> departmentList = departmentManager.queryAllViewInfo();
            model.addAttribute("departmentList",departmentList);
        }catch (Exception ex){
            logger.error("query department list error!",ex);
        }

        try{
            List<ProjectEntity> projectList = projectManager.queryAll();
            model.addAttribute("projectList",projectList);
        }catch (Exception ex){
            logger.error("query project list error!",ex);
        }
        model.addAttribute("projectId",projectId);
        model.addAttribute("departmentId",departmentId);
        model.addAttribute("search",search);
        return new ModelAndView("/stat/stat_list",model);
    }

    @AuthorityPermission(relationParam = "id",roleTypeEnum = PrivilegeTypeEnum.STAT_ITEM_ADMIN)
    @RequestMapping("/stat/disable.shtml")
    public @ResponseBody
    ObjectNode disable(HttpServletRequest request) throws Exception{
        int id = ParamWrapper.getIntValue(request, "id");
        Validate.isTrue(statService.isExist(id));
        try{
            statService.changeState(id, StatStateEnum.STOPPED);
        }catch (Exception ex){
            logger.error("change stat[id:{}] state error!",id,ex);
            return RequestCodeEnum.toJSON(RequestCodeEnum.SYSTEM_ERROR);
        }
        logger.info("disable stat[id:{}] success!",id);
        return RequestCodeEnum.toJSON(RequestCodeEnum.SUCCESS);
    }

    @AuthorityPermission(relationParam = "id",roleTypeEnum = PrivilegeTypeEnum.STAT_ITEM_ADMIN)
    @RequestMapping("/stat/enable.shtml")
    public @ResponseBody
    ObjectNode enable(HttpServletRequest request) throws Exception{
        int id = ParamWrapper.getIntValue(request,"id");
        Validate.isTrue(statService.isExist(id));
        try{
            statService.changeState(id, StatStateEnum.RUNNING);
        }catch (Exception ex){
            logger.error("change stat[id:{}] state error!",id,ex);
            return RequestCodeEnum.toJSON(RequestCodeEnum.SYSTEM_ERROR);
        }
        logger.info("enable stat[id:{}] success!",id);
        return RequestCodeEnum.toJSON(RequestCodeEnum.SUCCESS);
    }

    @AuthorityPermission(relationParam = "id",roleTypeEnum = PrivilegeTypeEnum.STAT_ITEM_ADMIN)
    @RequestMapping("/stat/frozen.shtml")
    public @ResponseBody
    ObjectNode frozen(HttpServletRequest request) throws Exception{
        int id = ParamWrapper.getIntValue(request,"id");
        Validate.isTrue(statService.isExist(id));
        try{
            statService.changeState(id, StatStateEnum.FROZEN);
        }catch (Exception ex){
            logger.error("change stat[id:{}] state error!",id,ex);
            return RequestCodeEnum.toJSON(RequestCodeEnum.SYSTEM_ERROR);
        }
        logger.info("frozen stat[id:{}] success!",id);
        return RequestCodeEnum.toJSON(RequestCodeEnum.SUCCESS);
    }

    @AuthorityPermission(relationParam = "id",roleTypeEnum = PrivilegeTypeEnum.STAT_ITEM_ADMIN)
    @RequestMapping("/stat/delete.shtml")
    public @ResponseBody
    ObjectNode delete(HttpServletRequest request) throws Exception{
        int id = ParamWrapper.getIntValue(request,"id");
        Validate.isTrue(statService.isExist(id));
        try{
            statService.deleteById(id);
        }catch (Exception ex){
            logger.error("change stat[id:{}] state error!",id,ex);
            return RequestCodeEnum.toJSON(RequestCodeEnum.SYSTEM_ERROR);
        }
        logger.info("enable state success,id:{}",id);
        return RequestCodeEnum.toJSON(RequestCodeEnum.SUCCESS);
    }

    @AuthorityPermission(relationParam = "id",roleTypeEnum = PrivilegeTypeEnum.STAT_ITEM_ADMIN)
    @RequestMapping("/stat/change_display.shtml")
    public @ResponseBody
    ObjectNode changeDisplay(HttpServletRequest request) throws Exception{
        int id = ParamWrapper.getIntValue(request,"id");
        int displayType = ParamWrapper.getIntValueOrElse(request,"displayType",-1);
        DisplayTypeEnum displayTypeEnum = DisplayTypeEnum.getType(displayType);
        Validate.isTrue(statService.isExist(id));
        Validate.isTrue(displayTypeEnum != null);
        try{
            statService.changeDisplayType(id,displayTypeEnum);
        }catch (Exception ex){
            logger.error("change stat[id:{}] display type error!",id,ex);
        }
        logger.info("change stat[id:{}]display type success!",id);
        return RequestCodeEnum.toJSON(RequestCodeEnum.SUCCESS);
    }

    @RequestMapping("/stat/detail.shtml")
    public @ResponseBody
    ObjectNode detail(HttpServletRequest request) throws Exception{
        int id = ParamWrapper.getIntValue(request,"id");
        StatExtEntity statExtEntity = null;
        try{
            statExtEntity = statService.queryById(id);
        }catch (Exception ex){
            logger.error("query stat[id:{}] error!",id,ex);
        }
        ObjectNode objectNode = JsonUtil.createObjectNode();
        objectNode.put("code","0");
        objectNode.putPOJO("data", statExtEntity);
        return objectNode;
    }

    @RequestMapping("/stat/update/index.shtml")
    public ModelAndView update(ModelMap model){
        return new ModelAndView("/stat/stat_update",model);
    }

    @AuthorityPermission(relationParam = "statId",roleTypeEnum = PrivilegeTypeEnum.STAT_ITEM_ADMIN)
    @RequestMapping("/stat/update/submit.shtml")
    public @ResponseBody
    ObjectNode updateSubmit(HttpServletRequest request) throws Exception {
        int statId = ParamWrapper.getIntValue(request, "statId");
        int groupId = ParamWrapper.getIntValue(request, "groupId");
        int projectId = ParamWrapper.getIntValue(request, "projectId");
        String template = request.getParameter("template");
        String title = request.getParameter("title");
        int expire = ParamWrapper.getIntValueOrElse(request,"statExpire",1);
        Document document = Jsoup.parse(template,"", Parser.xmlParser());
        Elements elements = document.select("stat-item");
        Element element = elements.get(0);
        element.attr("title",title);
        StatExtEntity statExtEntity = new StatExtEntity();
        statExtEntity.setId(statId);
        statExtEntity.setGroupId(groupId);
        statExtEntity.setProjectId(projectId);
        statExtEntity.setTemplate(document.toString());
        statExtEntity.setTitle(title);
        statExtEntity.setDataExpire(expire);
        try{
            statService.update(statExtEntity);
        }catch (Exception ex){
            ex.printStackTrace();
            return RequestCodeEnum.toJSON(RequestCodeEnum.SYSTEM_ERROR);
        }
        return RequestCodeEnum.toJSON(RequestCodeEnum.SUCCESS);
    }

    @RequestMapping("/stat/quote/index.shtml")
    public ModelAndView quote(ModelMap model){
        return new ModelAndView("/stat/stat_quote",model);
    }
}
