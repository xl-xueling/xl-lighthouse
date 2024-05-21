package com.dtstep.lighthouse.client.standalone;

import com.dtstep.lighthouse.client.LightHouse;
import com.dtstep.lighthouse.common.util.DateUtil;
import org.junit.Test;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class TestStat extends StandaloneBaseTest{

    @Test
    public void testCount() throws Exception {
        long t = DateUtil.parseDate("2024-06-04 08:39:14","yyyy-MM-dd HH:mm:ss");
        for(int i=0;i<1239;i++){
            HashMap<String,Object> paramMap = new HashMap<>();
            paramMap.put("province", ThreadLocalRandom.current().nextInt(10));
            paramMap.put("city", ThreadLocalRandom.current().nextInt(10));
            paramMap.put("score",ThreadLocalRandom.current().nextDouble(10));
            LightHouse.stat("fFY:test_standalone","jRpdnhXGdRV4FMAyO0zIYr0Fcn5ZFrlf6WxaiTzR",paramMap,t);
            Thread.sleep(100);
        }
        Thread.sleep(50 * 1000);
    }
}
