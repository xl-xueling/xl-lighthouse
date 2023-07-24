package com.dtstep.lighthouse.web.controller.home;
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
import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.entity.user.UserEntity;
import com.dtstep.lighthouse.common.enums.order.OrderStateEnum;
import com.dtstep.lighthouse.common.enums.user.UserStateEnum;
import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.core.builtin.BuiltinLoader;
import com.dtstep.lighthouse.web.manager.project.ProjectManager;
import com.dtstep.lighthouse.web.service.order.ApproveService;
import com.dtstep.lighthouse.web.controller.base.BaseController;
import com.dtstep.lighthouse.web.manager.stat.StatManager;
import com.dtstep.lighthouse.web.manager.user.UserManager;
import com.dtstep.lighthouse.web.param.ParamWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@ControllerAdvice
public class HomeController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private ApproveService approveService;

    @Autowired
    private UserManager userManager;

    @Autowired
    private StatManager statManager;

    @Autowired
    private ProjectManager projectManager;

    @RequestMapping("/")
    public void homepage(HttpServletResponse response){
        try{
            response.sendRedirect("/index.shtml");
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @RequestMapping("/index.shtml")
    public ModelAndView index(HttpServletRequest request,ModelMap model) throws Exception{
        StatExtEntity fixStatExtEntity = BuiltinLoader.getBuiltinStat(1011);
        model.addAttribute("fixStatEntity", fixStatExtEntity);
        int yesterdayStatCount = 0;
        try{
            long yesterdayTime = DateUtil.getDayBefore(System.currentTimeMillis(),1);
            long startTime = DateUtil.getDayStartTime(yesterdayTime);
            long endTime = DateUtil.getDayEndTime(yesterdayTime);
            yesterdayStatCount = statManager.countWithDuration(startTime,endTime);
        }catch (Exception ex){
            logger.error("homepage index,countStatOfDate error",ex);
        }

        int totalStatCount = 0;
        try{
            totalStatCount = statManager.totalCount();
        }catch (Exception ex){
            logger.error("homepage index,totalStatCount error",ex);
        }

        int totalProjectCount = 0;
        try{
            totalProjectCount = projectManager.totalCount();
        }catch (Exception ex){
            logger.error("homepage index,countProjectCount error",ex);
        }

        int totalUserCount = 0;
        try{
            totalUserCount = userManager.countByState(UserStateEnum.USR_NORMAL);
        }catch (Exception ex){
            logger.error("homepage index,totalUserCount error",ex);
        }

        UserEntity currentUser = ParamWrapper.getCurrentUser(request);

        int approveCount = 0;
        try{
            approveCount = approveService.countApproveByParam(currentUser, OrderStateEnum.PEND.getState(), null);
        }catch (Exception ex){
            logger.error("homepage index,countApprove error",ex);
        }

        int pendUserCount = 0;
        try{
            pendUserCount = userManager.countByState(UserStateEnum.USER_PEND);
        }catch (Exception ex){
            logger.error("homepage index,countPendUser error",ex);
        }
        model.addAttribute("approveCount",approveCount);
        model.addAttribute("pendUserCount",pendUserCount);
        model.addAttribute("yesterdayStatCount",yesterdayStatCount);
        model.addAttribute("totalStatCount",totalStatCount);
        model.addAttribute("totalProjectCount",totalProjectCount);
        model.addAttribute("totalUserCount",totalUserCount);
        return new ModelAndView("/home/index",model);
    }

    @RequestMapping("/forbidden.shtml")
    public ModelAndView forbidden(HttpServletRequest request) {
        int isSub = ParamWrapper.getIntValueOrElse(request,"isSub",0);
        if(isSub == 1){
            return new ModelAndView("/common/sub_forbidden");
        }else{
            return new ModelAndView("/common/forbidden");
        }
    }

    @RequestMapping("/404.shtml")
    public ModelAndView error404() {
        return new ModelAndView("/common/404");
    }

    @RequestMapping("/error.shtml")
    public ModelAndView error() {
        return new ModelAndView("/common/error");
    }

    @RequestMapping("/license.shtml")
    public ModelAndView license() {
        return new ModelAndView("/common/license");
    }
}
