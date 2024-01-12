package com.dtstep.lighthouse.insights.dto;

import java.io.Serializable;
import java.util.List;

public class StatValue implements Serializable {

    private long batchTime;

    private String displayBatchTime;

    private Object value = 0;

    private List<Object> statesValue;

    private long updateTime;

    public long getBatchTime() {
        return batchTime;
    }

    public void setBatchTime(long batchTime) {
        this.batchTime = batchTime;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public List<Object> getStatesValue() {
        return statesValue;
    }

    public void setStatesValue(List<Object> statesValue) {
        this.statesValue = statesValue;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getDisplayBatchTime() {
        return displayBatchTime;
    }

    public void setDisplayBatchTime(String displayBatchTime) {
        this.displayBatchTime = displayBatchTime;
    }
}
