package com.dtstep.lighthouse.core.storage.warehouse;


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
            storageEngine  = new HBaseWarehouseStorageEngine();
        }else if("mysql".equals(engine)){
            storageEngine  = new MySQLWarehouseStorageEngine();
        }else{
            throw new RuntimeException("warehouse engine["+engine+"] not support!");
        }
    }

    public static WarehouseStorageEngine getInstance(){
        return storageEngine;
    }

}
