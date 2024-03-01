package com.dtstep.lighthouse.core.test.rowkey;

import com.dtstep.lighthouse.common.modal.Stat;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.core.config.LDPConfig;
import com.dtstep.lighthouse.core.rowkey.impl.DefaultKeyGenerator;
import com.dtstep.lighthouse.core.wrapper.StatDBWrapper;
import org.junit.Test;

public class KeyGeneratorTest {

    @Test
    public void testGeneratorKey() throws Exception {
        System.out.println("---11");
        LDPConfig.initWithHomePath("/Users/xueling/lighthouse");
        Stat stat = StatDBWrapper.queryById(1100517);
        System.out.println("stat:" + JsonUtil.toJSONString(stat));
        DefaultKeyGenerator keyGenerator = new DefaultKeyGenerator();
        String key = keyGenerator.resultKey(stat,0,"sss",System.currentTimeMillis());
        System.out.println("key:" + key);
    }
}
