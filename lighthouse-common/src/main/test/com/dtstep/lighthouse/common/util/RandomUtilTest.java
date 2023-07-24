package com.dtstep.lighthouse.common.util;

import com.dtstep.lighthouse.common.key.RandomID;
import org.junit.Test;

import java.util.Random;

public class RandomUtilTest {

    @Test
    public void testRandom() throws Exception{
        int size = 10000000;
        for(int n =0;n<100;n++){
            long t1 = System.currentTimeMillis();
            for(int i=0;i<size;i++){
                String s = RandomID.id(20);
            }
            long t2 = System.currentTimeMillis();

            Random random = new Random();
            long t3 = System.currentTimeMillis();
            for(int i=0;i<size;i++){
                String s = RandomID.id(20);
            }
            long t4 = System.currentTimeMillis();

            System.out.println("cost1:" + (t2 - t1) + ",cost2:" + (t4 - t3));
            Thread.sleep(1000);
        }
    }
}
