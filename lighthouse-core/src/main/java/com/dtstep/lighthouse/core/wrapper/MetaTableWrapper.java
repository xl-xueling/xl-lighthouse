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
import com.dtstep.lighthouse.core.hbase.HBaseTableOperator;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.dtstep.lighthouse.common.constant.SysConst;
import com.dtstep.lighthouse.common.modal.MetaTable;
import com.dtstep.lighthouse.common.enums.MetaTableTypeEnum;
import com.dtstep.lighthouse.core.dao.DaoHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;


public final class MetaTableWrapper {

    private static final Logger logger = LoggerFactory.getLogger(MetaTableWrapper.class);

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

    private static Optional<MetaTable> actualQueryById(int metaId) {
        MetaTable metaTable = null;
        try{
            metaTable = DaoHelper.sql.getItem(MetaTable.class, "select * from ldp_meta_table where id = ?", metaId);
            if(metaTable != null){
                String metaName = metaTable.getMetaName();
                if(!HBaseTableOperator.isTableExist(metaName)){
                    HBaseTableOperator.createTableIfNotExist(metaName,SysConst._DATA_STORAGE_PRE_PARTITIONS_SIZE);
                }
            }
        }catch (Exception ex){
            logger.error("query metatable info error!", ex);
        }
        return Optional.ofNullable(metaTable);
    }




    public static int createStatResultMetaTable() throws Exception {
        MetaTable metaTable = new MetaTable();
        metaTable.setCreateTime(new Date());
        String metaName = "ldp_stat_" + System.currentTimeMillis();
        metaTable.setMetaName(metaName);
        metaTable.setState(1);
        Date date = new Date();
        metaTable.setCreateTime(date);
        metaTable.setUpdateTime(date);
        metaTable.setType(MetaTableTypeEnum.STAT_RESULT_TABLE.getType());
        try{
            HBaseTableOperator.createTable(metaName, SysConst._DATA_STORAGE_PRE_PARTITIONS_SIZE);
            logger.info("create stat result table,create hbase table success,metaName:{}",metaName);
        }catch (Exception ex){
            logger.error("create stat result table,create hbase table error,metaName:{}",metaName,ex);
            throw ex;
        }
        int tableId;
        try{
            tableId = DaoHelper.sql.insert(metaTable);
            logger.info("create stat result table,save table info success,metaName;{}",metaName);
        }catch (Exception ex){
            HBaseTableOperator.deleteTable(metaName);
            logger.error("create stat result table,save table info error,metaName:{}",metaName,ex);
            throw ex;
        }
        return tableId;
    }

    public static int createSeqResultMetaTable() throws Exception {
        MetaTable metaTable = new MetaTable();
        metaTable.setCreateTime(new Date());
        String metaName = "ldp_seq_" + System.currentTimeMillis();
        metaTable.setMetaName(metaName);
        metaTable.setState(1);
        Date date = new Date();
        metaTable.setCreateTime(date);
        metaTable.setUpdateTime(date);
        metaTable.setType(MetaTableTypeEnum.SEQ_RESULT_TABLE.getType());
        try{
            HBaseTableOperator.createTable(metaName, SysConst._DATA_STORAGE_PRE_PARTITIONS_SIZE);
            logger.info("create seq result table,create hbase table success,metaName:{}",metaName);
        }catch (Exception ex){
            logger.error("create seq result table,create hbase table error,metaName:{}",metaName,ex);
            throw ex;
        }
        int tableId;
        try{
            tableId = DaoHelper.sql.insert(metaTable);
            logger.info("create seq result table,save table info success,metaName;{}",metaName);
        }catch (Exception ex){
            HBaseTableOperator.deleteTable(metaName);
            logger.error("create seq result table,save table info error,metaName:{}",metaName,ex);
            throw ex;
        }
        return tableId;
    }


    public static void removeMetaTableCache(int metaId){
        META_CACHE.invalidate(metaId);
    }
}
