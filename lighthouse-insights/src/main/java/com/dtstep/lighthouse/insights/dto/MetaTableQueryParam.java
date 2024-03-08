package com.dtstep.lighthouse.insights.dto;

import java.time.LocalDateTime;

public class MetaTableQueryParam {

    private Long maxRecordSize;

    private Long maxContentSize;

    private LocalDateTime startDate;

    public Long getMaxRecordSize() {
        return maxRecordSize;
    }

    public void setMaxRecordSize(Long maxRecordSize) {
        this.maxRecordSize = maxRecordSize;
    }

    public Long getMaxContentSize() {
        return maxContentSize;
    }

    public void setMaxContentSize(Long maxContentSize) {
        this.maxContentSize = maxContentSize;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }
}
