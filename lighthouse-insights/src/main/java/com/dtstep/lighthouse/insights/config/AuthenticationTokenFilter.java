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
import com.dtstep.lighthouse.common.constant.SysConst;
import com.dtstep.lighthouse.common.enums.UserStateEnum;
import com.dtstep.lighthouse.common.util.StringUtil;
import com.dtstep.lighthouse.common.modal.User;
import com.dtstep.lighthouse.insights.service.SystemEnvService;
import com.dtstep.lighthouse.insights.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private SystemEnvService systemEnvService;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authToken = request.getHeader(SysConst.AUTH_ACCESS_PARAM);
        if (StringUtil.isEmptyOrNullStr(authToken)){
            filterChain.doFilter(request,response);
            return;
        }
        String secretKey = systemEnvService.getParam(SysConst.PARAM_SIGN_KEY);
        Jws<Claims> jws;
        try{
            jws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(authToken);
        }catch (Exception ex){
            logger.warn("JWT signature verification failed!");
            filterChain.doFilter(request,response);
            return;
        }
        if(jws == null){
            filterChain.doFilter(request,response);
            return;
        }
        Long expired = (Long)jws.getBody().get("expired");
        if(expired == null || expired <= System.currentTimeMillis()){
            filterChain.doFilter(request,response);
            return;
        }
        Integer id = (Integer) jws.getBody().get("id");
        UserStateEnum userStateEnum = userService.queryUserState(id);
        if(userStateEnum != UserStateEnum.USER_NORMAL){
            filterChain.doFilter(request,response);
            return;
        }
        String seed = (String) jws.getBody().get("seed");
        SeedAuthenticationToken authentication = new SeedAuthenticationToken(id,seed);
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        RepeatableRequestWrapper requestWrapper  = new RepeatableRequestWrapper(request);
        filterChain.doFilter(requestWrapper, response);
    }

}
