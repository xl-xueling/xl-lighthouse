package com.dtstep.lighthouse.core.test.stat;

import com.dtstep.lighthouse.client.LightHouse;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class SendTest {

    @Test
    public void testCount() throws Exception{
        LightHouse.init("10.206.6.25:4061");
        for(int i=0;i<10000;i++){
            Map<String,Object> map = new HashMap<>();
            LightHouse.stat("YQr:test_stat","1Ys3tlvkG8vL8uLPHslwRZcJfe8Z0tdUpRgQcfD6",map,System.currentTimeMillis());
            Thread.sleep(300);
        }
        System.out.println("send success!");
        Thread.sleep(100000);

    }
}
