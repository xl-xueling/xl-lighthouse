package com.dtstep.lighthouse.core.test.stat;

import com.dtstep.lighthouse.client.LightHouse;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class SendTest {

    @Test
    public void testCount() throws Exception{
        LightHouse.init("10.206.6.13:4061");
        for(int i=0;i<10000;i++){
            Map<String,Object> map = new HashMap<>();
            LightHouse.stat("Cim:test_stat","eL23ySRbVto4KkT4pbcqfQbdV3Ylkl1MmVsnLgRb",map,System.currentTimeMillis());
            Thread.sleep(300);
        }
        System.out.println("send success!");
        Thread.sleep(100000);

    }
}
