package com.dtstep.lighthouse.test.relation;

import com.dtstep.lighthouse.common.enums.RoleTypeEnum;
import com.dtstep.lighthouse.common.modal.Role;
import com.dtstep.lighthouse.common.modal.User;
import com.dtstep.lighthouse.core.storage.cmdb.CMDBStorageEngineProxy;
import com.dtstep.lighthouse.test.example.RunExample;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class RoleHandler {

    private static final Logger logger = LoggerFactory.getLogger(RoleHandler.class);

    public static Integer createRole(Connection connection,Role role) throws Exception {
        String insertSql = "INSERT INTO ldp_roles (role_type,resource_id,pid,`create_time`, `update_time`,`desc`) VALUES (?, ?, ?, ?, ?, ?)";
        QueryRunner queryRunner = new QueryRunner();
        BigInteger roleId = queryRunner.insert(connection, insertSql, new ScalarHandler<>(), role.getRoleType().getRoleType(),
                role.getResourceId(),
                role.getPid(),
                role.getCreateTime(),
                role.getUpdateTime(),
                role.getDesc());
        return roleId.intValue();
    }

    public static Integer queryRoleId(Connection conn,Integer resourceId, RoleTypeEnum roleTypeEnum) throws Exception {
        try {
            QueryRunner qr = new QueryRunner();
            List<Map<String, Object>> list = qr.query(conn,"select id from ldp_roles where resource_id = ? and role_type = ?",new MapListHandler(),resourceId,roleTypeEnum.getRoleType());
            if(CollectionUtils.isEmpty(list) || list.size() != 1) {
                logger.error("query roles info error!");
                throw new RuntimeException("query roles info error!");
            }
            Map<String,Object> infoMap = list.get(0);
            return (Integer) infoMap.get("id");
        } catch (SQLException e) {
            logger.error("query user[admin] info error!",e);
            e.printStackTrace();
            throw e;
        }
    }

}
