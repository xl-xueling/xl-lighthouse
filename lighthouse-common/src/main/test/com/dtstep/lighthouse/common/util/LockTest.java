package com.dtstep.lighthouse.common.util;

import org.junit.Test;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

public class LockTest {

    @Test
    public void testlock() throws Exception{
        int size = 1000000;
        final ReentrantLock lock = new ReentrantLock(false);
        AtomicBoolean flag = new AtomicBoolean(true);
        Semaphore semaphore = new Semaphore(1,false);
        int t = 0;
        for(int n =0;n<100;n++){
            long t1 = System.currentTimeMillis();
            for(int i=0;i<size;i++){
                lock.lock();
                t++;
                lock.unlock();
            }
            long t2 = System.currentTimeMillis();

            long t3 = System.currentTimeMillis();
            for(int i=0;i<size;i++){
                while (true){
                    if(flag.getAndSet(false)){
                        i++;
                        flag.set(true);
                        break;
                    }
                }
            }
            long t4 = System.currentTimeMillis();
            System.out.println("cost1:" + (t2 - t1) + ",cost2:" + (t4 - t3));
            Thread.sleep(1000);
        }
    }
}
