package com.dtstep.lighthouse.core.wrapper;

import com.dtstep.lighthouse.common.modal.User;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.core.storage.cmdb.CMDBStorageEngine;
import com.dtstep.lighthouse.core.storage.cmdb.CMDBStorageEngineProxy;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.math.BigInteger;
import java.sql.Connection;
import java.util.List;

public class UserDBWrapper {

    private static final CMDBStorageEngine<Connection> storageEngine = CMDBStorageEngineProxy.getInstance();

    public static List<User> queryUserList(List<Integer> userIdList,List<Integer> departmentIdList) throws Exception {
        Connection conn = storageEngine.getConnection();
        QueryRunner queryRunner = new QueryRunner();
        int id;
        try{
            String sql = " WITH RECURSIVE DepartmentHierarchy AS (\n" +
                    "                    SELECT id FROM ldp_departments WHERE id IN ('10252')\n" +
                    "                    UNION ALL\n" +
                    "                    SELECT d.id FROM ldp_departments d\n" +
                    "                    INNER JOIN DepartmentHierarchy dh ON d.pid = dh.id\n" +
                    "                )\n" +
                    "                SELECT DISTINCT u.id AS user_id\n" +
                    "                FROM ldp_users u\n" +
                    "                WHERE u.id IN ('1')\n" +
                    "                   OR u.department_id IN (SELECT id FROM DepartmentHierarchy);";

            List objects = queryRunner.query(conn,sql,new ColumnListHandler<>());
            for(Object object : objects){
                System.out.println("object is:" + JsonUtil.toJSONString(object));
            }
        }finally {
            storageEngine.closeConnection();
        }
        return null;
    }

}
