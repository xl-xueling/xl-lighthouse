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
import com.dtstep.lighthouse.web.manager.order.OrderManager;
import com.dtstep.lighthouse.web.service.order.ApproveService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.dtstep.lighthouse.common.entity.list.ListViewDataObject;
import com.dtstep.lighthouse.common.entity.order.OrderEntity;
import com.dtstep.lighthouse.common.entity.user.UserEntity;
import com.dtstep.lighthouse.common.enums.result.RequestCodeEnum;
import com.dtstep.lighthouse.web.controller.base.BaseController;
import com.dtstep.lighthouse.web.param.ParamWrapper;
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

@RestController
@ControllerAdvice
public class ApproveController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(ApproveController.class);

    @Autowired
    private ApproveService approveService;

    @Autowired
    private OrderManager orderManager;

    @RequestMapping("/approve/list.shtml")
    public ModelAndView approveList(HttpServletRequest request, ModelMap model) throws Exception{
        int state = ParamWrapper.getIntValueOrElse(request,"state",-1);
        String search = request.getParameter("search");
        int page = ParamWrapper.getIntValueOrElse(request, "page", 1);
        UserEntity currentUser = ParamWrapper.getCurrentUser(request);
        try{
            ListViewDataObject listObject = approveService.queryListByPage(currentUser,page,state,search);
            model.addAttribute("listObject",listObject);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        model.addAttribute("state",state);
        model.addAttribute("search",search);
        return new ModelAndView("/order/order_approve",model);
    }

    @RequestMapping("/approve/agree.shtml")
    public @ResponseBody
    ObjectNode agree(HttpServletRequest request) throws Exception{
        int orderId = ParamWrapper.getIntValue(request,"orderId");
        UserEntity currentUser = ParamWrapper.getCurrentUser(request);
        OrderEntity orderEntity = orderManager.queryById(orderId);
        Validate.notNull(orderEntity);
        try{
            approveService.approve(currentUser,orderEntity);
        }catch (Exception ex){
            logger.error("order approve error!",ex);
        }
        return RequestCodeEnum.toJSON(RequestCodeEnum.SUCCESS);
    }

    @RequestMapping("/approve/refuse.shtml")
    public @ResponseBody
    ObjectNode refuse(HttpServletRequest request) throws Exception{
        int orderId = ParamWrapper.getIntValue(request,"orderId");
        UserEntity currentUser = ParamWrapper.getCurrentUser(request);
        OrderEntity orderEntity = orderManager.queryById(orderId);
        Validate.notNull(orderEntity);
        try{
            approveService.reject(currentUser,orderEntity);
        }catch (Exception ex){
            logger.error("order reject error!",ex);
        }
        return RequestCodeEnum.toJSON(RequestCodeEnum.SUCCESS);
    }
}
