package com.dtstep.lighthouse.core.reactor;
/*
 * Copyright (C) 2022-2025 XueLing.雪灵
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

    private static final long DEFAULT_TIMEOUT = 60000L;

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
