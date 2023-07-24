package com.dtstep.lighthouse.common.util;

import org.junit.Test;

import java.util.Random;
import java.util.UUID;

public class Md5UtilTest {

    @Test
    public void md5Test() throws Exception{
        String message = "ab\u0001cd";
        String str1 = UUID.randomUUID().toString();
        String str2 = new Random().nextInt(10000)+"";
        for(int m=0;m<100;m++){
            long t1 = System.currentTimeMillis();
            for(int i=0;i<100000;i++){
                String str = Md5Util.getMD5(message);
            }
            long t2 = System.currentTimeMillis();
            for(int i=0;i<100000;i++){
                long t = System.currentTimeMillis();
            }
            long t3 = System.currentTimeMillis();
            System.out.println("cost1:" + (t2 - t1) + ",cost2:" + (t3 - t2));
            Thread.sleep(1000);
        }
    }
}
