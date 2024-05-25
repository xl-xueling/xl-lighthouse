package com.dtstep.lighthouse.common.schedule;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.junit.Test;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class ScheduleTest {

    private static final AtomicLong atomicLong = new AtomicLong();

    private static ScheduledFuture<?> scheduledFuture;

    private static final ScheduledExecutorService service = ScheduledThreadPoolBuilder.
            newScheduledThreadPoolExecutor(1,new BasicThreadFactory.Builder().namingPattern("demo-consumer-schedule-pool-%d").daemon(true).build());

    @Test
    public void testCancelled() throws Exception {
        scheduledFuture = service.scheduleWithFixedDelay(new RefreshThread(),0,5, TimeUnit.SECONDS);
        Thread.sleep(TimeUnit.MINUTES.toMillis(100));
    }


    private static class RefreshThread implements Runnable {

        @Override
        public void run() {
            long v = atomicLong.getAndAdd(1L);
            if(v == 5){
                scheduledFuture.cancel(true);
            }
            System.out.println("thread:" + Thread.currentThread().getName() + " execute!");
        }
    }
}
