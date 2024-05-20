package com.dtstep.lighthouse.core.wrapper;
/*
 * Copyright (C) 2022-2024 XueLing.雪灵
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
import com.dtstep.lighthouse.common.enums.MetaTableStateEnum;
import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.core.storage.cmdb.CMDBStorageEngine;
import com.dtstep.lighthouse.core.storage.cmdb.CMDBStorageEngineProxy;
import com.dtstep.lighthouse.core.storage.warehouse.WarehouseStorageEngineProxy;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.dtstep.lighthouse.common.modal.MetaTable;
import com.dtstep.lighthouse.common.enums.MetaTableTypeEnum;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.TimeUnit;


public final class MetaTableWrapper {

    private static final Logger logger = LoggerFactory.getLogger(MetaTableWrapper.class);

    private static final CMDBStorageEngine<Connection> storageEngine = CMDBStorageEngineProxy.getInstance();

    private final static Cache<Integer, Optional<MetaTable>> META_CACHE = Caffeine.newBuilder()
            .expireAfterWrite(2, TimeUnit.MINUTES)
            .maximumSize(10000)
            .softValues()
            .build();

    public static MetaTable queryById(int metaId){
        Optional<MetaTable> optional = META_CACHE.get(metaId, k -> actualQueryById(metaId));
        assert optional != null;
        return optional.orElse(null);
    }

    private static class MetaResultSetHandler implements ResultSetHandler<MetaTable> {

        @Override
        public MetaTable handle(ResultSet rs) throws SQLException {
            MetaTable metaTable = null;
            if(rs.next()){
                metaTable = new MetaTable();
                int id = rs.getInt("id");
                String metaName = rs.getString("meta_name");
                int type = rs.getInt("type");
                int state = rs.getInt("state");
                int recordSize = rs.getInt("record_size");
                int contentSize = rs.getInt("content_size");
                String desc = rs.getString("desc");
                long createTime = rs.getTimestamp("create_time").getTime();
                long updateTime = rs.getTimestamp("update_time").getTime();
                metaTable.setId(id);
                metaTable.setMetaName(metaName);
                metaTable.setMetaTableType(MetaTableTypeEnum.getMetaTableTypeEnum(type));
                metaTable.setState(MetaTableStateEnum.forValue(state));
                metaTable.setRecordSize(recordSize);
                metaTable.setContentSize(contentSize);
                metaTable.setDesc(desc);
                metaTable.setCreateTime(DateUtil.timestampToLocalDateTime(createTime));
                metaTable.setUpdateTime(DateUtil.timestampToLocalDateTime(updateTime));
            }
            return metaTable;
        }
    }

    private static Optional<MetaTable> actualQueryById(int metaId) {
        MetaTable metaTable = null;
        try{
            metaTable = queryMetaByIdFromDB(metaId);
            if(metaTable != null){
                String metaName = metaTable.getMetaName();
                if(!WarehouseStorageEngineProxy.getInstance().isTableExist(metaName)){
                    WarehouseStorageEngineProxy.getInstance().createResultTable(metaName);
                }
            }
        }catch (Exception ex){
            logger.error("query metatable info error!", ex);
        }
        return Optional.ofNullable(metaTable);
    }

    private static MetaTable queryMetaByIdFromDB(int metaId) throws Exception {
        Connection conn = storageEngine.getConnection();
        QueryRunner queryRunner = new QueryRunner();
        MetaTable metaTable;
        try{
            metaTable = queryRunner.query(conn, String.format("select * from ldp_metas where id = '%s'",metaId), new MetaResultSetHandler());
        }finally {
            storageEngine.closeConnection();
        }
        return metaTable;
    }

    private static int insertIntoMySQL(MetaTable metaTable) throws Exception {
        Connection conn = storageEngine.getConnection();
        QueryRunner queryRunner = new QueryRunner();
        String sql = "INSERT INTO ldp_metas (`meta_name`, `type`, `state`,`record_size`,`content_size`,`desc`,`create_time`,`update_time`) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        LocalDateTime localDateTime = LocalDateTime.now();
        BigInteger id;
        try{
             id = queryRunner.insert(conn,sql,new ScalarHandler<>(),metaTable.getMetaName(),metaTable.getMetaTableType().getType(),metaTable.getState().getState(),0,0,null,localDateTime,localDateTime);
        }finally {
            storageEngine.closeConnection();
        }
        return id.intValue();
    }

    private static void deleteTable(String tableName){
        try{
            Connection conn = storageEngine.getConnection();
            QueryRunner queryRunner = new QueryRunner();
            String sql = "DELETE from ldp_metas where meta_name = ?";
            try{
                queryRunner.update(conn,sql,tableName);
            }finally {
                storageEngine.closeConnection();
            }
            logger.info("drop meta table success,metaName:{}",tableName);
        }catch (Exception ex){
            logger.error("drop meta table failed,metaName:{}",tableName,ex);
        }

        try{
            WarehouseStorageEngineProxy.getInstance().dropTable(tableName);
            logger.info("drop storage table success,metaName:{}",tableName);
        }catch (Exception ex){
            logger.error("drop storage table failed,metaName:{}",tableName,ex);
        }

    }

    public static int createStatStorageAndMetaTable() throws Exception {
        MetaTable metaTable = new MetaTable();
        String metaName = "ldp_stat_" + System.currentTimeMillis();
        metaTable.setMetaName(metaName);
        metaTable.setState(MetaTableStateEnum.VALID);
        LocalDateTime date = LocalDateTime.now();
        metaTable.setCreateTime(date);
        metaTable.setUpdateTime(date);
        metaTable.setMetaTableType(MetaTableTypeEnum.STAT_RESULT_TABLE);
        try{
            WarehouseStorageEngineProxy.getInstance().createResultTable(metaName);
            logger.info("create stat storage table,create hbase table success,metaName:{}",metaName);
        }catch (Exception ex){
            logger.error("create stat storage table,create hbase table error,metaName:{}",metaName,ex);
            throw ex;
        }
        int tableId;
        try{
            tableId = insertIntoMySQL(metaTable);
            logger.info("create stat meta table,save table info success,metaName;{}",metaName);
        }catch (Exception ex){
            logger.error("create stat meta table,save table info error,metaName:{}",metaName,ex);
            deleteTable(metaName);
            throw ex;
        }
        return tableId;
    }
}
