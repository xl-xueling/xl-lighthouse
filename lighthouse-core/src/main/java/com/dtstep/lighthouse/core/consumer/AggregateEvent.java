package com.dtstep.lighthouse.core.consumer;
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


public final class AggregateEvent implements Serializable{

    private static final long serialVersionUID = -3642892395349223020L;

    private String aggregateKey;

    private int statId;

    private String data;

    private String dimensValue;

    private String distinctValue;

    private int functionIndex;

    private long batchTime;

    private long repeat;

    public AggregateEvent(){}

    AggregateEvent(String aggregateKey, int statId, String data, String dimensValue,int functionIndex, long batchTime, long repeat){
        this.aggregateKey = aggregateKey;
        this.statId = statId;
        this.data = data;
        this.dimensValue = dimensValue;
        this.functionIndex = functionIndex;
        this.batchTime = batchTime;
        this.repeat = repeat;
    }

    public String getAggregateKey() {
        return aggregateKey;
    }

    public void setAggregateKey(String rowKey) {
        this.aggregateKey = rowKey;
    }

    public int getStatId() {
        return statId;
    }

    public void setStatId(int statId) {
        this.statId = statId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDimensValue() {
        return dimensValue;
    }

    public void setDimensValue(String dimensValue) {
        this.dimensValue = dimensValue;
    }

    public int getFunctionIndex() {
        return functionIndex;
    }

    public void setFunctionIndex(int functionIndex) {
        this.functionIndex = functionIndex;
    }

    public long getBatchTime() {
        return batchTime;
    }

    public void setBatchTime(long batchTime) {
        this.batchTime = batchTime;
    }

    public long getRepeat() {
        return repeat;
    }

    public void setRepeat(long repeat) {
        this.repeat = repeat;
    }

    public String getDistinctValue() {
        return distinctValue;
    }

    public void setDistinctValue(String distinctValue) {
        this.distinctValue = distinctValue;
    }
}
