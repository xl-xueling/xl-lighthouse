package com.dtstep.lighthouse.insights.dto;

import javax.validation.constraints.NotNull;

public class MetricBindElement {

    @NotNull
    private Integer id;

    @NotNull
    private MetricBindType type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public MetricBindType getType() {
        return type;
    }

    public void setType(MetricBindType type) {
        this.type = type;
    }
}
