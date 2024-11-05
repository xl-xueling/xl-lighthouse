package com.dtstep.lighthouse.common.entity;

import java.io.Serializable;

public class IndicatorQuery implements Serializable {

    private Integer statId;

    private Integer indicator;

    private String dimensValue;

    private Long batchTime;

    private String metaName;

    public Integer getStatId() {
        return statId;
    }

    public void setStatId(Integer statId) {
        this.statId = statId;
    }

    public Integer getIndicator() {
        return indicator;
    }

    public void setIndicator(Integer indicator) {
        this.indicator = indicator;
    }

    public String getDimensValue() {
        return dimensValue;
    }

    public void setDimensValue(String dimensValue) {
        this.dimensValue = dimensValue;
    }

    public Long getBatchTime() {
        return batchTime;
    }

    public void setBatchTime(Long batchTime) {
        this.batchTime = batchTime;
    }

    public String getMetaName() {
        return metaName;
    }

    public void setMetaName(String metaName) {
        this.metaName = metaName;
    }
}
