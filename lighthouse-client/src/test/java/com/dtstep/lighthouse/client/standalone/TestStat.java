package com.dtstep.lighthouse.client.standalone;

import com.dtstep.lighthouse.client.LightHouse;
import com.dtstep.lighthouse.common.util.DateUtil;
import org.junit.Test;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class TestStat extends StandaloneBaseTest{

    @Test
    public void testCount() throws Exception {
        long t = DateUtil.getCurrentHourTime();
        for(int i=0;i<1239;i++){
            HashMap<String,Object> paramMap = new HashMap<>();
            paramMap.put("province", ThreadLocalRandom.current().nextInt(10));
            paramMap.put("city", ThreadLocalRandom.current().nextInt(10));
            paramMap.put("score",ThreadLocalRandom.current().nextDouble(10));
            LightHouse.stat("HVP:test_standalone","73V8IcFZu9kckcpc8xOrN2aZLcUrFlu2E67uRcgM",paramMap,t);
            Thread.sleep(100);
        }
        Thread.sleep(50 * 1000);
    }
}
