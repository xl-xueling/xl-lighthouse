package com.dtstep.lighthouse.common.entity.monitor;

import com.dtstep.lighthouse.common.enums.RunningMode;

import java.io.Serializable;

public class ClusterInfo implements Serializable {

    private RunningMode runningMode;

    private long startTime;

    private long runningTime;

    public RunningMode getRunningMode() {
        return runningMode;
    }

    public void setRunningMode(RunningMode runningMode) {
        this.runningMode = runningMode;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getRunningTime() {
        return runningTime;
    }

    public void setRunningTime(long runningTime) {
        this.runningTime = runningTime;
    }
}
