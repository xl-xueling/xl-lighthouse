package com.dtstep.lighthouse.core.dao;
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

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;


public final class DBConnectionSource {

    private static final Logger logger = LoggerFactory.getLogger(DBConnectionSource.class);

    private static final HashMap<String,BasicDataSource> dataSourceMap = new HashMap<>();

    public static void init(RDBMSConfiguration rdbmsConfiguration) {
        try {
            Properties p = new Properties();
            p.setProperty("driverClassName", rdbmsConfiguration.getDriverClassName());
            p.setProperty("url", rdbmsConfiguration.getConnectionURL());
            p.setProperty("username", rdbmsConfiguration.getConnectionUserName());
            p.setProperty("password", rdbmsConfiguration.getConnectionPassword());
            p.setProperty("maxActive", "-1");
            p.setProperty("maxIdle", "10");
            p.setProperty("maxWait", "1000");
            p.setProperty("removeAbandoned", "true");
            p.setProperty("removeAbandonedTimeout", "120");
            p.setProperty("testOnBorrow", "false");
            p.setProperty("logAbandoned", "true");
            p.setProperty("validationQuery", "SELECT 1");
            p.setProperty("testWhileIdle", "true");
            p.setProperty("timeBetweenEvictionRunsMillis", "30000");
            p.setProperty("minEvictableIdleTimeMillis", "180000");
            p.setProperty("numTestsPerEvictionRun", "3");
            BasicDataSource dataSource = BasicDataSourceFactory.createDataSource(p);
            dataSourceMap.put(rdbmsConfiguration.getDatabase(), dataSource);
        } catch (Exception e) {
            logger.error("get db connection error",e);
        }
    }

    public static synchronized BasicDataSource getBasicDataSource(RDBMSConfiguration rdbmsConfiguration) throws SQLException {
        String database = rdbmsConfiguration.getDatabase();
        Validate.notNull(database);
        if(!dataSourceMap.containsKey(database)){
            init(rdbmsConfiguration);
        }
        return dataSourceMap.get(database);
    }
}
