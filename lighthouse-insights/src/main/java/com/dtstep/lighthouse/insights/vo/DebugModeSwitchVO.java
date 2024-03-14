package com.dtstep.lighthouse.insights.vo;

import java.io.Serializable;

public class DebugModeSwitchVO implements Serializable {

    private Long startTime;

    private Long endTime;

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }
}
