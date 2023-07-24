package com.dtstep.lighthouse.web.init;
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
import com.dtstep.lighthouse.common.constant.StatConst;
import com.dtstep.lighthouse.common.constant.SysConst;
import com.dtstep.lighthouse.core.hbase.HBaseTableOperator;
import com.dtstep.lighthouse.core.lock.RedLock;
import com.dtstep.lighthouse.web.manager.department.DepartmentManager;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.concurrent.TimeUnit;

@Configuration
@ComponentScan("com.dtstep.lighthouse")
public class InitialListener implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(InitialListener.class);

    @Autowired
    private DepartmentManager departmentManager;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event){
        try{
            hbaseDBInit();
        }catch (Exception ex){
            logger.error("system storage table initialization failed,process exit!",ex);
            System.exit(-1);
        }
        try{
            departmentInit();
        }catch (Exception ex){
            logger.error("system department initialization failed,process exit!",ex);
            System.exit(-1);
        }
        logger.info("System initialization complete!");
    }


    private void hbaseDBInit() throws Exception{
        String namespace = HBaseTableOperator.getDefaultNamespace();
        HBaseTableOperator.createNamespaceIfNotExist(namespace);
        HBaseTableOperator.createTableIfNotExist(StatConst.DIMENS_STORAGE_TABLE, SysConst._DIMENS_STORAGE_PRE_PARTITIONS_SIZE);
        HBaseTableOperator.createTableIfNotExist(StatConst.SYSTEM_STAT_RESULT_TABLE,SysConst._DATA_STORAGE_PRE_PARTITIONS_SIZE);
        Validate.isTrue(HBaseTableOperator.isTableExist(StatConst.DIMENS_STORAGE_TABLE));
        Validate.isTrue(HBaseTableOperator.isTableExist(StatConst.SYSTEM_STAT_RESULT_TABLE));
    }

    private void departmentInit() throws Exception {
        if(departmentManager.count() > 0){
            return;
        }
        String lockKey = "lock_init_web_department";
        boolean isLock = RedLock.tryLock(lockKey,1,1, TimeUnit.MINUTES);
        if(isLock){
            try{
                if(departmentManager.count() == 0){
                    departmentManager.init();
                }
            } finally {
                RedLock.unLock(lockKey);
            }
        }
    }
}
