package com.dtstep.lighthouse.common.exception;

public class StatisticNotFoundException extends RuntimeException {

    public StatisticNotFoundException() {
        super();
    }

    public StatisticNotFoundException(String message) {
        super(message);
    }

    public StatisticNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public StatisticNotFoundException(Throwable cause) {
        super(cause);
    }

    protected StatisticNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
