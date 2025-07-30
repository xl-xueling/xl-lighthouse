package com.dtstep.lighthouse.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

public enum ViewElementType {

    VCHART_BASIC_LINE(1),
    VCHART_SMOOTH_LINE(2),
    VCHART_STEP_LINE(3),
    VCHART_BASIC_BAR(4),
    VCHART_STACKED_BAR(5),
    VCHART_HORIZONTAL_BAR(6),
    VCHART_GRADIENT_BAR(7),
    VCHART_BASIC_PIE(8),
    VCHART_BASIC_RING(9),
    VCHART_BASIC_FUNNEL(10),
    VCHART_BASIC_LINE_AREA(11),
    VCHART_SMOOTH_LINE_AREA(12),
    ECHART_BASIC_LINE(13),
    TEXT_BASIC(50),
    TEXT_GRADIENT(51),
    TEXT_BARRAGE(52),
    DATE_BASIC(53),
    MATERIAL_BACKGROUND(54),
    MATERIAL_BORDER(55),
    FILTER(56),
    SELECT(57),
    DATE_PICKER(58),
    TEMPLATE(59),

    Complex(999),
    ;

    ViewElementType(int elementType){
        this.elementType = elementType;
    }

    @JsonValue
    private int elementType;

    public int getElementType() {
        return elementType;
    }

    public void setElementType(int elementType) {
        this.elementType = elementType;
    }

    @JsonCreator
    public static ViewElementType forValue(int value){
        ViewElementType[] values = ViewElementType.values();
        return Stream.of(values).filter(it -> it.getElementType() == value).findAny().orElse(null);
    }
}
