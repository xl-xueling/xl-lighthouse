package com.dtstep.lighthouse.core.hbase;
/*
 * Copyright (C) 2022-2023 XueLing.雪灵
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
import com.dtstep.lighthouse.core.config.LDPConfig;
import com.dtstep.lighthouse.common.constant.SysConst;
import com.dtstep.lighthouse.common.util.StringUtil;
import com.dtstep.lighthouse.core.lock.RedLock;
import org.apache.commons.lang3.Validate;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.io.compress.Compression;
import org.apache.hadoop.hbase.io.encoding.DataBlockEncoding;
import org.apache.hadoop.hbase.regionserver.BloomType;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


public class HBaseTableOperator {

    private static final Logger logger = LoggerFactory.getLogger(HBaseTableOperator.class);

    private static Admin hBaseAdmin = null;

    private static Compression.Algorithm algorithm = null;

    static {
        try{
            hBaseAdmin = HBaseClient.getConnection().getAdmin();
        }catch (Exception ex){
            logger.error("hbase get admin error!",ex);
        }
        String compression = LDPConfig.getOrDefault(LDPConfig.KEY_DATA_COMPRESSION_TYPE,"zstd",String.class);
        if(StringUtil.isNotEmpty(compression)){
            switch (compression) {
                case "snappy":
                    algorithm = Compression.Algorithm.SNAPPY;
                    break;
                case "bzip2":
                    algorithm = Compression.Algorithm.BZIP2;
                    break;
                case "zstd":
                    algorithm = Compression.Algorithm.ZSTD;
                    break;
                case "gz":
                    algorithm = Compression.Algorithm.GZ;
                    break;
                case "lzo":
                    algorithm = Compression.Algorithm.LZO;
                    break;
                case "lzma":
                    algorithm = Compression.Algorithm.LZMA;
                    break;
                default:
                    algorithm = Compression.Algorithm.NONE;
                    break;
            }
        }
    }

    public static String getDefaultNamespace() throws Exception {
        String clusterName = LDPConfig.getVal(LDPConfig.KEY_CLUSTER_ID);
        Validate.notNull(clusterName);
        return String.format("cluster_%s_ldp_hbasedb",clusterName);
    }

    public static boolean isNameSpaceExist(String namespace) throws Exception {
        String [] namespaceArr = hBaseAdmin.listNamespaces();
        for(String dbNamespace : namespaceArr){
            if(dbNamespace.equals(namespace)){
                return true;
            }
        }
        return false;
    }

    public static void createNamespaceIfNotExist(String namespace) throws Exception {
        Validate.notNull(namespace);
        String lockKey = "lock_create_namespace_" + namespace;
        boolean isLock = RedLock.tryLock(lockKey,2,3, TimeUnit.MINUTES);
        if(isLock){
            try{
                if(isNameSpaceExist(namespace)){
                    return;
                }
                NamespaceDescriptor namespaceDescriptor = NamespaceDescriptor.create(namespace).build();
                hBaseAdmin.createNamespace(namespaceDescriptor);
                logger.info("create namespace {} success!",namespace);
            }catch (Exception ex){
                logger.error("create namespace error,namespace:{}",namespace,ex);
                throw ex;
            }finally {
                RedLock.unLock(lockKey);
            }
        }
    }

    public static void createTableIfNotExist(String tableName,int prePartitionsSize) throws Exception {
        Validate.notNull(tableName);
        String lockKey = "lock_create_table_" + tableName;
        boolean isLock = RedLock.tryLock(lockKey,2,3, TimeUnit.MINUTES);
        if(isLock){
            try{
                if(isTableExist(tableName)){
                    return;
                }
                HBaseTableOperator.createTable(tableName, prePartitionsSize);
                logger.info("create table {} success!",tableName);
            }catch (Exception ex){
                logger.error("create table error,table:{}!",tableName,ex);
            }finally {
                RedLock.unLock(lockKey);
            }
        }
    }


    public static void createTable(String tableName,int prePartitionsSize) throws Exception {
        if(prePartitionsSize > SysConst._DBKeyPrefixArray.length){
            throw new Exception("create HBase table error!");
        }
        String [] keys = SysConst._DBKeyPrefixArray;
        byte[][] splitKeys = new byte[prePartitionsSize][];
        TreeSet<byte[]> rows = new TreeSet<>(Bytes.BYTES_COMPARATOR);
        for (int i = 0; i < prePartitionsSize; i++) {
            rows.add(Bytes.toBytes(keys[i]));
        }
        Iterator<byte[]> rowKeyIterator = rows.iterator();
        int i=0;
        while (rowKeyIterator.hasNext()) {
            byte[] tempRow = rowKeyIterator.next();
            rowKeyIterator.remove();
            splitKeys[i] = tempRow;
            i++;
        }
        String namespace = HBaseTableOperator.getDefaultNamespace();
        TableDescriptorBuilder tableDescriptor = TableDescriptorBuilder.newBuilder(TableName.valueOf(namespace + ":" + tableName));
        ColumnFamilyDescriptor family = ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes("f")).setMaxVersions(1)
                .setInMemory(false)
                .setBloomFilterType(BloomType.ROW)
                .setCompressTags(true)
                .setTimeToLive((int)TimeUnit.DAYS.toSeconds(30))
                .setCompactionCompressionType(algorithm)
                .setCompressionType(algorithm)
                .setBlocksize(16384)
                .setDataBlockEncoding(DataBlockEncoding.FAST_DIFF)
                .setInMemoryCompaction(MemoryCompactionPolicy.BASIC).build();
        tableDescriptor.setColumnFamily(family);
        try{
            hBaseAdmin.createTable(tableDescriptor.build(),splitKeys);
        }catch (Exception ex){
            logger.error("create hbase table,system error.metaName:{}",tableName,ex);
            throw ex;
        }
        boolean isResult;
        try{
            isResult = isTableExist(tableName);
        }catch (Exception ex){
            logger.error(String.format("create hbase table,check status error.metaName:%s",tableName),ex);
            throw ex;
        }
        if(!isResult) {
            logger.info("create hbase table error,table not created.metaName:{}",tableName);
            throw new Exception("create hbase table error!");
        }
    }



    public static boolean isTableExist(String tableName) throws Exception {
        String namespace = HBaseTableOperator.getDefaultNamespace();
        TableName tableNameObj = TableName.valueOf(namespace + ":" + tableName);
        return hBaseAdmin.tableExists(tableNameObj);
    }

    public static void flush(String tableName) throws Exception {
        String namespace = HBaseTableOperator.getDefaultNamespace();
        TableName tableNameObj = TableName.valueOf(namespace + ":" + tableName);
        hBaseAdmin.flush(tableNameObj);
    }

    public static void deleteTable(String tableName) throws Exception{
        boolean isExist = isTableExist(tableName);
        if(!isExist){
            return;
        }
        String namespace = HBaseTableOperator.getDefaultNamespace();
        TableName tableNameObj = TableName.valueOf(namespace + ":" + tableName);
        hBaseAdmin.disableTable(tableNameObj);
        hBaseAdmin.deleteTable(tableNameObj);
    }

    public static List<String> loadTableList() throws Exception {
        TableName[] tableNames = hBaseAdmin.listTableNames();
        return Arrays.stream(tableNames).map(TableName::getNameAsString).collect(Collectors.toList());
    }

    public static void clearTable(String tableName) throws Exception{
        String namespace = HBaseTableOperator.getDefaultNamespace();
        TableName tableNameObj = TableName.valueOf(namespace + ":" + tableName);
        hBaseAdmin.flush(tableNameObj);
        if(hBaseAdmin.isTableEnabled(tableNameObj)){
            hBaseAdmin.disableTable(tableNameObj);
        }
        hBaseAdmin.truncateTable(tableNameObj,false);
    }

}
