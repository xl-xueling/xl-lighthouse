package com.dtstep.lighthouse.common.entity.calculate;
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

import com.dtstep.lighthouse.common.entity.event.SlotEvent;

public final class MicroBucket extends SlotEvent<MicroBucket> {

    private static final long serialVersionUID = 3765407294643408871L;

    private int statId;

    private String rowKey;

    private String metaName;

    private long value;

    private int functionIndex;

    private String dimensValue;

    private long batchTime;

    private boolean isLimit = false;

    private long ttl;

    private MicroCalculateEnum calculateEnum;

    private String column = "v";

    private MicroBucket(int statId, String metaName, String rowKey, boolean isLimit, MicroCalculateEnum calculateEnum, String dimensValue, long value, long batchTime, int functionIndex,String column,long ttl){
        this.statId = statId;
        this.metaName = metaName;
        this.rowKey = rowKey;
        this.isLimit = isLimit;
        this.calculateEnum = calculateEnum;
        this.dimensValue = dimensValue;
        this.value = value;
        this.batchTime = batchTime;
        this.functionIndex = functionIndex;
        this.column = column;
        this.ttl = ttl;
    }

    @Override
    public int compareTo(MicroBucket o) {
        if(this.getTimestamp() > o.getTimestamp()){
            return 1;
        }else if(this.getTimestamp() == o.getTimestamp()){
            return (this.getRowKey() + ";" + this.getColumn()).compareTo(o.getRowKey() + ";" + o.getColumn());
        }else {
            return -1;
        }
    }

    public static class Builder {
        private int statId;
        private String rowKey;
        private String metaName;
        private long value;
        private String dimensValue;
        private long batchTime;
        private boolean isLimit = false;
        private MicroCalculateEnum calculateEnum;
        private int functionIndex;
        private String column = "v";
        private long ttl;
        public Builder() {}

        public Builder setStatId(int statId) {
            this.statId = statId;
            return this;
        }

        public Builder setMetaName(String metaName){
            this.metaName = metaName;
            return this;
        }

        public Builder setRowKey(String rowKey) {
            this.rowKey = rowKey;
            return this;
        }

        public Builder setDimensValue(String dimensValue) {
            this.dimensValue = dimensValue;
            return this;
        }

        public Builder isLimit(boolean isLimit) {
            this.isLimit = isLimit;
            return this;
        }

        public Builder setValue(long value) {
            this.value = value;
            return this;
        }

        public Builder setBatchTime(long batchTime) {
            this.batchTime = batchTime;
            return this;
        }

        public Builder setColumn(String column) {
            this.column = column;
            return this;
        }

        public Builder setCalculateEnum(MicroCalculateEnum calculateEnum) {
            this.calculateEnum = calculateEnum;
            return this;
        }

        public Builder setTTL(long ttl){
            this.ttl = ttl;
            return this;
        }

        public Builder setFunctionIndex(int functionIndex){
            this.functionIndex = functionIndex;
            return this;
        }

        public MicroBucket create() {
            return new MicroBucket(statId, metaName, rowKey, isLimit,calculateEnum,dimensValue, value, batchTime,functionIndex,column, ttl);
        }
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getRowKey() {
        return rowKey;
    }

    public void setRowKey(String rowKey) {
        this.rowKey = rowKey;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public boolean isLimit() {
        return isLimit;
    }

    public void setLimit(boolean limit) {
        isLimit = limit;
    }

    public String getDimensValue() {
        return dimensValue;
    }

    public void setDimensValue(String dimensValue) {
        this.dimensValue = dimensValue;
    }

    public String getMetaName() {
        return metaName;
    }

    public void setMetaName(String metaName) {
        this.metaName = metaName;
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

    public MicroCalculateEnum getCalculateEnum() {
        return calculateEnum;
    }

    public void setCalculateEnum(MicroCalculateEnum calculateEnum) {
        this.calculateEnum = calculateEnum;
    }

    public long getTTL() {
        return ttl;
    }

    public void setTTL(long ttl) {
        this.ttl = ttl;
    }

    public long getTtl() {
        return ttl;
    }

    public void setTtl(long ttl) {
        this.ttl = ttl;
    }

    public int getFunctionIndex() {
        return functionIndex;
    }

    public void setFunctionIndex(int functionIndex) {
        this.functionIndex = functionIndex;
    }
}
