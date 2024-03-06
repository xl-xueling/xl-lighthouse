package com.dtstep.lighthouse.core.test.engine;

import com.dtstep.lighthouse.core.config.LDPConfig;
import com.dtstep.lighthouse.core.storage.engine.StorageEngineProxy;
import org.junit.Test;

public class HBaseStorageEngineTest {

    @Test
    public void testCreateNamespace() throws Exception {
        LDPConfig.initWithHomePath("/Users/xueling/lighthouse");
        System.out.println("---11");
        StorageEngineProxy.getInstance().createNamespace("sss");
        System.out.println("ss");
    }
}
