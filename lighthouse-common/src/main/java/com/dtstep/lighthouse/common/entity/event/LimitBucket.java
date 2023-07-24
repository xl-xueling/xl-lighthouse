package com.dtstep.lighthouse.common.entity.event;

import com.dtstep.lighthouse.common.entity.calculate.MicroBucket;
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
public class LimitBucket extends SlotEvent<LimitBucket>{

    private static final long serialVersionUID = -8809784354879857182L;

    private int statId;

    private long batchTime;

    private String dimensValue;

    public LimitBucket(MicroBucket microBucket){
        this.statId = microBucket.getStatId();
        this.batchTime = microBucket.getBatchTime();
        this.dimensValue = microBucket.getDimensValue();
    }

    @Override
    public int compareTo(LimitBucket o) {
        if(this.getTimestamp() > o.getTimestamp()){
            return 1;
        }else if(this.getTimestamp() == o.getTimestamp()){
            return (this.getStatId() + "_" + this.getBatchTime()).compareTo(o.getStatId() + "_" + o.getBatchTime());
        }else {
            return -1;
        }
    }

    public int getStatId() {
        return statId;
    }

    public void setStatId(int statId) {
        this.statId = statId;
    }

    public long getBatchTime() {
        return batchTime;
    }

    public void setBatchTime(long batchTime) {
        this.batchTime = batchTime;
    }

    public String getDimensValue() {
        return dimensValue;
    }

    public void setDimensValue(String dimensValue) {
        this.dimensValue = dimensValue;
    }
}
