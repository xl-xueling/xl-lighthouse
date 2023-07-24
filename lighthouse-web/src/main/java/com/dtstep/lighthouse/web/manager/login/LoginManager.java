package com.dtstep.lighthouse.web.manager.login;
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
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Service
public class LoginManager {

    private static final Logger logger = LoggerFactory.getLogger(LoginManager.class);

    public void refreshSession(HttpServletRequest request, UserEntity userEntity) throws Exception {
        HttpSession session = request.getSession();
        session.setAttribute("user",userEntity);
        Cookie[] cookie = request.getCookies();
        if(cookie != null){
            for (Cookie cook : cookie) {
                if(cook.getName().equalsIgnoreCase("ldp_language")){
                    session.setAttribute("ldp_language",cook.getValue());
                }
            }
        }
    }


    public void signout(HttpServletRequest request) throws Exception {
        Validate.notNull(request);
        if(request.getSession().getAttribute("user") != null){
            request.getSession().removeAttribute("user");
        }
    }
}
