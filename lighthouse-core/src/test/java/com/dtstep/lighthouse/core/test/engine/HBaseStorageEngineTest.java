package com.dtstep.lighthouse.core.test.engine;

import com.dtstep.lighthouse.core.config.LDPConfig;
import com.dtstep.lighthouse.core.storage.engine.StorageEngineProxy;
import org.junit.Test;

public class HBaseStorageEngineTest {

    static {
        try{
            LDPConfig.initWithHomePath("/Users/xueling/lighthouse");
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Test
    public void testCreateNamespace() throws Exception {
        StorageEngineProxy.getInstance().createNamespace("ssvs");
    }

    @Test
    public void testCreateTable() throws Exception {
        String tableName = "ssvs:table_abc";
        StorageEngineProxy.getInstance().createTable(tableName);
    }

    @Test
    public void testDeleteTable() throws Exception {
        String tableName = "ssvs:table_abc";
        StorageEngineProxy.getInstance().dropTable(tableName);
    }

    @Test
    public void testIncrement() throws Exception {

    }
}
