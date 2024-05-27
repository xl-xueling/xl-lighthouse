package com.dtstep.lighthouse.test.relation;

import com.dtstep.lighthouse.common.constant.SysConst;
import com.dtstep.lighthouse.common.modal.Group;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.core.storage.cmdb.CMDBStorageEngineProxy;
import com.dtstep.lighthouse.core.storage.warehouse.mysql.MySQLWarehouseStorageEngine;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

public class GroupHandler {

    private static final Logger logger = LoggerFactory.getLogger(GroupHandler.class);

    public static Integer createGroup(Connection conn, Group group) throws Exception {
        String insertSql = "INSERT INTO ldp_groups (token,project_id,columns,random_id,secret_key,`state`,refresh_time,create_time,update_time,`desc`) VALUES (?, ?, ?, ?, ?,?, ?, ?, ?,?)";
        QueryRunner queryRunner = new QueryRunner();
        BigInteger permissionId = queryRunner.insert(conn, insertSql, new ScalarHandler<>(),
                group.getToken(),
                group.getProjectId(),
                JsonUtil.toJSONString(group.getColumns()),
                group.getRandomId(),
                group.getSecretKey(),
                group.getState().getState(),
                group.getRefreshTime(),
                group.getCreateTime(),
                group.getUpdateTime(),
                group.getDesc());
        return permissionId.intValue();
    }


    public static Group queryGroupInfo(String token) throws Exception {
        Connection conn;
        try {
            conn = CMDBStorageEngineProxy.getInstance().getConnection();
            QueryRunner qr = new QueryRunner();
            List<Map<String, Object>> list = qr.query(conn,
                    "select id,project_id from ldp_groups where token = ?",new MapListHandler(), token);
            if(CollectionUtils.isEmpty(list)){
                return null;
            }
            Map<String,Object> infoMap = list.get(0);
            Integer id = (Integer) infoMap.get("id");
            Integer projectId = (Integer) infoMap.get("project_id");
            Group group = new Group();
            group.setId(id);
            group.setProjectId(projectId);
            return group;
        }catch (Exception ex){
            logger.error("query valid meta info error!",ex);
            ex.printStackTrace();
            throw ex;
        }finally {
            CMDBStorageEngineProxy.getInstance().closeConnection();
        }
    }

    public static void delete(Connection connection,Integer groupId) throws Exception {
        String sql = "DELETE FROM ldp_groups WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, groupId);
            ps.executeUpdate();
        } catch (Exception ex) {
            logger.error("delete mysql data error!", ex);
            ex.printStackTrace();
        }
    }

}
