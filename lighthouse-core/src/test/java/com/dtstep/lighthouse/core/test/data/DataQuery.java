package com.dtstep.lighthouse.core.test.data;

import com.dtstep.lighthouse.common.modal.Stat;
import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.core.config.LDPConfig;
import com.dtstep.lighthouse.core.rowkey.KeyGenerator;
import com.dtstep.lighthouse.core.rowkey.impl.DefaultKeyGenerator;
import com.dtstep.lighthouse.core.wrapper.StatDBWrapper;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class DataQuery {

    private static final KeyGenerator keyGenerator = new DefaultKeyGenerator();

    static {
        try{
            LDPConfig.initWithHomePath("/Users/xueling/lighthouse");
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Test
    public void countDataQuery() throws Exception {
        long batchTime = DateUtil.batchTime(1, TimeUnit.DAYS,System.currentTimeMillis());
        System.out.println("batchTime:" + DateUtil.formatTimeStamp(batchTime,"yyyy-MM-dd HH:mm:ss"));
        int statId = 1100518;
        Stat stat = StatDBWrapper.queryById(statId);
        System.out.println("stat:" + JsonUtil.toJSONString(stat));
        String aggregateKey = keyGenerator.resultKey(stat,0,null,batchTime);
        System.out.println("aggregateKey is:" + aggregateKey);
    }
}
