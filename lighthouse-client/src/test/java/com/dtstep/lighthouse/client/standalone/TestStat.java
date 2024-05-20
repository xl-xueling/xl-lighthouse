package com.dtstep.lighthouse.client.standalone;

import com.dtstep.lighthouse.client.LightHouse;
import org.junit.Test;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class TestStat extends StandaloneBaseTest{

    @Test
    public void testCount() throws Exception {
        long t = System.currentTimeMillis();
        HashMap<String,Object> paramMap = new HashMap<>();
        paramMap.put("province", ThreadLocalRandom.current().nextInt(10));
        paramMap.put("city", ThreadLocalRandom.current().nextInt(10));
        paramMap.put("score",ThreadLocalRandom.current().nextDouble(10));
        LightHouse.stat("fFY:test_standalone","jRpdnhXGdRV4FMAyO0zIYr0Fcn5ZFrlf6WxaiTzR",paramMap,t);
        Thread.sleep(30 * 1000);
    }
}
