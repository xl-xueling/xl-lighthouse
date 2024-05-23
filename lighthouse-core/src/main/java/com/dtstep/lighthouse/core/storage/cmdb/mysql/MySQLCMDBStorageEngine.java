package com.dtstep.lighthouse.core.storage.cmdb.mysql;

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

    private static final ThreadLocal<Connection> connectionHolder = new ThreadLocal<>();

    static {
        String driverClassName = LDPConfig.getVal("cmdb.storage.engine.javax.jdo.option.driverClassName");
        String connectionUrl = LDPConfig.getVal("cmdb.storage.engine.javax.jdo.option.ConnectionURL");
        String connectionUserName = LDPConfig.getVal("cmdb.storage.engine.javax.jdo.option.ConnectionUserName");
        String connectionPassword = LDPConfig.getVal("cmdb.storage.engine.javax.jdo.option.ConnectionPassword");
        RDBMSConfiguration mySQLConfiguration = new RDBMSConfiguration(driverClassName,connectionUrl,connectionUserName,connectionPassword);
        try{
            basicDataSource = DBConnectionSource.getBasicDataSource(mySQLConfiguration);
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
}
