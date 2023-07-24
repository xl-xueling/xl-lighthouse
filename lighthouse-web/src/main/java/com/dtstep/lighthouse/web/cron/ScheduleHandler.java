package com.dtstep.lighthouse.web.cron;
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
import com.dtstep.lighthouse.web.manager.meta.MetaTableManager;
import com.dtstep.lighthouse.common.entity.meta.MetaTableEntity;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.List;

@Controller
@EnableScheduling
public class ScheduleHandler {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleHandler.class);

    @Autowired
    private MetaTableManager metaTableManager;

    @Scheduled(cron = "10 1 1 * * ? ")
    public void updateResultTableContentSize(){
        List<MetaTableEntity> list = null;
        try{
            list = metaTableManager.queryAllResultTables();
        }catch (Exception ex){
            logger.error("query stat result table error!",ex);
        }
        if(CollectionUtils.isEmpty(list)){
            return;
        }
        for(MetaTableEntity metaTableEntity : list){
            String metaName = metaTableEntity.getMetaName();
            try{
                long contentSize = metaTableManager.getTableContentSize(metaName);
                metaTableManager.updateContentSize(metaName,contentSize);
                logger.info("update db table content size success,table:{},content size:{}",metaName,contentSize);
            }catch (Exception ex){
                logger.error("update db table content size error,table:{}!",metaName,ex);
            }
        }
    }
}
