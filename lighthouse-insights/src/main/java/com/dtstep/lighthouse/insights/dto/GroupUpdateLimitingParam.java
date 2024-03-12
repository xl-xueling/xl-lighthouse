package com.dtstep.lighthouse.insights.dto;

import com.dtstep.lighthouse.common.enums.LimitingStrategyEnum;

import javax.validation.constraints.NotNull;

public class GroupUpdateLimitingParam {

    @NotNull
    private Integer id;

    @NotNull
    private LimitingStrategyEnum strategy;

    @NotNull
    private Integer value;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LimitingStrategyEnum getStrategy() {
        return strategy;
    }

    public void setStrategy(LimitingStrategyEnum strategy) {
        this.strategy = strategy;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
