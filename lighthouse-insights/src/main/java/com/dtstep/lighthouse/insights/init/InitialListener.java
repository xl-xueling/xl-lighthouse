package com.dtstep.lighthouse.insights.init;
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
import com.dtstep.lighthouse.common.enums.user.UserStateEnum;
import com.dtstep.lighthouse.common.util.Md5Util;
import com.dtstep.lighthouse.commonv2.constant.SystemConstant;
import com.dtstep.lighthouse.insights.modal.User;
import com.dtstep.lighthouse.insights.service.SystemEnvService;
import com.dtstep.lighthouse.insights.service.UserService;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Configuration
@ComponentScan("com.dtstep.lighthouse")
public class InitialListener implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(InitialListener.class);

    @Autowired
    private SystemEnvService systemEnvService;

    @Autowired
    private UserService userService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try{
            systemEnvService.generateSignKeyIfNotExist();
        }catch (Exception ex){
            logger.error("failed to generate sign key!",ex);
            System.exit(-1);
        }

        try{
            if(!userService.isUserNameExist(SystemConstant.DEFAULT_ADMIN_USER)){
                User user = new User();
                user.setUsername(SystemConstant.DEFAULT_ADMIN_USER);
                user.setPassword(Md5Util.getMD5(SystemConstant.DEFAULT_PASSWORD));
                user.setState(UserStateEnum.USR_NORMAL);
                userService.create(user);
            }
        }catch (Exception ex){
            logger.error("Admin account initialization failed!",ex);
            System.exit(-1);
        }
    }
}
