package com.dtstep.lighthouse.common.schedule;
/*
 * Copyright (C) 2022-2024 XueLing.雪灵
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
                super.afterExecute(runnable, throwable);
                String threadName = Thread.currentThread().getName();
                if (throwable == null && runnable instanceof Future<?>) {
                    try {
                        Future<?> future = (Future<?>) runnable;
                        if (future.isDone()) {
                            future.get();
                            logger.info("thread:{} execute completed!",threadName);
                        }
                    } catch (InterruptedException ex) {
                        logger.error("thread:{} interrupted!",threadName,ex);
                    } catch (Exception | Error ex) {
                        logger.error("thread:{} execute failed!",threadName,ex);
                    }
                } else if (throwable != null) {
                    logger.error("thread:{} execute failed!",threadName,throwable);
                }
            }
        };
    }
}
