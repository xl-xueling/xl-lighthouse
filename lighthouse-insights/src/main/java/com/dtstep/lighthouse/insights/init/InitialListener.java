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
import com.dtstep.lighthouse.insights.service.InitService;
import com.dtstep.lighthouse.insights.service.SystemEnvService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

@Configuration
@ComponentScan("com.dtstep.lighthouse")
public class InitialListener implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(InitialListener.class);

    @Autowired
    private SystemEnvService systemEnvService;

    @Autowired
    private InitService initService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try{
            systemEnvService.generateSignKeyIfNotExist();
        }catch (Exception ex){
            logger.error("failed to generate sign key!",ex);
            System.exit(-1);
        }

        try{
            initService.initRole();
        }catch (Exception ex){
            logger.error("Exception in initializing system roles!",ex);
            System.exit(-1);
        }

        try{
            initService.initDefaultDomain();
        }catch (Exception ex){
            logger.error("Exception in initialization default domain info!",ex);
            System.exit(-1);
        }

        try{
            initService.initDepartment();
        }catch (Exception ex){
            logger.error("Exception in initializing department info!",ex);
            System.exit(-1);
        }

        try{
            initService.initAdmin();
        }catch (Exception ex){
            logger.error("Admin account initialization failed!",ex);
            System.exit(-1);
        }

        try{
            initService.initStorageEngine();
        }catch (Exception ex){
            logger.error("Storage database initialization failed!",ex);
            System.exit(-1);
        }

        try{
            initService.createCMDBTablesIfNotExist();
            initService.createCMDBColumnsIfNotExist();
            initService.createCMDBIndexIfNotExist();
        }catch (Exception ex){
            logger.error("CMDB database upgrade failed!",ex);
            System.exit(-1);
        }
        logger.info("service init success!");
    }
}
