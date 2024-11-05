package com.dtstep.lighthouse.common.entity.event;

import java.io.Serializable;

public class AlarmEvent implements Serializable {

    private int statId;

    private long batchTime;

    private String dimensValue;

    public AlarmEvent(){}

    public AlarmEvent(Integer statId,Long batchTime,String dimensValue){
        this.statId = statId;
        this.batchTime = batchTime;
        this.dimensValue = dimensValue;
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
