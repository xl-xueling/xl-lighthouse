package com.dtstep.lighthouse.web.controller.limited;
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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.dtstep.lighthouse.common.entity.limiting.LimitingEntity;
import com.dtstep.lighthouse.web.controller.base.BaseController;
import com.dtstep.lighthouse.web.param.ParamWrapper;
import com.dtstep.lighthouse.web.service.limited.LimitedService;
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
public class LimitedController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(LimitedController.class);

    @Autowired
    private LimitedService limitedService;

    @RequestMapping("/limited/group/index.shtml")
    public ModelAndView limitedGroup(HttpServletRequest request,ModelMap model){
        model.addAttribute("ptype","1");
        return new ModelAndView("/limited/popup_limited",model);
    }

    @RequestMapping("/limited/stat/index.shtml")
    public ModelAndView limitedStat(HttpServletRequest request,ModelMap model){
        model.addAttribute("ptype","2");
        return new ModelAndView("/limited/popup_limited",model);
    }

    @RequestMapping("/limited/update/index.shtml")
    public ModelAndView update(ModelMap model){
        return new ModelAndView("/limited/popup_threshold",model);
    }

    @RequestMapping("/limited/stat/list.shtml")
    public @ResponseBody
    ObjectNode statList(HttpServletRequest request) throws Exception{
        int statId = ParamWrapper.getIntValue(request,"statId");
        int page = ParamWrapper.getIntValueOrElse(request, "page", 1);
        List<LimitingEntity> limitedList = limitedService.queryStatLimitedListByPage(statId,page);
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("code","0");
        objectNode.putPOJO("data", limitedList);
        return objectNode;
    }

    @RequestMapping("/limited/group/list.shtml")
    public @ResponseBody
    ObjectNode groupList(HttpServletRequest request) throws Exception{
        int groupId = ParamWrapper.getIntValue(request,"groupId");
        int page = ParamWrapper.getIntValueOrElse(request, "page", 1);
        List<LimitingEntity> limitedList = limitedService.queryGroupLimitedListByPage(groupId,page);
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("code","0");
        objectNode.putPOJO("data", limitedList);
        return objectNode;
    }
}
