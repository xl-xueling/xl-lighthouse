package com.dtstep.lighthouse.core.test.rowkey;

import com.dtstep.lighthouse.common.modal.Group;
import com.dtstep.lighthouse.common.modal.Stat;
import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.core.batch.BatchAdapter;
import com.dtstep.lighthouse.core.config.LDPConfig;
import com.dtstep.lighthouse.core.rowkey.impl.DefaultKeyGenerator;
import com.dtstep.lighthouse.core.wrapper.GroupDBWrapper;
import com.dtstep.lighthouse.core.wrapper.StatDBWrapper;
import org.junit.Test;

import java.util.List;

public class KeyGeneratorTest {

    @Test
    public void testGeneratorKey() throws Exception {
        System.out.println("---11");
        LDPConfig.initWithHomePath("/Users/xueling/lighthouse");
        Stat stat = StatDBWrapper.queryById(1100517);
        System.out.println("stat:" + JsonUtil.toJSONString(stat));
        DefaultKeyGenerator keyGenerator = new DefaultKeyGenerator();
        List<Long> batchTimeList = BatchAdapter.queryBatchTimeList("1-minute", DateUtil.getDayStartTime(System.currentTimeMillis()),DateUtil.getDayEndTime(System.currentTimeMillis()));
        for(Long batchTime : batchTimeList){
            String key = keyGenerator.resultKey(stat,0,"sss",batchTime);
            System.out.println("key:" + key);
        }
    }

    @Test
    public void testGeneratorDimensKey() throws Exception {
        LDPConfig.initWithHomePath("/Users/xueling/lighthouse");
        Group group = GroupDBWrapper.queryById(100282);
        DefaultKeyGenerator keyGenerator = new DefaultKeyGenerator();
        for(int i=0;i<100;i++){
            String key = keyGenerator.dimensKey(group,"province","ss_"+i);
            System.out.println("key:" + key);
        }
    }
}
