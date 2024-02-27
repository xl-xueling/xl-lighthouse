package com.dtstep.lighthouse.core.test;

import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.core.config.LDPConfig;
import com.dtstep.lighthouse.core.wrapper.StatDBWrapper;
import org.junit.Test;

public class StatDBWrapperTest {

    @Test
    public void testQueryId() throws Exception {
        LDPConfig.initWithHomePath("/Users/xueling/lighthouse");
        int statId = 1100517;
        StatExtEntity statExtEntity = StatDBWrapper.queryById(statId);
        System.out.println("statExtEntity:" + JsonUtil.toJSONString(statExtEntity));
    }
}
