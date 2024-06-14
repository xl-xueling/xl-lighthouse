package com.dtstep.lighthouse.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

public enum ChartTypeEnum implements Serializable  {

    LINE_CHART(1),

    LINE_AREA_CHART(2),

    BAR_CHART(3),

    PIE_CHART(4),

    ;

    @JsonValue
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    ChartTypeEnum(int type){
        this.type = type;
    }

    public static ChartTypeEnum getChartTypeEnum(int type){
        for(ChartTypeEnum chartTypeEnum : ChartTypeEnum.values()){
            if(chartTypeEnum.getType() == type){
                return chartTypeEnum;
            }
        }
        return null;
    }
}
