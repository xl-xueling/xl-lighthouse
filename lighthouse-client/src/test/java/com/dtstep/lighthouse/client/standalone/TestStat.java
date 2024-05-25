package com.dtstep.lighthouse.client.standalone;

import com.dtstep.lighthouse.client.LightHouse;
import com.dtstep.lighthouse.common.enums.RunningMode;
import com.dtstep.lighthouse.common.random.RandomID;
import com.dtstep.lighthouse.common.util.DateUtil;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class TestStat{

    @Test
    public void testCount() throws Exception {
        LightHouse.init("10.206.6.36:4061");
        String token = "3qa:test_stat";
        long t = System.currentTimeMillis();
        for(int i = 0;i<927931;i++){
            //修改统计组参数值、Token和秘钥
            Map<String,Object> map = new HashMap<>();
            map.put("uid", RandomID.id(6));
            map.put("province", ThreadLocalRandom.current().nextInt(10));
            Double d = ThreadLocalRandom.current().nextDouble(1000);
            map.put("score",String.format("%.3f", d));//防止上面随机数出现科学计数法
            LightHouse.stat(token,"EumZ8y6M4hDRrQVXYOiYMaBsHiXhQYmIUJewlrKO",map,t);
        }
        System.out.println("send ok.");
        Thread.sleep(3000000);
    }
}
