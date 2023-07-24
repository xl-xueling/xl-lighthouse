package com.dtstep.lighthouse.common.key;

import com.dtstep.lighthouse.common.util.Md5Util;
import org.junit.Test;

import java.util.UUID;

public class RandomIDTest {

    @Test
    public void idTest(){
        long t1 = System.currentTimeMillis();
        for(int i=0;i<1000000;i++){
            String id = RandomID.id(20);
        }
        long t2 = System.currentTimeMillis();

        long t3 = System.currentTimeMillis();
        for(int i=0;i<1000000;i++){
            String id = UUID.randomUUID().toString();
        }
        long t4 = System.currentTimeMillis();

        long t5 = System.currentTimeMillis();
        for(int i=0;i<1000000;i++){
            String id = Md5Util.get16MD5(System.currentTimeMillis() + "");
        }
        long t6 = System.currentTimeMillis();
        System.out.println("cost1:" + (t2 - t1) + ",cost2:" + (t4 - t3) + ",cost3:"+ (t6 - t5));
    }

    @Test
    public void idTest2() throws Exception{
        int size = 1000000;
        Object object = new Object();
        for(int n =0;n<100;n++){
            long t1 = System.currentTimeMillis();
            for(int i=0;i<size;i++){
                String id = RandomID.id(10);
            }
            long t2 = System.currentTimeMillis();

            long t3 = System.currentTimeMillis();
            for(int i=0;i<size;i++){
                char[] chars = RandomID.idOfChar(10);
            }
            long t4 = System.currentTimeMillis();

            long t5 = System.currentTimeMillis();
            for(int i=0;i<size;i++){
                String id = Md5Util.get16MD5(System.currentTimeMillis() + "");
            }
            long t6 = System.currentTimeMillis();
            System.out.println("cost1:" + (t2 - t1) + ",cost2:" + (t4 - t3) + ",cost3:" + (t6 - t5));
            Thread.sleep(1000);
        }
    }
}
