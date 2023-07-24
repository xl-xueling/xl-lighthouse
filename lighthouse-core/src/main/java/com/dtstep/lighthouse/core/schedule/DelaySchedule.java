package com.dtstep.lighthouse.core.schedule;
/*
 * Copyright (C) 2022-2023 XueLing.雪灵
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
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public final class DelaySchedule {

    private static final int threadSize = 2;

    private static final ScheduledExecutorService service = Executors.newScheduledThreadPool(threadSize,
            new BasicThreadFactory.Builder().namingPattern("delay-consumer-schedule-pool-%d").daemon(true).build());

    public void delaySchedule(Runnable runnable, long interval, TimeUnit timeUnit){
        service.schedule(runnable,interval,timeUnit);
    }

}
