package com.dtstep.lighthouse.web.controller.login;
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
import com.dtstep.lighthouse.web.controller.user.UserController;
import com.dtstep.lighthouse.web.service.login.LoginService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.dtstep.lighthouse.common.enums.result.RequestCodeEnum;
import com.dtstep.lighthouse.web.controller.base.BaseController;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;

@RestController
@ControllerAdvice
public class LoginController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private LoginService loginService;

    @RequestMapping(value = "/login/submit.shtml")
    public @ResponseBody
    ObjectNode loginSubmit(HttpServletRequest request) throws Exception {
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");
        Validate.notNull(userName);
        Validate.notNull(password);
        RequestCodeEnum requestCodeEnum;
        try{
            requestCodeEnum = loginService.login(request,userName,password);
        }catch (Exception ex){
            logger.error("user login error!",ex);
            requestCodeEnum = RequestCodeEnum.SYSTEM_ERROR;
        }
        return RequestCodeEnum.toJSON(requestCodeEnum);
    }

    @RequestMapping(value = "/login/index.shtml")
    public ModelAndView login() throws Exception {
        return new ModelAndView("/user/user_login");
    }

    @RequestMapping("/login/signout.shtml")
    public ModelAndView loginOut(HttpServletRequest request){
        try{
            loginService.signout(request);
        }catch (Exception ex){
            logger.error("user login out error!",ex);
        }
        return new ModelAndView("/user/user_login");
    }
}
