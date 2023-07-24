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
import com.dtstep.lighthouse.common.exception.PermissionException;
import com.dtstep.lighthouse.common.exception.ProcessException;
import com.dtstep.lighthouse.common.key.KeyGenerator;
import com.dtstep.lighthouse.common.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
public class SessionFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(SessionFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain){
        long logkey = KeyGenerator.generateId();
        String uri = request.getRequestURI();
        try{
            MDC.put("logkey",String.valueOf(logkey));
            MDC.put("uri",uri);
            if(uri.startsWith("/static/")
                    || uri.startsWith("/login/index.shtml")
                    || uri.startsWith("/login/signout.shtml")
                    || uri.startsWith("/user/register/index.shtml")
                    || uri.startsWith("/license.shtml")
                    || uri.startsWith("/forbidden.shtml")
                    || uri.startsWith("/404.shtml")
                    || uri.startsWith("/error.shtml")){
                filterChain.doFilter(request, response);
                return;
            }
            IntersectRequest intersectRequest;
            try{
                intersectRequest = new IntersectRequest(uri,request);
                intersectRequest.decode();
            }catch (ProcessException ex){
                logger.info("Request uri validation parameter missing,uri:{}",uri);
                response.sendRedirect("/404.shtml");
                return;
            }catch (PermissionException ex){
                logger.info("lighthouse web,request uri denied,uri:{}",uri);
                response.sendRedirect("/forbidden.shtml");
                return;
            }
            if(uri.startsWith("/login/submit.shtml")
                    ||uri.startsWith("/user/register/submit.shtml")){
                filterChain.doFilter(intersectRequest, response);
                return;
            }
            doFilter(intersectRequest, response,filterChain,uri);
        }catch (Exception ex){
            logger.error("process error!",ex);
        }finally {
            MDC.clear();
        }
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain,String uri) throws Exception{
        UserEntity userEntity = (UserEntity)request.getSession().getAttribute("user");
        if(userEntity != null){
            logger.info("receive request,url:{},params:{}",request.getRequestURI(), JsonUtil.toJSONString(request.getParameterMap()));
            filterChain.doFilter(request, response);
        }else{
            if(request.getHeader("x-requested-with") != null && "XMLHttpRequest".equals(request.getHeader("x-requested-with"))) {
                response.setHeader("sessionStatus","timeout");
            } else {
                response.sendRedirect("/login/index.shtml");
            }
        }
    }
}
