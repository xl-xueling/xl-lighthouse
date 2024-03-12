package com.dtstep.lighthouse.core.test.stat;

import com.dtstep.lighthouse.client.LightHouse;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class SendTest {

    @Test
    public void testCount() throws Exception{
        LightHouse.init("10.206.6.28:4061");
        long t = System.currentTimeMillis();
        for(int i=0;i<10;i++){
            Map<String,Object> map = new HashMap<>();
            map.put("randomId", UUID.randomUUID().toString());
            map.put("province", ThreadLocalRandom.current().nextInt(3));
            if(ThreadLocalRandom.current().nextInt(100) > 50){
                map.put("city","a");
            }else{
                map.put("city","b");
            }
            map.put("score",ThreadLocalRandom.current().nextDouble(100));
            LightHouse.stat("AaZ:test_stat","yPqIatFePKiyYnMZ8UpPQpQuigiWfR3JjaWjSehN",map,t);
        }
        System.out.println("send success!");
        Thread.sleep(100000);

    }
}
