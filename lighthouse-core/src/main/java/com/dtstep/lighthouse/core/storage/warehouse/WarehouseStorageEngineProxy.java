package com.dtstep.lighthouse.core.storage.warehouse;
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

import com.dtstep.lighthouse.common.util.StringUtil;
import com.dtstep.lighthouse.core.config.LDPConfig;
import com.dtstep.lighthouse.core.storage.warehouse.hbase.HBaseWarehouseStorageEngine;
import com.dtstep.lighthouse.core.storage.warehouse.mysql.MySQLWarehouseStorageEngine;
import org.jsoup.helper.Validate;

public class WarehouseStorageEngineProxy {

    private static WarehouseStorageEngine storageEngine;

    static {
        loadStorageEngine();
    }

    private static void loadStorageEngine(){
        String engine = LDPConfig.getVal(LDPConfig.KEY_WAREHOUSE_STORAGE_ENGINE);
        Validate.isTrue(StringUtil.isNotEmpty(engine));
        if("hbase".equals(engine)){
            if(storageEngine == null){
                storageEngine  = new HBaseWarehouseStorageEngine();
            }
        }else if("mysql".equals(engine)){
            if(storageEngine == null){
                storageEngine  = new MySQLWarehouseStorageEngine();
            }
        }else{
            throw new RuntimeException("warehouse engine["+engine+"] not support!");
        }
    }

    public static WarehouseStorageEngine getInstance(){
        return storageEngine;
    }

}
