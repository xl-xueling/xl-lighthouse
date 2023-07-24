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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class ConnectionManager {

    private static final Logger logger = LoggerFactory.getLogger(ConnectionManager.class);

    private static final ThreadLocal<DBConnection> connectionHolder = new ThreadLocal<>();

    public static DBConnection getConnection() throws Exception{
        DBConnection dbConn = connectionHolder.get();
        if(dbConn == null){
            Connection conn = DBConnectionSource.getConnection();
            dbConn = new DBConnection(conn);
            connectionHolder.set(dbConn);
        }
        return dbConn;
    }


    public static void close(DBConnection dbConn) {
        if (dbConn != null && !dbConn.isTransactionSwitch()) {
            close(dbConn.getConnection());
            connectionHolder.remove();
        }
    }

    private static void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {logger.error("connection close error!",e);}
        }
    }

    public static void close(Statement st) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {logger.error("statement close error!",e);}
        }
    }

    public static void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {logger.error("result set close error!",e);}
        }
    }

    public static void beginTransaction(DBConnection dbConn) {
        try {
            if (dbConn != null) {
                int transactionLevel = dbConn.getTransactionLevel();
                dbConn.setTransactionLevel(++transactionLevel);
                if (dbConn.getConnection().getAutoCommit()) {
                    dbConn.setTransactionSwitch(true);
                    dbConn.getConnection().setAutoCommit(false);
                }
            }
        }catch(SQLException e) {logger.error("begin transaction error!",e);}
    }


    public static void commitTransaction(DBConnection dbConn) {
        try {
            if (dbConn != null) {
                int transactionLevel = dbConn.getTransactionLevel() - 1;
                dbConn.setTransactionLevel(transactionLevel);
                if (!dbConn.getConnection().getAutoCommit() && transactionLevel == 0) {
                    dbConn.setTransactionSwitch(false);
                    dbConn.getConnection().commit();
                }
            }
        }catch(SQLException e) {logger.error("commit transaction error!",e);}
    }


    public static void rollbackTransaction(DBConnection dbConn) {
        try {
            if (dbConn != null) {
                int transactionLevel = dbConn.getTransactionLevel() - 1;
                dbConn.setTransactionLevel(transactionLevel);
                if (!dbConn.getConnection().getAutoCommit() && transactionLevel == 0) {
                    dbConn.setTransactionSwitch(false);
                    dbConn.getConnection().rollback();
                }
            }
        }catch(SQLException e) {logger.error("rollback transaction error!",e);}
    }
}