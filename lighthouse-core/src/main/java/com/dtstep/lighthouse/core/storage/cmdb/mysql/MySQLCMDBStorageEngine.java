package com.dtstep.lighthouse.core.storage.cmdb.mysql;
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
import com.dtstep.lighthouse.common.exception.InitializationException;
import com.dtstep.lighthouse.core.config.LDPConfig;
import com.dtstep.lighthouse.core.dao.DBConnectionSource;
import com.dtstep.lighthouse.core.dao.RDBMSConfiguration;
import com.dtstep.lighthouse.core.storage.cmdb.CMDBStorageEngine;
import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Connection;

public class MySQLCMDBStorageEngine implements CMDBStorageEngine<Connection> {

    private static final Logger logger = LoggerFactory.getLogger(MySQLCMDBStorageEngine.class);

    private static final BasicDataSource basicDataSource;

    private static final RDBMSConfiguration rdbmsConfiguration;

    private static final ThreadLocal<Connection> connectionHolder = new ThreadLocal<>();

    static {
        String driverClassName = LDPConfig.getVal("cmdb.storage.engine.javax.jdo.option.driverClassName");
        String connectionUrl = LDPConfig.getVal("cmdb.storage.engine.javax.jdo.option.ConnectionURL");
        String connectionUserName = LDPConfig.getVal("cmdb.storage.engine.javax.jdo.option.ConnectionUserName");
        String connectionPassword = LDPConfig.getVal("cmdb.storage.engine.javax.jdo.option.ConnectionPassword");
        rdbmsConfiguration = new RDBMSConfiguration(driverClassName,connectionUrl,connectionUserName,connectionPassword);
        try{
            basicDataSource = DBConnectionSource.getBasicDataSource(rdbmsConfiguration);
        }catch (Exception ex){
            logger.error("init mysql warehouse connection error!",ex);
            throw new InitializationException("init mysql warehouse connection error!");
        }
    }

    @Override
    public Connection getConnection() throws Exception {
        Connection conn = connectionHolder.get();
        if (conn == null || conn.isClosed()) {
            conn = basicDataSource.getConnection();
            connectionHolder.set(conn);
        }
        return conn;
    }

    @Override
    public void closeConnection() throws Exception {
        Connection conn = connectionHolder.get();
        if (conn != null) {
            try{
                conn.close();
            }catch (Exception ex){
                ex.printStackTrace();
            }finally {
                connectionHolder.remove();
            }
        }
    }

    @Override
    public RDBMSConfiguration getConfiguration() throws Exception {
        return rdbmsConfiguration;
    }
}
