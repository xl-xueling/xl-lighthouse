package com.dtstep.lighthouse.core.wrapper;

import com.dtstep.lighthouse.common.enums.ResourceTypeEnum;
import com.dtstep.lighthouse.common.enums.RoleTypeEnum;
import com.dtstep.lighthouse.common.modal.Role;
import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.core.storage.cmdb.CMDBStorageEngine;
import com.dtstep.lighthouse.core.storage.cmdb.CMDBStorageEngineProxy;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class RoleDBWrapper {

    private static final Logger logger = LoggerFactory.getLogger(RoleDBWrapper.class);

    private static final Integer _CacheExpireMinutes = 5;

    private static final CMDBStorageEngine<Connection> storageEngine = CMDBStorageEngineProxy.getInstance();

    private static final Cache<Object, Optional<Role>> ROLE_CACHE = Caffeine.newBuilder()
            .expireAfterWrite(_CacheExpireMinutes, TimeUnit.MINUTES)
            .maximumSize(100000)
            .softValues()
            .build();

    public static Role queryAccessRoleByResource(Integer resourceId, ResourceTypeEnum resourceTypeEnum) throws Exception {
        RoleTypeEnum roleTypeEnum = null;
        if(resourceTypeEnum == ResourceTypeEnum.Project){
            roleTypeEnum = RoleTypeEnum.PROJECT_ACCESS_PERMISSION;
        }else if(resourceTypeEnum == ResourceTypeEnum.Group){
            roleTypeEnum = RoleTypeEnum.GROUP_ACCESS_PERMISSION;
        }else if(resourceTypeEnum == ResourceTypeEnum.Stat){
            roleTypeEnum = RoleTypeEnum.STAT_ACCESS_PERMISSION;
        }else if(resourceTypeEnum == ResourceTypeEnum.View){
            roleTypeEnum = RoleTypeEnum.VIEW_ACCESS_PERMISSION;
        }
        RoleTypeEnum finalRoleTypeEnum = roleTypeEnum;
        Optional<Role> optional =  ROLE_CACHE.get("AccessRole-" + resourceId + "-" + resourceTypeEnum, k -> actualQueryRoleByResource(resourceId, finalRoleTypeEnum));
        assert optional != null;
        return optional.orElse(null);
    }

    public static Role queryManageRoleByResource(Integer resourceId, ResourceTypeEnum resourceTypeEnum){
        RoleTypeEnum roleTypeEnum = null;
        if(resourceTypeEnum == ResourceTypeEnum.Project){
            roleTypeEnum = RoleTypeEnum.PROJECT_MANAGE_PERMISSION;
        }else if(resourceTypeEnum == ResourceTypeEnum.Group){
            roleTypeEnum = RoleTypeEnum.GROUP_MANAGE_PERMISSION;
        }else if(resourceTypeEnum == ResourceTypeEnum.Stat){
            roleTypeEnum = RoleTypeEnum.STAT_MANAGE_PERMISSION;
        }else if(resourceTypeEnum == ResourceTypeEnum.View){
            roleTypeEnum = RoleTypeEnum.VIEW_MANAGE_PERMISSION;
        }
        RoleTypeEnum finalRoleTypeEnum = roleTypeEnum;
        Optional<Role> optional =  ROLE_CACHE.get("ManageRole-" + resourceId + "-" + resourceTypeEnum, k -> actualQueryRoleByResource(resourceId, finalRoleTypeEnum));
        assert optional != null;
        return optional.orElse(null);
    }

    private static Optional<Role> actualQueryRoleByResource(Integer resourceId, RoleTypeEnum roleTypeEnum) {
        Role role = null;
        try{
            role = queryRoleByNameFromDB(resourceId,roleTypeEnum);
        }catch (Exception ex){
            logger.error("query role info error!", ex);
        }
        return Optional.ofNullable(role);
    }

    private static Role queryRoleByNameFromDB(Integer resourceId,RoleTypeEnum roleTypeEnum) throws Exception {
        Connection conn = storageEngine.getConnection();
        QueryRunner queryRunner = new QueryRunner();
        Role role;
        try{
            role = queryRunner.query(conn, String.format("select * from ldp_roles where resource_id = '%s' and role_type = '%s'",resourceId,roleTypeEnum.getRoleType()), new RoleSetHandler());
        }finally {
            storageEngine.closeConnection();
        }
        return role;
    }

    private static class RoleSetHandler implements ResultSetHandler<Role> {

        @Override
        public Role handle(ResultSet rs) throws SQLException {
            Role role = null;
            if(rs.next()){
                role = new Role();
                int id = rs.getInt("id");
                int roleType = rs.getInt("role_type");
                int resourceId = rs.getInt("resource_id");
                int pid = rs.getInt("pid");
                long createTime = rs.getTimestamp("create_time").getTime();
                long updateTime = rs.getTimestamp("update_time").getTime();
                role.setRoleType(RoleTypeEnum.forValue(roleType));
                role.setResourceId(resourceId);
                role.setPid(pid);
                role.setId(id);
                role.setCreateTime(DateUtil.timestampToLocalDateTime(createTime));
                role.setUpdateTime(DateUtil.timestampToLocalDateTime(updateTime));
            }
            return role;
        }
    }
}
