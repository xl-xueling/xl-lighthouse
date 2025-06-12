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
import com.dtstep.lighthouse.common.enums.CallerStateEnum;
import com.dtstep.lighthouse.common.modal.Caller;
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

public class CallerDBWrapper {

    private static final Logger logger = LoggerFactory.getLogger(CallerDBWrapper.class);

    private static final Integer _CacheExpireMinutes = 5;

    private static final CMDBStorageEngine<Connection> storageEngine = CMDBStorageEngineProxy.getInstance();

    private static final Cache<Object, Optional<Caller>> CALLER_CACHE = Caffeine.newBuilder()
            .expireAfterWrite(_CacheExpireMinutes, TimeUnit.MINUTES)
            .maximumSize(100000)
            .softValues()
            .build();

    public static Caller queryByName(String callerName){
        Optional<Caller> optional =  CALLER_CACHE.get(callerName, k -> actualQueryByName(callerName));
        assert optional != null;
        return optional.orElse(null);
    }

    private static Optional<Caller> actualQueryByName(String callerName) {
        Caller caller = null;
        try{
            caller = queryCallerByNameFromDB(callerName);
        }catch (Exception ex){
            logger.error("query caller info error!", ex);
        }
        return Optional.ofNullable(caller);
    }

    private static Caller queryCallerByNameFromDB(String callerName) throws Exception {
        Connection conn = storageEngine.getConnection();
        QueryRunner queryRunner = new QueryRunner();
        Caller caller;
        try{
            caller = queryRunner.query(conn, String.format("select id,name,state,secret_key,create_time,update_time from ldp_callers where name = '%s'",callerName), new CallerSetHandler());
        }finally {
            storageEngine.closeConnection();
        }
        return caller;
    }

    private static class CallerSetHandler implements ResultSetHandler<Caller> {

        @Override
        public Caller handle(ResultSet rs) throws SQLException {
            Caller caller = null;
            if(rs.next()){
                caller = new Caller();
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int state = rs.getInt("state");
                String secretKey = rs.getString("secret_key");
                long createTime = rs.getTimestamp("create_time").getTime();
                long updateTime = rs.getTimestamp("update_time").getTime();
                caller.setId(id);
                caller.setName(name);
                caller.setState(CallerStateEnum.forValue(state));
                caller.setSecretKey(secretKey);
                caller.setCreateTime(DateUtil.timestampToLocalDateTime(createTime));
                caller.setUpdateTime(DateUtil.timestampToLocalDateTime(updateTime));
            }
            return caller;
        }
    }
}
