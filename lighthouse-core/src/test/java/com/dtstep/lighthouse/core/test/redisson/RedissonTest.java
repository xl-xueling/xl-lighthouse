package com.dtstep.lighthouse.core.test.redisson;

import com.dtstep.lighthouse.core.config.LDPConfig;
import com.dtstep.lighthouse.core.lock.RedissonLock;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class RedissonTest {

    static {
        try{
            LDPConfig.initWithHomePath("/Users/xueling/lighthouse");
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Test
    public void testLock() throws Exception {
        String key = "test123";
        boolean is = RedissonLock.tryLock(key,0,5, TimeUnit.SECONDS);
        if(is){
            try{
                System.out.println("get lock!");
            }catch (Exception ex){
                ex.printStackTrace();
                RedissonLock.unLock(key);
            }
        }
    }
}
