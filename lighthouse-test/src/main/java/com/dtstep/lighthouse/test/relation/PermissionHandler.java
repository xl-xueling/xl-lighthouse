package com.dtstep.lighthouse.test.relation;

import com.dtstep.lighthouse.common.modal.Permission;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.math.BigInteger;
import java.sql.Connection;

public class PermissionHandler {

    public static Integer createPermission(Connection conn,Permission permission) throws Exception {
        String insertSql = "INSERT INTO ldp_permissions (owner_id,owner_type,role_id,create_time,update_time) VALUES (?, ?, ?, ?, ?)";
        QueryRunner queryRunner = new QueryRunner();
        BigInteger permissionId = queryRunner.insert(conn, insertSql, new ScalarHandler<>(),
                permission.getOwnerId(),
                permission.getOwnerType().getOwnerType(),
                permission.getRoleId(),
                permission.getCreateTime(),
                permission.getUpdateTime());
        return permissionId.intValue();
    }
}
