package com.dtstep.lighthouse.insights.enums;

public enum RenderTypeEnum {

    DATEPICKER_DATE_SELECT(1,ComponentTypeEnum.DatePickerComponent),

    DATEPICKER_DATE_RANGE_SELECT(2,ComponentTypeEnum.DatePickerComponent),

    DATEPICKER_DATE_TIME_RANGE_SELECT(3,ComponentTypeEnum.DatePickerComponent),

    FILTER_INPUT(4,ComponentTypeEnum.FilterComponent),

    FILTER_SELECT(5,ComponentTypeEnum.FilterComponent),

    FILTER_REMOTE_SEARCH_SELECT(6,ComponentTypeEnum.FilterComponent),


    ;

    private int renderType;

    private ComponentTypeEnum componentTypeEnum;

    public int getRenderType() {
        return renderType;
    }

    public void setRenderType(int renderType) {
        this.renderType = renderType;
    }

    public ComponentTypeEnum getComponentTypeEnum() {
        return componentTypeEnum;
    }

    public void setComponentTypeEnum(ComponentTypeEnum componentTypeEnum) {
        this.componentTypeEnum = componentTypeEnum;
    }

    RenderTypeEnum(int renderType, ComponentTypeEnum componentTypeEnum){
        this.renderType = renderType;
        this.componentTypeEnum = componentTypeEnum;
    }
}
