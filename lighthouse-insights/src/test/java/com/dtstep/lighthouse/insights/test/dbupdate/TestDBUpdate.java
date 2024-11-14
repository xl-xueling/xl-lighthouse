package com.dtstep.lighthouse.insights.test.dbupdate;

import com.dtstep.lighthouse.core.storage.cmdb.CMDBStorageEngine;
import com.dtstep.lighthouse.core.storage.cmdb.CMDBStorageEngineProxy;
import com.dtstep.lighthouse.insights.LightHouseInsightsApplication;
import com.dtstep.lighthouse.insights.test.listener.SpringTestExecutionListener;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LightHouseInsightsApplication.class,properties = {"spring.config.location=classpath:lighthouse-insights.yml"})
@TestExecutionListeners(listeners = SpringTestExecutionListener.class, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public class TestDBUpdate {

    @Test
    public void testUpdate() throws Exception {
        CMDBStorageEngine<Connection> storageEngine = CMDBStorageEngineProxy.getInstance();
        Statement statement = null;
        try (Connection conn = storageEngine.getConnection()) {
            try {
                // 连接数据库
                statement = conn.createStatement();

                // 构建动态 SQL
                String tableName = "ldp_relations";
                String columnName = "extend";
                String columnDefinition = "VARCHAR(255)";
                String databaseName = "cluster_aa3489d4_ldp_cmdb";
                String sql = "ALTER TABLE ldp_relations " +
                        "ADD COLUMN extend VARCHAR(5000) default null ";
                statement.execute(sql);
                System.out.println("Column check and addition process completed.");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (statement != null) statement.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
