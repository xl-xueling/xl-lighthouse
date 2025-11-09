package com.dtstep.lighthouse.core.reactor;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.concurrent.*;

public class AsyncReactorExecutor {

    private static final ExecutorService EXECUTOR = new ThreadPoolExecutor(
            8, 20, 60L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(10000),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.AbortPolicy()
    );

    private static final Scheduler SCHEDULER = Schedulers.fromExecutor(EXECUTOR);

    private static final long DEFAULT_TIMEOUT = 10000L;

    private AsyncReactorExecutor() {
    }

    public static <T> Mono<T> executeAsync(Callable<T> task) {
        return executeAsync(task, DEFAULT_TIMEOUT);
    }

    public static <T> Mono<T> executeAsync(Callable<T> task, long timeoutMillis) {
        return Mono.fromCallable(task)
                .subscribeOn(SCHEDULER)
                .timeout(Duration.ofMillis(timeoutMillis));
    }

    public static <T> T executeBlocking(Callable<T> task, long timeoutMillis) throws Exception {
        return executeAsync(task, timeoutMillis).block(Duration.ofMillis(timeoutMillis));
    }

    public static void shutdown() {
        EXECUTOR.shutdown();
    }
}
