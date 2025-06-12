package com.dtstep.lighthouse.core.tools;
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
import com.dtstep.lighthouse.core.config.LDPConfig;
import com.dtstep.lighthouse.core.storage.warehouse.hbase.HBaseWarehouseStorageEngine;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.RegionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class HBaseTableRegionMerge {

    private static final Logger logger = LoggerFactory.getLogger(HBaseTableRegionMerge.class);

    public static void main(String[] args) throws Exception {
        LDPConfig.loadConfiguration();
        int targetSize = 6;
        if(args != null){
            targetSize = Integer.parseInt(args[0]);
        }
        HBaseWarehouseStorageEngine storageEngine = new HBaseWarehouseStorageEngine();
        TableName[] tableNames = storageEngine.listTables();
        if(tableNames == null || tableNames.length == 0){
            return;
        }
        for(TableName tableName : tableNames){
            String tableNameStr = tableName.getNameAsString();
            storageEngine.merge(tableNameStr,targetSize);
            Thread.sleep(10000);
            List<RegionInfo> regionInfoList = storageEngine.getRegionInfo(tableNameStr);
            if(regionInfoList.size() != targetSize){
                logger.error("Merge table failed,tableName:{},current region size:{},target region size:{}",tableNameStr,regionInfoList.size(),targetSize);
                break;
            }
        }
        logger.info("Execution completed and the program exits!");
        System.exit(0);
    }
}
