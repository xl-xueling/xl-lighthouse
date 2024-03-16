package com.dtstep.lighthouse.insights.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

public class LimitStatQueryParam implements Serializable {

    @NotNull
    private Integer statId;

    @NotNull
    private List<Long> batchTimeList;

    public Integer getStatId() {
        return statId;
    }

    public void setStatId(Integer statId) {
        this.statId = statId;
    }

    public List<Long> getBatchTimeList() {
        return batchTimeList;
    }

    public void setBatchTimeList(List<Long> batchTimeList) {
        this.batchTimeList = batchTimeList;
    }
}
