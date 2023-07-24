package com.dtstep.lighthouse.common.enums.fusing;
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
import java.util.concurrent.TimeUnit;

public enum FusingRules {

    CLIENT_EXCEPTION_RULE("ClientTimeOutFusing",3,10,TimeUnit.MINUTES),

    CLIENT_MULTI_EXECUTE_RULE("ClientMultiExecuteFusing",1,3,TimeUnit.MINUTES),

    CLIENT_DATA_QUERY_RULE("ClientDataQueryFusing",1500,5,TimeUnit.SECONDS),

    ;

    private String fusingName;

    private int threshold;

    private int duration;

    private TimeUnit timeUnit;

    FusingRules(String fusingName, int threshold, int duration, TimeUnit timeUnit){
        this.fusingName = fusingName;
        this.threshold = threshold;
        this.duration = duration;
        this.timeUnit = timeUnit;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    public String getFusingName() {
        return fusingName;
    }

    public void setFusingName(String fusingName) {
        this.fusingName = fusingName;
    }
}
