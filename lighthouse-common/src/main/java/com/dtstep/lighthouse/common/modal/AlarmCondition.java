package com.dtstep.lighthouse.common.modal;

import com.dtstep.lighthouse.common.enums.NumberCompareType;

import java.io.Serializable;

public class AlarmCondition implements Serializable {

    private Integer indicator;

    private Integer last;

    private NumberCompareType compare;

    private boolean divide;

    private ThresholdConfig overall;

    private ThresholdConfig p0;

    private ThresholdConfig p1;

    private ThresholdConfig p2;

    private ThresholdConfig p3;

    public Integer getIndicator() {
        return indicator;
    }

    public void setIndicator(Integer indicator) {
        this.indicator = indicator;
    }

    public Integer getLast() {
        return last;
    }

    public void setLast(Integer last) {
        this.last = last;
    }

    public NumberCompareType getCompare() {
        return compare;
    }

    public void setCompare(NumberCompareType compare) {
        this.compare = compare;
    }

    public boolean isDivide() {
        return divide;
    }

    public void setDivide(boolean divide) {
        this.divide = divide;
    }

    public ThresholdConfig getOverall() {
        return overall;
    }

    public void setOverall(ThresholdConfig overall) {
        this.overall = overall;
    }

    public ThresholdConfig getP0() {
        return p0;
    }

    public void setP0(ThresholdConfig p0) {
        this.p0 = p0;
    }

    public ThresholdConfig getP1() {
        return p1;
    }

    public void setP1(ThresholdConfig p1) {
        this.p1 = p1;
    }

    public ThresholdConfig getP2() {
        return p2;
    }

    public void setP2(ThresholdConfig p2) {
        this.p2 = p2;
    }

    public ThresholdConfig getP3() {
        return p3;
    }

    public void setP3(ThresholdConfig p3) {
        this.p3 = p3;
    }

    private static class ThresholdConfig {

        private Double threshold;

        private Integer silent;

        private Boolean state;

        public Double getThreshold() {
            return threshold;
        }

        public void setThreshold(Double threshold) {
            this.threshold = threshold;
        }

        public Integer getSilent() {
            return silent;
        }

        public void setSilent(Integer silent) {
            this.silent = silent;
        }

        public Boolean getState() {
            return state;
        }

        public void setState(Boolean state) {
            this.state = state;
        }
    }

}
