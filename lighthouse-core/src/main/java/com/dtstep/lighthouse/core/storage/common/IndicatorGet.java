package com.dtstep.lighthouse.core.storage.common;
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
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Objects;

public class IndicatorGet implements Serializable, Comparable<IndicatorGet> {

    private int statId;

    private int indicatorIndex;

    private String dimensValue;

    private long batchTime;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        IndicatorGet other = (IndicatorGet) obj;
        if (batchTime != other.batchTime) {
            return false;
        }

        if (statId != other.statId) {
            return false;
        }

        if (indicatorIndex != other.indicatorIndex) {
            return false;
        }
        return Objects.equals(dimensValue, other.dimensValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(statId, indicatorIndex, dimensValue, batchTime);
    }

    @Override
    public int compareTo(@NotNull IndicatorGet o) {
        if (batchTime != o.batchTime) {
            return Double.compare(batchTime,o.batchTime);
        }
        if (statId != o.statId) {
            return Double.compare(statId,o.statId);
        }

        if (indicatorIndex != o.indicatorIndex) {
            return Double.compare(indicatorIndex,o.indicatorIndex);
        }
        return String.valueOf(dimensValue).compareTo(String.valueOf(o.dimensValue));
    }

    public int getStatId() {
        return statId;
    }

    public void setStatId(int statId) {
        this.statId = statId;
    }

    public int getIndicatorIndex() {
        return indicatorIndex;
    }

    public void setIndicatorIndex(int indicatorIndex) {
        this.indicatorIndex = indicatorIndex;
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
}
