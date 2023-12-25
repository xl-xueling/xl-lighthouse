package com.dtstep.lighthouse.insights.modal;

import com.dtstep.lighthouse.insights.enums.LimitedStrategyEnum;

public class LimitedConfig {

    private LimitedStrategyEnum strategy;

    private Integer threshold;

    public LimitedStrategyEnum getStrategy() {
        return strategy;
    }

    public void setStrategy(LimitedStrategyEnum strategy) {
        this.strategy = strategy;
    }

    public Integer getThreshold() {
        return threshold;
    }

    public void setThreshold(Integer threshold) {
        this.threshold = threshold;
    }
}
