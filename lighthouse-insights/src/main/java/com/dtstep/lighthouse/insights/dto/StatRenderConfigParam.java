package com.dtstep.lighthouse.insights.dto;

import com.dtstep.lighthouse.common.modal.RenderChartConfig;
import com.dtstep.lighthouse.common.modal.RenderDateConfig;

import java.io.Serializable;
import java.util.List;

public class StatRenderConfigParam implements Serializable {

    private Integer id;

    private RenderDateConfig datepicker;

    private List<RenderChartConfig> charts;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public RenderDateConfig getDatepicker() {
        return datepicker;
    }

    public void setDatepicker(RenderDateConfig datepicker) {
        this.datepicker = datepicker;
    }

    public List<RenderChartConfig> getCharts() {
        return charts;
    }

    public void setCharts(List<RenderChartConfig> charts) {
        this.charts = charts;
    }
}
