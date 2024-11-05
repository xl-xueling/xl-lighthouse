package com.dtstep.lighthouse.core.storage.common;

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
        return this.statId - o.statId;
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
