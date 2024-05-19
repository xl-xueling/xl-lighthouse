package com.dtstep.lighthouse.core.test.config;

import com.dtstep.lighthouse.core.config.LDPConfig;
import org.junit.Test;

public class TestLDPConfig {

    @Test
    public void testLoadConfig() throws Exception {
        LDPConfig.init("/Users/xueling/lighthouse/conf/ldp-site-standalone.xml");
    }
}
