package com.dtstep.lighthouse.common.entity.stat;
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

import java.io.Serializable;
import java.util.concurrent.TimeUnit;


public final class TimeParam implements Serializable {

    private static final long serialVersionUID = 3077691198272246436L;

    private int interval;

    private TimeUnit timeUnit;

    private long duration;

    public TimeParam(){}

    public TimeParam(int interval,TimeUnit timeUnit){
        this.interval = interval;
        this.timeUnit = timeUnit;
        this.duration = calculateDuration(interval,timeUnit);
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public static long calculateDuration(TimeParam timeParam){
        if(timeParam == null){
            return 0L;
        }
        return timeParam.timeUnit.toMillis(timeParam.getDuration());
    }

    public static long calculateDuration(int interval, TimeUnit timeUnit){
        return timeUnit.toMillis(interval);
    }
}
