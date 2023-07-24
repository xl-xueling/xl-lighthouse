package com.dtstep.lighthouse.core.dao;
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
import com.dtstep.lighthouse.core.config.LDPConfig;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;


public final class DBConnectionSource {

    private static final Logger logger = LoggerFactory.getLogger(DBConnectionSource.class);

    private static BasicDataSource dataSource = null;

    public DBConnectionSource() {}

    public static void init() {
        if (dataSource != null) {
            try {
                dataSource.close();
            } catch (Exception e) {
                logger.error("connection close error.",e);
            }
            dataSource = null;
        }

        try {
            Properties p = new Properties();
            p.setProperty("driverClassName", "com.mysql.cj.jdbc.Driver");
            p.setProperty("url", LDPConfig.getVal(LDPConfig.KEY_DB_ConnectionURL));
            p.setProperty("username", LDPConfig.getVal(LDPConfig.KEY_DB_ConnectionUserName));
            p.setProperty("password", LDPConfig.getVal(LDPConfig.KEY_DB_ConnectionPassword));
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
            dataSource = BasicDataSourceFactory.createDataSource(p);
        } catch (Exception e) {
            logger.error("get db connection error",e);
        }
    }

    public static synchronized Connection getConnection() throws SQLException {
        if (dataSource == null) {
            init();
        }
        Connection conn = null;
        if (dataSource != null) {
            conn = dataSource.getConnection();
        }
        return conn;
    }
}
