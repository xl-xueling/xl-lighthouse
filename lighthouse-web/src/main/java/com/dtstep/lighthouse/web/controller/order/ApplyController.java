package com.dtstep.lighthouse.web.controller.order;
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
import com.dtstep.lighthouse.web.manager.group.GroupManager;
import com.dtstep.lighthouse.web.manager.project.ProjectManager;
import com.dtstep.lighthouse.web.manager.stat.StatManager;
import com.dtstep.lighthouse.web.service.order.ApplyService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.dtstep.lighthouse.common.entity.group.GroupExtEntity;
import com.dtstep.lighthouse.common.entity.list.ListViewDataObject;
import com.dtstep.lighthouse.common.entity.order.OrderEntity;
import com.dtstep.lighthouse.common.entity.project.ProjectEntity;
import com.dtstep.lighthouse.common.entity.sitemap.SiteMapEntity;
import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.entity.user.UserEntity;
import com.dtstep.lighthouse.common.enums.order.OrderStateEnum;
import com.dtstep.lighthouse.common.enums.order.OrderTypeEnum;
import com.dtstep.lighthouse.common.enums.result.RequestCodeEnum;
import com.dtstep.lighthouse.common.enums.role.PrivilegeTypeEnum;
import com.dtstep.lighthouse.common.exception.ProcessException;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.common.util.Md5Util;
import com.dtstep.lighthouse.web.controller.base.BaseController;
import com.dtstep.lighthouse.web.param.ParamWrapper;
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

@RestController
@ControllerAdvice
public class ApplyController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(ApplyController.class);

    @Autowired
    private ApplyService applyService;

    @Autowired
    private StatManager statManager;

    @Autowired
    private ProjectManager projectManager;

    @Autowired
    private GroupManager groupManager;

    @RequestMapping("/order/submit.shtml")
    public @ResponseBody
    ObjectNode apply(HttpServletRequest request) throws Exception {
        UserEntity currentUser = ParamWrapper.getCurrentUser(request);
        int orderType = ParamWrapper.getIntValueOrElse(request,"orderType",-1);
        String params = ParamWrapper.getValue(request,"params");
        OrderTypeEnum orderTypeEnum = OrderTypeEnum.getOrderType(orderType);
        Validate.notNull(orderTypeEnum);
        JsonNode paramNode = JsonUtil.readTree(params);
        Validate.notNull(paramNode);
        OrderEntity orderEntity = new OrderEntity();
        if(orderTypeEnum == OrderTypeEnum.STAT_ACCESS){
            int statId = paramNode.get("statId").asInt();
            StatExtEntity statExtEntity = statManager.queryById(statId);
            Validate.notNull(statExtEntity);
            orderEntity.setUserId(currentUser.getId());
            orderEntity.setOrderType(orderTypeEnum.getType());
            orderEntity.setPrivilegeKId(statExtEntity.getProjectId());
            orderEntity.setPrivilegeType(PrivilegeTypeEnum.STAT_PROJECT_ADMIN.getPrivilegeType());
            orderEntity.setParams(params);
            String hash = Md5Util.getMD5(currentUser.getId() + "_" + orderTypeEnum.getType() + "_" + statId);
            orderEntity.setHash(hash);
        }else if(orderTypeEnum == OrderTypeEnum.PROJECT_ACCESS){
            int projectId = paramNode.get("projectId").asInt();
            ProjectEntity projectEntity = projectManager.queryById(projectId);
            Validate.notNull(projectEntity);
            orderEntity.setUserId(currentUser.getId());
            orderEntity.setOrderType(orderTypeEnum.getType());
            orderEntity.setPrivilegeKId(projectId);
            orderEntity.setPrivilegeType(PrivilegeTypeEnum.STAT_PROJECT_ADMIN.getPrivilegeType());
            orderEntity.setParams(params);
            String hash = Md5Util.getMD5(currentUser.getId() + "_" + orderTypeEnum.getType() + "_" + projectId);
            orderEntity.setHash(hash);
        }else if(orderTypeEnum == OrderTypeEnum.GROUP_THRESHOLD_ADJUST){
            int groupId = paramNode.get("groupId").asInt();
            GroupExtEntity groupExtEntity = groupManager.queryById(groupId);
            Validate.notNull(groupExtEntity);
            orderEntity.setUserId(currentUser.getId());
            orderEntity.setOrderType(orderTypeEnum.getType());
            orderEntity.setPrivilegeType(PrivilegeTypeEnum.ADMIN.getPrivilegeType());
            orderEntity.setParams(params);
            String hash = Md5Util.getMD5(currentUser.getId() + "_" + orderTypeEnum.getType() + "_" + groupId + "_" + System.currentTimeMillis());
            orderEntity.setHash(hash);
        }else {
            logger.error("not support apply type[{}]!",orderTypeEnum);
            throw new ProcessException();
        }
        try{
            applyService.createOrder(orderEntity);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return RequestCodeEnum.toJSON(RequestCodeEnum.SUCCESS);
    }

    @RequestMapping("/apply/list.shtml")
    public ModelAndView applyList(HttpServletRequest request, ModelMap model) throws Exception{
        int state = ParamWrapper.getIntValueOrElse(request,"state",-1);
        int page = ParamWrapper.getIntValueOrElse(request, "page", 1);
        UserEntity currentUser = ParamWrapper.getCurrentUser(request);
        try{
            ListViewDataObject listObject = applyService.queryListByPage(currentUser,page,state);
            model.addAttribute("listObject",listObject);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        model.addAttribute("state",state);
        return new ModelAndView("/order/order_apply",model);
    }

    @RequestMapping("/apply/retract.shtml")
    public @ResponseBody
    ObjectNode retract(HttpServletRequest request) throws Exception{
        int applyId = ParamWrapper.getIntValue(request,"id");
        OrderEntity orderEntity = applyService.queryById(applyId);
        Assert.notNull(orderEntity);
        int state = orderEntity.getState();
        if(state != OrderStateEnum.PEND.getState()){
            logger.info("order apply retract,the application is irrevocable.applyId:{},state:{}",applyId,state);
            return RequestCodeEnum.toJSON(RequestCodeEnum.APPLY_NOT_SUPPORT_RETRACT);
        }
        try{
            applyService.retract(applyId);
        }catch (Exception ex){
            logger.error("apply remove,system error,id:{}",applyId,ex);
            return RequestCodeEnum.toJSON(RequestCodeEnum.SYSTEM_ERROR);
        }
        return RequestCodeEnum.toJSON(RequestCodeEnum.SUCCESS);
    }

}
