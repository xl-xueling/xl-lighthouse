package com.dtstep.lighthouse.web.manager.env;
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
import com.dtstep.lighthouse.common.entity.manage.EnvEntity;
import com.dtstep.lighthouse.core.dao.ConnectionManager;
import com.dtstep.lighthouse.core.dao.DBConnection;
import com.dtstep.lighthouse.web.dao.EnvDao;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EnvManager {

    private static final Logger logger = LoggerFactory.getLogger(EnvManager.class);

    @Autowired
    private EnvDao envDao;

    public void save(EnvEntity envEntity) throws Exception{
        Validate.notNull(envEntity);
        Validate.notNull(envEntity.getParam());
        Validate.notNull(envEntity.getValue());
        DBConnection dbConnection = ConnectionManager.getConnection();
        ConnectionManager.beginTransaction(dbConnection);
        try{
            envDao.delete(envEntity.getParam());
            envDao.save(envEntity);
            ConnectionManager.commitTransaction(dbConnection);
        }catch (Exception ex){
            logger.error("update env error,params:{}", envEntity.getParam(),ex);
            ConnectionManager.rollbackTransaction(dbConnection);
            throw ex;
        }finally {
            ConnectionManager.close(dbConnection);
        }
    }
}
