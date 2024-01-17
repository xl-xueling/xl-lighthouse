package com.dtstep.lighthouse.insights.dto_bak;

import javax.validation.constraints.NotNull;
import java.util.List;

public class MetricBindParam {

    @NotNull
    private List<Integer> metricIds;

    @NotNull
    private List<MetricBindElement> bindElements;

    public List<Integer> getMetricIds() {
        return metricIds;
    }

    public void setMetricIds(List<Integer> metricIds) {
        this.metricIds = metricIds;
    }

    public List<MetricBindElement> getBindElements() {
        return bindElements;
    }

    public void setBindElements(List<MetricBindElement> bindElements) {
        this.bindElements = bindElements;
    }
}
