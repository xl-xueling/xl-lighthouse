package com.dtstep.lighthouse.common.entity;

import java.util.List;

public class ViewStatBindItem {

    private Integer statId;

    private Integer indicator;

    private String timePeriod;

    private String timePoint;

    private String alias;

    private List<LabelValue> dimens;

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

    public String getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(String timePeriod) {
        this.timePeriod = timePeriod;
    }

    public String getTimePoint() {
        return timePoint;
    }

    public void setTimePoint(String timePoint) {
        this.timePoint = timePoint;
    }

    public List<LabelValue> getDimens() {
        return dimens;
    }

    public void setDimens(List<LabelValue> dimens) {
        this.dimens = dimens;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
