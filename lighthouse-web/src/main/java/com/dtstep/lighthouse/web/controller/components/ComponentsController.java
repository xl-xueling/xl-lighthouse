package com.dtstep.lighthouse.web.controller.components;
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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.dtstep.lighthouse.common.entity.components.ComponentsEntity;
import com.dtstep.lighthouse.common.entity.list.ListViewDataObject;
import com.dtstep.lighthouse.common.entity.user.UserEntity;
import com.dtstep.lighthouse.common.enums.result.RequestCodeEnum;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.web.param.ParamWrapper;
import com.dtstep.lighthouse.web.controller.base.BaseController;
import com.dtstep.lighthouse.web.service.components.ComponentsService;
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

@RestController
@ControllerAdvice
public class ComponentsController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(ComponentsController.class);

    @Autowired
    private ComponentsService componentsService;

    @RequestMapping("/components/list.shtml")
    public ModelAndView list(HttpServletRequest request, ModelMap model) throws Exception{
        String search = request.getParameter("search");
        int page = ParamWrapper.getIntValueOrElse(request, "page", 1);
        UserEntity currentUser = ParamWrapper.getCurrentUser(request);
        try{
            ListViewDataObject listObject = componentsService.queryListByPage(currentUser,page,search);
            model.addAttribute("listObject",listObject);
        }catch (Exception ex){
            logger.error("query components list error!",ex);
            throw ex;
        }
        model.addAttribute("search",search);
        return new ModelAndView("/components/components_list",model);
    }

    @RequestMapping("/components/custom.shtml")
    public @ResponseBody
    ObjectNode customList(HttpServletRequest request) throws Exception {
        int page = ParamWrapper.getIntValueOrElse(request, "page", 1);
        int pagesize = 10;
        int startIndex = (page - 1) * pagesize;
        int userId = ParamWrapper.getCurrentUserId(request);
        List<ComponentsEntity> componentsList = componentsService.queryComponentsList(userId,startIndex,pagesize);
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("code","0");
        objectNode.putPOJO("data", JsonUtil.valueToTree(componentsList));
        return objectNode;
    }

    @RequestMapping("/components/system.shtml")
    public @ResponseBody
    ObjectNode systemList() throws Exception{
        ArrayNode arrayNode = componentsService.querySystemComponentsList();
        Validate.notNull(arrayNode);
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("code","0");
        objectNode.putPOJO("data", arrayNode);
        return objectNode;
    }

    @RequestMapping("/components/data.shtml")
    public @ResponseBody
    ObjectNode data(HttpServletRequest request) throws Exception {
        int id = ParamWrapper.getIntValue(request,"id");
        ComponentsEntity componentsEntity = componentsService.queryById(id);
        Validate.notNull(componentsEntity);
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode result = objectMapper.createObjectNode();
        result.put("code","0");
        result.putPOJO("data", componentsEntity);
        return result;
    }

    @RequestMapping("/components/update/index.shtml")
    public ModelAndView update(HttpServletRequest request, ModelMap model) throws Exception{
        int componentsId = ParamWrapper.getIntValue(request,"id");
        ComponentsEntity componentsEntity = componentsService.queryById(componentsId);
        Validate.notNull(componentsEntity);
        model.addAttribute("componentsEntity",componentsEntity);
        return new ModelAndView("/components/components_update",model);
    }

    @RequestMapping("/components/register/index.shtml")
    public ModelAndView register() {
        return new ModelAndView("/components/components_create");
    }

    @RequestMapping("/components/check.shtml")
    public @ResponseBody
    ObjectNode check(HttpServletRequest request) throws Exception{
        String data = request.getParameter("data");
        int componentsType = ParamWrapper.getIntValue(request,"componentsType");
        try{
            componentsService.checkData(data,componentsType);
        }catch (Exception ex){
            return RequestCodeEnum.toJSON(ex);
        }
        ObjectNode objectNode = JsonUtil.createObjectNode();
        int level = componentsService.getLevel(data);
        JsonNode dataNode = JsonUtil.readTree(data);
        objectNode.put("level",level);
        objectNode.put("data", dataNode);
        objectNode.put("type",componentsType);
        ObjectNode result = JsonUtil.createObjectNode();
        result.put("code","0");
        result.putPOJO("data", objectNode);
        return result;
    }

    @RequestMapping("/components/create/submit.shtml")
    public @ResponseBody
    ObjectNode createSubmit(HttpServletRequest request) {
        String data = request.getParameter("data");
        String title = request.getParameter("title");
        int userId = ParamWrapper.getCurrentUserId(request);
        int componentsType = ParamWrapper.getIntValue(request,"componentsType");
        int privateFlag = ParamWrapper.getIntValueOrElse(request,"privateFlag",1);
        try{
            componentsService.checkData(data,componentsType);
        }catch (Exception ex){
            return RequestCodeEnum.toJSON(ex);
        }
        ComponentsEntity componentsEntity = new ComponentsEntity();
        componentsEntity.setData(data);
        componentsEntity.setType(componentsType);
        componentsEntity.setTitle(title);
        componentsEntity.setUserId(userId);
        componentsEntity.setPrivateFlag(privateFlag);
        try{
            componentsService.register(componentsEntity);
        }catch (Exception ex){
            logger.error("register components error,system error!",ex);
            return RequestCodeEnum.toJSON(RequestCodeEnum.SYSTEM_ERROR);
        }
        return RequestCodeEnum.toJSON(RequestCodeEnum.SUCCESS);
    }

    @RequestMapping("/components/update/submit.shtml")
    public @ResponseBody
    ObjectNode updateSubmit(HttpServletRequest request) {
        String data = request.getParameter("data");
        String title = request.getParameter("title");
        int userId = ParamWrapper.getCurrentUserId(request);
        int componentsType = ParamWrapper.getIntValue(request,"componentsType");
        int componentsId = ParamWrapper.getIntValue(request,"id");
        int privateFlag = ParamWrapper.getIntValueOrElse(request,"privateFlag",1);
        try{
            componentsService.checkData(data,componentsType);
        }catch (Exception ex){
            return RequestCodeEnum.toJSON(ex);
        }
        ComponentsEntity componentsEntity = new ComponentsEntity();
        componentsEntity.setData(data);
        componentsEntity.setId(componentsId);
        componentsEntity.setType(componentsType);
        componentsEntity.setTitle(title);
        componentsEntity.setUserId(userId);
        componentsEntity.setPrivateFlag(privateFlag);
        try{
            componentsService.update(componentsEntity);
        }catch (Exception ex){
            logger.error("update components error,system error!",ex);
            return RequestCodeEnum.toJSON(RequestCodeEnum.SYSTEM_ERROR);
        }
        return RequestCodeEnum.toJSON(RequestCodeEnum.SUCCESS);
    }

    @RequestMapping("/components/delete/submit.shtml")
    public @ResponseBody
    ObjectNode deleteSubmit(HttpServletRequest request) {
        int componentsId = ParamWrapper.getIntValue(request,"id");
        try{
            componentsService.delete(componentsId);
        }catch (Exception ex){
            logger.error("register components error,system error!",ex);
            return RequestCodeEnum.toJSON(RequestCodeEnum.SYSTEM_ERROR);
        }
        return RequestCodeEnum.toJSON(RequestCodeEnum.SUCCESS);
    }
}
