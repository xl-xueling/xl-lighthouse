package com.dtstep.lighthouse.core.tools;

import com.dtstep.lighthouse.core.storage.cmdb.CMDBStorageEngine;
import com.dtstep.lighthouse.core.storage.cmdb.CMDBStorageEngineProxy;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class CMDBUtil {

    private static final Logger logger = LoggerFactory.getLogger(CMDBUtil.class);

    public static void addColumnIfNotExist(String tableName, String columnName, String type) throws Exception {
        CMDBStorageEngine<Connection> storageEngine = CMDBStorageEngineProxy.getInstance();
        Connection connection = null;
        PreparedStatement checkStmt = null;
        Statement alterStmt = null;
        ResultSet resultSet = null;
        try{
            connection = storageEngine.getConnection();
            String checkSql =  "SELECT COUNT(*) " +
                    "FROM information_schema.columns " +
                    "WHERE table_schema = ? AND table_name = ? AND column_name = ?";
            checkStmt = connection.prepareStatement(checkSql);
            checkStmt.setString(1, storageEngine.getConfiguration().getDatabase());
            checkStmt.setString(2, tableName);
            checkStmt.setString(3, columnName);
            resultSet = checkStmt.executeQuery();
            if (resultSet.next() && resultSet.getInt(1) == 0) {
                String alterSql = String.format("ALTER TABLE %s " +
                        "ADD COLUMN %s %s default NULL",tableName,columnName,type);
                alterStmt = connection.createStatement();
                alterStmt.executeUpdate(alterSql);
            }
        }catch (Exception ex){
            logger.error("add cmdb column[{}:{}] error!",tableName,columnName);
        }finally {
            try {
                if (resultSet != null) resultSet.close();
                if (checkStmt != null) checkStmt.close();
                if (alterStmt != null) alterStmt.close();
                if (connection != null) storageEngine.closeConnection();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void addIndexIfNotExist(String tableName,String indexName,String columnName) throws Exception{
        CMDBStorageEngine<Connection> storageEngine = CMDBStorageEngineProxy.getInstance();
        String checkIndexSql = "SELECT COUNT(1) FROM INFORMATION_SCHEMA.STATISTICS " +
                "WHERE TABLE_SCHEMA = ? AND TABLE_NAME = ? AND INDEX_NAME = ?";
        String createIndexSql = String.format("ALTER TABLE %s ADD INDEX %s (%s)", tableName, indexName, columnName);
        Connection connection = null;
        try{
            connection = storageEngine.getConnection();
            QueryRunner queryRunner = new QueryRunner();
            Long count = queryRunner.query(connection, checkIndexSql, new ScalarHandler<>(), storageEngine.getConfiguration().getDatabase(), tableName, indexName);
            if (count == null || count == 0) {
                queryRunner.update(connection, createIndexSql);
            }
        }catch (Exception ex){
            logger.error("add cmdb index[{}:{}] error!",tableName,indexName);
        }finally {
            try {
                if (connection != null) storageEngine.closeConnection();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
