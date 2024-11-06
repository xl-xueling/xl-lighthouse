package com.dtstep.lighthouse.core.wrapper;

import com.dtstep.lighthouse.common.modal.Notification;
import com.dtstep.lighthouse.core.storage.cmdb.CMDBStorageEngine;
import com.dtstep.lighthouse.core.storage.cmdb.CMDBStorageEngineProxy;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.math.BigInteger;
import java.sql.Connection;

public class NotificationDBWrapper {

    private static final CMDBStorageEngine<Connection> storageEngine = CMDBStorageEngineProxy.getInstance();

    public static long insert(Notification notification) throws Exception {
        Connection conn = storageEngine.getConnection();
        QueryRunner queryRunner = new QueryRunner();
        int id;
        try{
            ScalarHandler<BigInteger> keyHandler = new ScalarHandler<>();
            BigInteger result = queryRunner.insert(conn, "INSERT INTO ldp_notifications (`resource_id`, `resource_type`,`content`,`state`,`user_ids`,`department_ids`,`notification_type`,`create_time`,`update_time`) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)", keyHandler,notification.getResourceId(),notification.getResourceType().getResourceType(),
                    notification.getContent(),notification.getState().getState(),
                    "[1,2]",
                    "[1,2]",
                    notification.getNotificationType().getType(),
                    notification.getCreateTime(),notification.getUpdateTime()
            );
            id = result.intValue();
        }finally {
            storageEngine.closeConnection();
        }
        return id;
    }
}
