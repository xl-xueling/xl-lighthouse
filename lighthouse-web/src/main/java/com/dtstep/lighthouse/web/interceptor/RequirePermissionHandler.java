package com.dtstep.lighthouse.web.interceptor;
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
import com.dtstep.lighthouse.common.entity.user.UserEntity;
import com.dtstep.lighthouse.common.enums.role.PrivilegeTypeEnum;
import com.dtstep.lighthouse.common.exception.PermissionException;
import com.dtstep.lighthouse.core.config.LDPConfig;
import com.dtstep.lighthouse.web.manager.privilege.PrivilegeManager;
import com.dtstep.lighthouse.web.param.ParamWrapper;
import com.dtstep.lighthouse.web.controller.annotation.AuthorityPermission;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@RestController
public class RequirePermissionHandler implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(RequirePermissionHandler.class);

    @Autowired
    private PrivilegeManager privilegeManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        UserEntity userEntity = ParamWrapper.getCurrentUser(request);
        long startTime = System.currentTimeMillis();
        request.setAttribute("_start", startTime);
        if(!(handler instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        int isView = method.isAnnotationPresent(ResponseBody.class) ? 0 : 1;
        request.setAttribute("isView",isView);
        request.setAttribute("isSub",ParamWrapper.getIntValueOrElse(request,"isSub",0));
        if(LDPConfig.AUTHORITY_VERIFY_SWITCH && userEntity != null && handler instanceof HandlerMethod){
            AuthorityPermission[] authorityPermissions = handlerMethod.getMethod().getDeclaredAnnotationsByType(AuthorityPermission.class);
            if(ArrayUtils.isNotEmpty(authorityPermissions)){
                boolean hasRole = false;
                for(AuthorityPermission permission : authorityPermissions){
                    String relationParam = permission.relationParam();
                    PrivilegeTypeEnum privilegeTypeEnum = permission.roleTypeEnum();
                    if(privilegeTypeEnum == PrivilegeTypeEnum.ADMIN){
                        hasRole = privilegeManager.isSysAdmin(userEntity);
                    }else if(privilegeTypeEnum == PrivilegeTypeEnum.USER){
                        hasRole = !privilegeManager.isSysAdmin(userEntity);
                    }else{
                        int relationB = ParamWrapper.getIntValueOrElse(request,relationParam,-1);
                        hasRole = privilegeManager.hasRole(userEntity,relationB, privilegeTypeEnum.getPrivilegeType());
                    }
                    if(hasRole){
                        break;
                    }
                }
                if(hasRole){
                    return true;
                }else{
                    throw new PermissionException();
                }
            }
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        long endTime = System.currentTimeMillis();
        long startTime = (Long)request.getAttribute("_start");
        String uri = request.getRequestURI();
        if(uri.endsWith(".shtml")){
            logger.info("total cost:{}",(endTime - startTime));
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }
}
