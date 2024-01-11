package com.dtstep.lighthouse.insights.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

public enum ComponentTypeEnum {

    DATEPICKER_DATE_SELECT(1),

    DATEPICKER_DATE_RANGE_SELECT(2),

    DATEPICKER_DATE_TIME_RANGE_SELECT(3),

    FILTER_INPUT(4),

    FILTER_SELECT(5),

    FILTER_REMOTE_SEARCH_SELECT(6),

    ;

    @JsonValue
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

    @JsonCreator
    public static ComponentTypeEnum forValue(int renderType){
        ComponentTypeEnum[] values = ComponentTypeEnum.values();
        return Stream.of(values).filter(it -> it.getRenderType() == renderType).findAny().orElse(null);
    }
}
