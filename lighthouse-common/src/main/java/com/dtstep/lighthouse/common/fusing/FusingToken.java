package com.dtstep.lighthouse.common.fusing;
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
import com.dtstep.lighthouse.common.enums.fusing.FusingRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class FusingToken implements AutoCloseable{

    private static final Logger logger = LoggerFactory.getLogger(FusingToken.class);

    private FusingRules fusingRule;

    private long windowTime;

    private long current;

    public FusingToken(FusingRules fusingRule, long windowTime,long current){
        this.fusingRule = fusingRule;
        this.windowTime = windowTime;
        this.current = current;
    }

    @Override
    public void close(){}


    public FusingRules getFusingRule() {
        return fusingRule;
    }

    public void setFusingRule(FusingRules fusingRule) {
        this.fusingRule = fusingRule;
    }

    public long getWindowTime() {
        return windowTime;
    }

    public void setWindowTime(long windowTime) {
        this.windowTime = windowTime;
    }

    public long getCurrent() {
        return current;
    }

    public void setCurrent(long current) {
        this.current = current;
    }
}
