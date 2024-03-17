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
        LightHouse.init("10.206.6.13:4061");
        long t = System.currentTimeMillis();
        for(int i=0;i<100000;i++){
            Map<String,Object> map = new HashMap<>();
            map.put("randomId", UUID.randomUUID().toString());
            map.put("province", ThreadLocalRandom.current().nextInt(300));
            if(ThreadLocalRandom.current().nextInt(100) > 50){
                map.put("city","a");
            }else{
                map.put("city","b");
            }
            map.put("score",ThreadLocalRandom.current().nextDouble(100));
            LightHouse.stat("test_scene_behavior_stat","JmVM5qDhpkizvJSLjgCoXa10k5j4UWJyj3LSJsPp",map,t);
            Thread.sleep(300);
        }
        System.out.println("send success!");
        Thread.sleep(100000);

    }
}
