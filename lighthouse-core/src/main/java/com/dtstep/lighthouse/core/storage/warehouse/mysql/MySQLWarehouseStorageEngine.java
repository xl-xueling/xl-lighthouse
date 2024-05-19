package com.dtstep.lighthouse.core.storage.warehouse.mysql;

import com.dtstep.lighthouse.common.exception.InitializationException;
import com.dtstep.lighthouse.common.modal.Column;
import com.dtstep.lighthouse.core.config.LDPConfig;
import com.dtstep.lighthouse.core.dao.DBConnectionSource;
import com.dtstep.lighthouse.core.dao.RDBMSConfiguration;
import com.dtstep.lighthouse.core.storage.*;
import com.dtstep.lighthouse.core.storage.warehouse.WarehouseStorageEngine;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

public class MySQLWarehouseStorageEngine implements WarehouseStorageEngine {

    private static final Logger logger = LoggerFactory.getLogger(MySQLWarehouseStorageEngine.class);

    private static final BasicDataSource basicDataSource;

    private static final RDBMSConfiguration mySQLConfiguration;

    private static final ThreadLocal<Connection> connectionHolder = new ThreadLocal<>();

    static {
        String driverClassName = LDPConfig.getVal("warehouse.storage.engine.javax.jdo.option.driverClassName");
        String connectionUrl = LDPConfig.getVal("warehouse.storage.engine.javax.jdo.option.ConnectionURL");
        String connectionUserName = LDPConfig.getVal("warehouse.storage.engine.javax.jdo.option.ConnectionUserName");
        String connectionPassword = LDPConfig.getVal("warehouse.storage.engine.javax.jdo.option.ConnectionPassword");
        mySQLConfiguration = new RDBMSConfiguration(driverClassName,connectionUrl,connectionUserName,connectionPassword);
        try{
            basicDataSource = DBConnectionSource.getBasicDataSource(mySQLConfiguration);
        }catch (Exception ex){
            logger.error("init mysql warehouse connection error!",ex);
            throw new InitializationException("init mysql warehouse connection error!");
        }
    }

    private Connection getConnection() throws Exception {
        Connection conn = connectionHolder.get();
        if (conn == null || conn.isClosed()) {
            conn = basicDataSource.getConnection();
            connectionHolder.set(conn);
        }
        return conn;
    }

    private void closeConnection() throws Exception {
        Connection conn = connectionHolder.get();
        if (conn != null) {
            conn.close();
            connectionHolder.remove();
        }
    }

    @Override
    public String getDefaultNamespace() {
        return null;
    }

    @Override
    public void createNamespaceIfNotExist(String namespace) throws Exception {}

    @Override
    public void createTable(String tableName) throws Exception {
        Connection connection = null;
        Statement statement = null;
        try{
            connection = getConnection();
            String createTableSQL = String.format("CREATE TABLE IF NOT EXISTS %s ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "key VARCHAR(100) NOT NULL, "
                    + "value bigint DEFAULT '0', "
                    + "expired timestamp NOT NULL"
                    + ")",tableName);
            statement = connection.createStatement();
            statement.execute(createTableSQL);
            logger.info("Mysql Table '{}' created successfully!",tableName);
        } catch (SQLException ex) {
            logger.error("Mysql Table '{}' created failed!",tableName,ex);
        } finally {
            try {
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    @Override
    public boolean isTableExist(String tableName) throws Exception {
        String query = "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = ? AND table_name = ?";
        Connection connection = null;
        PreparedStatement ps = null;
        try{
            connection = getConnection();
            ps = connection.prepareStatement(query);
            ps.setString(1, mySQLConfiguration.getDatabase());
            ps.setString(2, tableName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }catch (Exception ex){
            logger.error("check mysql table exist error!",ex);
        }finally {
            try {
                if (ps != null) ps.close();
                if (connection != null) connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public void dropTable(String tableName) throws Exception {

    }

    @Override
    public void put(String tableName, LdpPut ldpPut) throws Exception {

    }

    @Override
    public void puts(String tableName, List<LdpPut> ldpPuts) throws Exception {

    }

    @Override
    public void increment(String tableName, LdpIncrement ldpIncrement) throws Exception {

    }

    @Override
    public void increments(String tableName, List<LdpIncrement> ldpIncrements) throws Exception {

    }

    @Override
    public void putsWithCompare(String tableName, CompareOperator compareOperator, List<LdpPut> ldpPuts) throws Exception {

    }

    @Override
    public <R> LdpResult<R> get(String tableName, LdpGet ldpGet, Class<R> clazz) throws Exception {
        return null;
    }

    @Override
    public <R> List<LdpResult<R>> gets(String tableName, List<LdpGet> ldpGets, Class<R> clazz) throws Exception {
        return null;
    }

    @Override
    public <R> List<LdpResult<R>> scan(String tableName, String startRow, String endRow, int limit, Class<R> clazz) throws Exception {
        return null;
    }

    @Override
    public void delete(String tableName, String key) throws Exception {

    }

    @Override
    public long getMaxRecordSize() {
        return 0;
    }

    @Override
    public long getMaxContentSize() {
        return 0;
    }

    @Override
    public long getMaxTimeInterval() {
        return 0;
    }

    @Override
    public long getRecordSize(String tableName) {
        return 0;
    }

    @Override
    public long getContentSize(String tableName) {
        return 0;
    }
}
