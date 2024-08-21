package com.dtstep.lighthouse.common.entity.view;

import java.io.Serializable;

public class OperatorValue implements Serializable {

    private long batchTime;

    private String displayBatchTime;

    private Object v;

    private long updTime;

    private long expTime;

    public long getBatchTime() {
        return batchTime;
    }

    public void setBatchTime(long batchTime) {
        this.batchTime = batchTime;
    }

    public String getDisplayBatchTime() {
        return displayBatchTime;
    }

    public void setDisplayBatchTime(String displayBatchTime) {
        this.displayBatchTime = displayBatchTime;
    }

    public Object getV() {
        return v;
    }

    public void setV(Object v) {
        this.v = v;
    }

    public long getUpdTime() {
        return updTime;
    }

    public void setUpdTime(long updTime) {
        this.updTime = updTime;
    }

    public long getExpTime() {
        return expTime;
    }

    public void setExpTime(long expTime) {
        this.expTime = expTime;
    }
}
