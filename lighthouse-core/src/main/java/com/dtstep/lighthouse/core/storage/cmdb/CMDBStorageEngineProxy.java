package com.dtstep.lighthouse.core.storage.cmdb;


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
