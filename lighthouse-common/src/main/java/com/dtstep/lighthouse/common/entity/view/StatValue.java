package com.dtstep.lighthouse.common.entity.view;
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

import java.io.Serializable;
import java.util.List;


public class StatValue implements Serializable {

    private static final long serialVersionUID = 3438579311994460321L;

    private long batchTime;

    private String displayBatchTime;

    private Object value = "0";

    private List<Object> statesValue;

    private String dimensValue;

    private long lastUpdateTime;

    public String getDisplayBatchTime() {
        return displayBatchTime;
    }

    public void setDisplayBatchTime(String displayBatchTime) {
        this.displayBatchTime = displayBatchTime;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getDimensValue() {
        return dimensValue;
    }

    public void setDimensValue(String dimensValue) {
        this.dimensValue = dimensValue;
    }

    public long getBatchTime() {
        return batchTime;
    }

    public void setBatchTime(long batchTime) {
        this.batchTime = batchTime;
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public List<Object> getStatesValue() {
        return statesValue;
    }

    public void setStatesValue(List<Object> statesValue) {
        this.statesValue = statesValue;
    }
}
