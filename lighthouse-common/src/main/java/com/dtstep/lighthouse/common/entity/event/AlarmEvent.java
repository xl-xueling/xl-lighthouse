package com.dtstep.lighthouse.common.entity.event;


import java.util.Objects;

public class AlarmEvent extends SlotEvent<AlarmEvent> {

    private int statId;

    private long batchTime;

    private String dimensValue;

    public AlarmEvent(){}

    public AlarmEvent(Integer statId,Long batchTime,String dimensValue){
        this.statId = statId;
        this.batchTime = batchTime;
        this.dimensValue = dimensValue;
    }

    @Override
    public int compareTo(AlarmEvent o) {
        if(this.getTimestamp() > o.getTimestamp()){
            return 1;
        }else if(this.getTimestamp() == o.getTimestamp()){
            return (this.getStatId() + "_" + this.getDimensValue()).compareTo(o.getStatId() + "_" + o.getDimensValue());
        }else {
            return -1;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(statId, batchTime, dimensValue);
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
