package com.dtstep.lighthouse.core.wrapper;

import com.dtstep.lighthouse.common.enums.CallerStateEnum;
import com.dtstep.lighthouse.common.enums.UserStateEnum;
import com.dtstep.lighthouse.common.modal.Caller;
import com.dtstep.lighthouse.common.modal.User;
import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.core.storage.cmdb.CMDBStorageEngine;
import com.dtstep.lighthouse.core.storage.cmdb.CMDBStorageEngineProxy;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class UserDBWrapper {

    private static final CMDBStorageEngine<Connection> storageEngine = CMDBStorageEngineProxy.getInstance();

    private static final Integer _CacheExpireMinutes = 10;

    private static final Logger logger = LoggerFactory.getLogger(UserDBWrapper.class);

    private static final Cache<Integer, Optional<List<Integer>>> DEPARTMENT_USER_CACHE = Caffeine.newBuilder()
            .expireAfterWrite(_CacheExpireMinutes, TimeUnit.MINUTES)
            .maximumSize(100000)
            .softValues()
            .build();

    private static final Cache<Integer, Optional<User>> USER_CACHE = Caffeine.newBuilder()
            .expireAfterWrite(_CacheExpireMinutes, TimeUnit.MINUTES)
            .maximumSize(100000)
            .softValues()
            .build();

    public static List<Integer> queryUseIdListByDepartment(Integer departmentId){
        Optional<List<Integer>> optional =  DEPARTMENT_USER_CACHE.get(departmentId, k -> actualQueryUserIdListByDepartment(departmentId));
        assert optional != null;
        return optional.orElse(null);
    }

    public static User queryById(Integer userId){
        Optional<User> optional =  USER_CACHE.get(userId, k -> actualQueryUserById(userId));
        assert optional != null;
        return optional.orElse(null);
    }

    private static Optional<List<Integer>> actualQueryUserIdListByDepartment(Integer departmentId) {
        List<Integer> userIdList = null;
        try{
            userIdList = queryUserIdListByDepartmentFromDB(departmentId);
        }catch (Exception ex){
            logger.error("query caller info error!", ex);
        }
        return Optional.ofNullable(userIdList);
    }

    private static Optional<User> actualQueryUserById(Integer userId){
        User user = null;
        try{
            user = queryUserFromDB(userId);
        }catch (Exception ex){
            logger.error("query user info error!", ex);
        }
        return Optional.ofNullable(user);
    }

    public static User queryUserFromDB(Integer userId) throws Exception {
        Connection conn = storageEngine.getConnection();
        QueryRunner queryRunner = new QueryRunner();
        User user;
        try{
            user = queryRunner.query(conn, String.format("select id,username,phone,email,state from ldp_users where id = '%s'",userId), new UserSetHandler());
        }finally {
            storageEngine.closeConnection();
        }
        return user;
    }

    private static List<Integer> queryUserIdListByDepartmentFromDB(Integer departmentId) throws Exception {
        Connection conn = storageEngine.getConnection();
        QueryRunner queryRunner = new QueryRunner();
        List<Integer> userIds;
        try{
            String sql = "WITH RECURSIVE sub_departments AS (" +
                    "  SELECT id FROM ldp_departments WHERE id = ? " +
                    "  UNION ALL " +
                    "  SELECT d.id FROM ldp_departments d " +
                    "  INNER JOIN sub_departments sd ON d.pid = sd.id " +
                    ") " +
                    "SELECT u.id AS user_id FROM ldp_users u " +
                    "WHERE u.`state` = 2 and u.department_id IN (SELECT id FROM sub_departments)";
            userIds = queryRunner.query(conn, sql, new ColumnListHandler<>(), departmentId);
        }finally {
            storageEngine.closeConnection();
        }
        return userIds;
    }

    private static class UserSetHandler implements ResultSetHandler<User> {

        @Override
        public User handle(ResultSet rs) throws SQLException {
            User user = null;
            if(rs.next()){
                user = new User();
                int id = rs.getInt("id");
                String username = rs.getString("username");
                String phone = rs.getString("phone");
                String email = rs.getString("email");
                int state = rs.getInt("state");
                user.setId(id);
                user.setUsername(username);
                user.setPhone(phone);
                user.setEmail(email);
                user.setState(UserStateEnum.forValue(state));
            }
            return user;
        }
    }

}
