package com.dtstep.lighthouse.insights.enums;

public enum ComponentTypeEnum {

    DATEPICKER_DATE_SELECT(1),

    DATEPICKER_DATE_RANGE_SELECT(2),

    DATEPICKER_DATE_TIME_RANGE_SELECT(3),

    FILTER_INPUT(4),

    FILTER_SELECT(5),

    FILTER_REMOTE_SEARCH_SELECT(6),

    ;

    private int renderType;

    ComponentTypeEnum(int renderType){
        this.renderType = renderType;
    }

    public int getRenderType() {
        return renderType;
    }

    public void setRenderType(int renderType) {
        this.renderType = renderType;
    }
}
