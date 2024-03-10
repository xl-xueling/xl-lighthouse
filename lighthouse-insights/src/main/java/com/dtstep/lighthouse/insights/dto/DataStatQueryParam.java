package com.dtstep.lighthouse.insights.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;

public class DataStatQueryParam implements Serializable {

    @NotNull
    private Integer statId;

    private LinkedHashMap<String, String[]> dimensParams;

    @NotNull
    private LocalDateTime startTime;

    @NotNull
    private LocalDateTime endTime;

    public Integer getStatId() {
        return statId;
    }

    public void setStatId(Integer statId) {
        this.statId = statId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public LinkedHashMap<String, String[]> getDimensParams() {
        return dimensParams;
    }

    public void setDimensParams(LinkedHashMap<String, String[]> dimensParams) {
        this.dimensParams = dimensParams;
    }
}
