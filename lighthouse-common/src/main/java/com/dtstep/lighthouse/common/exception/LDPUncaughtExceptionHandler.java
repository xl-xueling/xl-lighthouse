package com.dtstep.lighthouse.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LDPUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(LDPUncaughtExceptionHandler.class);

    @Override
    public void uncaughtException(Thread t, Throwable ex) {
        logger.error("Exception has been captured,Thread: {}", t.getName() , ex);
    }
}
