package com.dtstep.lighthouse.core.storage.cmdb;
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

import com.dtstep.lighthouse.common.util.StringUtil;
import com.dtstep.lighthouse.core.config.LDPConfig;
import com.dtstep.lighthouse.core.storage.cmdb.mysql.MySQLCMDBStorageEngine;
import com.dtstep.lighthouse.core.storage.warehouse.WarehouseStorageEngine;
import com.dtstep.lighthouse.core.storage.warehouse.hbase.HBaseWarehouseStorageEngine;
import com.dtstep.lighthouse.core.storage.warehouse.mysql.MySQLWarehouseStorageEngine;
import org.jsoup.helper.Validate;

import java.sql.Connection;

public class CMDBStorageEngineProxy {

    private static CMDBStorageEngine<Connection> storageEngine;

    static {
        loadStorageEngine();
    }

    private static void loadStorageEngine(){
        String engine = LDPConfig.getVal(LDPConfig.KEY_CMDB_STORAGE_ENGINE);
        Validate.isTrue(StringUtil.isNotEmpty(engine));
        if("mysql".equals(engine)){
            storageEngine  = new MySQLCMDBStorageEngine();
        }else{
            throw new RuntimeException("cmdb engine["+engine+"] not support!");
        }
    }

    public static CMDBStorageEngine<Connection> getInstance(){
        return storageEngine;
    }
}
