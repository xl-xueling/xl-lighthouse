package com.dtstep.lighthouse.tasks.test;

import com.dtstep.lighthouse.core.config.LDPConfig;
import com.dtstep.lighthouse.core.storage.warehouse.StorageEngineProxy;
import org.junit.Test;

public class HBaseStorageEngineTest {

    @Test
    public void testCreateNamespace() throws Exception {
        LDPConfig.loadConfiguration();
        System.out.println("---11");
        StorageEngineProxy.getInstance().createNamespaceIfNotExist("sss");
        System.out.println("ss");
    }
}
