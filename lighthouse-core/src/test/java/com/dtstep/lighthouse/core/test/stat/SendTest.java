package com.dtstep.lighthouse.core.test.stat;

import com.dtstep.lighthouse.client.LightHouse;
import org.junit.Test;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class SendTest {

    static {
        try{
            LightHouse.init("10.206.6.13:4061");
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    @Test
    public void testCount() throws Exception{
        LightHouse.init("10.206.6.13:4061");
        long t = System.currentTimeMillis();
        List<Double> doubleList = new ArrayList<>();
        for(int i=0;i<158137;i++){
            Map<String,Object> map = new HashMap<>();
            map.put("randomId", UUID.randomUUID().toString());
            map.put("province", ThreadLocalRandom.current().nextInt(300));
            if(ThreadLocalRandom.current().nextInt(100) > 50){
                map.put("city","a");
            }else{
                map.put("city","b");
            }
            double randomValue = ThreadLocalRandom.current().nextDouble();
            DecimalFormat df = new DecimalFormat("#.###");
            String formattedValue = df.format(randomValue);
            doubleList.add(Double.parseDouble(formattedValue));
            map.put("score",formattedValue);
            LightHouse.stat("test_scene_behavior_stat","JmVM5qDhpkizvJSLjgCoXa10k5j4UWJyj3LSJsPp",map,t);
        }
        Double result = 0d;
        for(int i = 0;i<doubleList.size();i++){
            result+= doubleList.get(i);
        }
        System.out.println("double list size:" + doubleList.size() + ",sum:" + result);
        System.out.println("send success!");
        Thread.sleep(100000);
    }

    @Test
    public void testSum() throws Exception {
        long t = System.currentTimeMillis();
        Map<String,Object> map1 = new HashMap<>();
        map1.put("score","2322847798441.226");
        LightHouse.stat("test_scene_behavior_stat","JmVM5qDhpkizvJSLjgCoXa10k5j4UWJyj3LSJsPp",map1,t);
        Map<String,Object> map2 = new HashMap<>();
        map2.put("score","311381319496.367");
        LightHouse.stat("test_scene_behavior_stat","JmVM5qDhpkizvJSLjgCoXa10k5j4UWJyj3LSJsPp",map2,t);
        System.out.println("send success!");
        Thread.sleep(100000);
    }
}
