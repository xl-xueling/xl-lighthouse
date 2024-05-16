package com.dtstep.lighthouse.core.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;

public final class ScheduledThreadPoolBuilder {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledThreadPoolBuilder.class);

    public static ScheduledThreadPoolExecutor newScheduledThreadPoolExecutor(int corePoolSize, ThreadFactory threadFactory){
        return new ScheduledThreadPoolExecutor(corePoolSize, threadFactory) {

            @Override
            protected void afterExecute(Runnable runnable, Throwable throwable) {
                String thread = Thread.currentThread().getName();
                super.afterExecute(runnable, throwable);
                if (throwable == null && runnable instanceof Future<?>) {
                    try {
                        Future<?> future = (Future<?>) runnable;
                        if (future.isDone()) {
                            future.get();
                            logger.info("thread:{} execute completed!",thread);
                        }
                    } catch (InterruptedException ex) {
                        logger.error("thread:{} interrupted!",thread,ex);
                        Thread.currentThread().interrupt();
                    } catch (Exception | Error ex) {
                        logger.error("thread:{} execute failed!",thread,ex);
                    }
                } else if (throwable != null) {
                    logger.error("thread:{} execute failed!",thread,throwable);
                }
            }
        };
    }
}
