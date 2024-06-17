package com.dtstep.lighthouse.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

public enum ChartTypeEnum implements Serializable  {

    LINE_CHART(1),

    LINE_AREA_CHART(2),

    BAR_CHART(3),

    PIE_CHART(4),

    ;

    @JsonValue
    private int chartType;

    public int getChartType() {
        return chartType;
    }

    public void setChartType(int chartType) {
        this.chartType = chartType;
    }

    ChartTypeEnum(int chartType){
        this.chartType = chartType;
    }

    @JsonCreator
    public static ChartTypeEnum fromType(int type){
        for(ChartTypeEnum chartTypeEnum : ChartTypeEnum.values()){
            if(chartTypeEnum.getChartType() == type){
                return chartTypeEnum;
            }
        }
        return null;
    }
}
