package com.dtstep.lighthouse.test.relation;

import com.dtstep.lighthouse.common.modal.User;
import com.dtstep.lighthouse.core.storage.cmdb.CMDBStorageEngineProxy;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class UserHandler {

    private static final Logger logger = LoggerFactory.getLogger(UserHandler.class);

    public static User queryUserInfo(String username) throws Exception {
        Connection conn;
        try {
            conn = CMDBStorageEngineProxy.getInstance().getConnection();
            QueryRunner qr = new QueryRunner();
            List<Map<String, Object>> list = qr.query(conn,"select id,department_id from ldp_users where username = ?",new MapListHandler(),username);
            if(CollectionUtils.isEmpty(list) || list.size() != 1){
                logger.error("query user[admin] info error!");
                throw new RuntimeException("query user[admin] info error!");
            }
            Map<String,Object> infoMap = list.get(0);
            Integer id = (Integer) infoMap.get("id");
            Integer departmentId = (Integer) infoMap.get("department_id");
            User user = new User();
            user.setId(id);
            user.setDepartmentId(departmentId);
            logger.info("query admin info,id:{},department_id:{}",id,departmentId);
            return user;
        } catch (SQLException e) {
            logger.error("query user[admin] info error!",e);
            e.printStackTrace();
            throw e;
        } finally {
            CMDBStorageEngineProxy.getInstance().closeConnection();
        }
    }
}
