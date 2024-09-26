package com.dtstep.lighthouse.insights.config;
/*
 * Copyright (C) 2022-2024 XueLing.雪灵
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
import com.dtstep.lighthouse.common.exception.PermissionException;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.common.util.StringUtil;
import com.dtstep.lighthouse.insights.controller.annotation.AuthPermission;
import com.dtstep.lighthouse.common.enums.RoleTypeEnum;
import com.dtstep.lighthouse.common.modal.Role;
import com.dtstep.lighthouse.insights.service.PermissionService;
import com.dtstep.lighthouse.insights.service.RoleService;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

@Component
public class PermissionInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(PermissionInterceptor.class);

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RoleService roleService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("Request URI:" + request.getRequestURI());
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            if (method.isAnnotationPresent(AuthPermission.class)) {
                boolean flag = false;
                AuthPermission[] authPermissions = method.getDeclaredAnnotationsByType(AuthPermission.class);
                for(AuthPermission authPermission : authPermissions){
                    if(hasPermission(authPermission,request)){
                        flag = true;
                        break;
                    }
                }
                if(!flag){throw new PermissionException();}
            }
        }
        return true;
    }

    private boolean hasPermission(AuthPermission authPermission,HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        RoleTypeEnum roleTypeEnum = authPermission.roleTypeEnum();
        String relatedParam = authPermission.relationParam();
        if(authentication.getClass() == SeedAuthenticationToken.class){
            Integer userId = ((SeedAuthenticationToken) authentication).getUserId();
            Role role = null;
            if(roleTypeEnum == RoleTypeEnum.OPT_MANAGE_PERMISSION){
                role = roleService.cacheQueryRole(roleTypeEnum,0);
            }else if(roleTypeEnum == RoleTypeEnum.FULL_MANAGE_PERMISSION){
                role = roleService.cacheQueryRole(roleTypeEnum,0);
            }else if(roleTypeEnum == RoleTypeEnum.PROJECT_MANAGE_PERMISSION
                    || roleTypeEnum == RoleTypeEnum.GROUP_MANAGE_PERMISSION
                    || roleTypeEnum == RoleTypeEnum.STAT_MANAGE_PERMISSION
                    || roleTypeEnum == RoleTypeEnum.METRIC_MANAGE_PERMISSION
                    || roleTypeEnum == RoleTypeEnum.VIEW_MANAGE_PERMISSION
                    || roleTypeEnum == RoleTypeEnum.CALLER_MANAGER_PERMISSION
            ){
                int id = getRelateParam(request,authPermission.relationParam());
                if(id != 0){
                    role = roleService.cacheQueryRole(roleTypeEnum,id);
                }
            }
            Validate.notNull(role);
            return permissionService.checkUserPermission(userId,role.getId());
        }
        return false;
    }

    private int getRelateParam(HttpServletRequest request,String relateParam){
        int id = 0;
        InputStream inputStream = null;
        try{
            inputStream = request.getInputStream();
            String requestBody = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            if(StringUtil.isNotEmpty(requestBody)){
                JsonNode objectNode = JsonUtil.readTree(requestBody);
                id = objectNode.get(relateParam).asInt();
                return id;
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            if(inputStream != null){
                try{
                    inputStream.close();
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        }
        return id;
    }
}