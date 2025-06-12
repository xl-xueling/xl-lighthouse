package com.dtstep.lighthouse.core.wrapper;
/*
 * Copyright (C) 2022-2025 XueLing.雪灵
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import com.dtstep.lighthouse.common.enums.OwnerTypeEnum;
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

public class PermissionDBWrapper {

    private static final Logger logger = LoggerFactory.getLogger(PermissionDBWrapper.class);

    private static final Integer _CacheExpireMinutes = 3;

    private static final CMDBStorageEngine<Connection> storageEngine = CMDBStorageEngineProxy.getInstance();

    private static final Cache<Object, Optional<Boolean>> PERMISSION_CACHE = Caffeine.newBuilder()
            .expireAfterWrite(_CacheExpireMinutes, TimeUnit.MINUTES)
            .maximumSize(100000)
            .softValues()
            .build();

    public static boolean hasPermission(Integer ownerId, OwnerTypeEnum ownerTypeEnum,Integer roleId) throws Exception {
        Optional<Boolean> optional =  PERMISSION_CACHE.get("HasPermission-" + ownerId + "-" + ownerTypeEnum + "-" + roleId, k -> actualHashPermission(ownerId, ownerTypeEnum,roleId));
        assert optional != null;
        return optional.orElse(false);
    }

    public static Optional<Boolean> actualHashPermission(Integer ownerId, OwnerTypeEnum ownerTypeEnum,Integer roleId) {
        Boolean bool = null;
        try{
            bool = checkPermissionFromDB(ownerId,ownerTypeEnum,roleId);
        }catch (Exception ex){
            logger.error("check permission info error!", ex);
        }
        return Optional.ofNullable(bool);
    }

    private static Boolean checkPermissionFromDB(Integer ownerId, OwnerTypeEnum ownerTypeEnum,Integer roleId) throws Exception {
        Connection conn = storageEngine.getConnection();
        QueryRunner queryRunner = new QueryRunner();
        Boolean bool;
        try{
            bool = queryRunner.query(conn, String.format("WITH RECURSIVE role_hierarchy AS (\n" +
                    "    SELECT id, pid\n" +
                    "    FROM ldp_roles\n" +
                    "    WHERE id = '%s'\n" +
                    "    UNION ALL\n" +
                    "    SELECT r.id, r.pid\n" +
                    "    FROM ldp_roles r\n" +
                    "    INNER JOIN role_hierarchy rh ON rh.pid = r.id\n" +
                    ")\n" +
                    "\n" +
                    "SELECT \n" +
                    "    CASE WHEN EXISTS (\n" +
                    "        SELECT 1\n" +
                    "        FROM ldp_permissions p\n" +
                    "        INNER JOIN role_hierarchy rh ON rh.id = p.role_id\n" +
                    "        WHERE p.owner_id = '%s'\n" +
                    "        AND p.owner_type = '%s'\n" +
                    "        AND (p.expire_time = null || p.expire_time > NOW())\n" +
                    "    )\n" +
                    "    THEN 'true'\n" +
                    "    ELSE 'false'\n" +
                    "    END AS has_permission",roleId,ownerId,ownerTypeEnum.getOwnerType()), new PermissionSetHandler());
        }finally {
            storageEngine.closeConnection();
        }
        return bool;
    }

    private static class PermissionSetHandler implements ResultSetHandler<Boolean> {

        @Override
        public Boolean handle(ResultSet rs) throws SQLException {
            boolean bool = false;
            if(rs.next()){
                bool = rs.getBoolean(1);
            }
            return bool;
        }
    }
}
