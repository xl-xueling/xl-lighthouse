package com.dtstep.lighthouse.common.modal;

import com.dtstep.lighthouse.common.enums.ChartTypeEnum;

public class RenderChartConfig {

    private int functionIndex;

    private String title;

    private ChartTypeEnum chartType;

    public int getFunctionIndex() {
        return functionIndex;
    }

    public void setFunctionIndex(int functionIndex) {
        this.functionIndex = functionIndex;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ChartTypeEnum getChartType() {
        return chartType;
    }

    public void setChartType(ChartTypeEnum chartType) {
        this.chartType = chartType;
    }
}
