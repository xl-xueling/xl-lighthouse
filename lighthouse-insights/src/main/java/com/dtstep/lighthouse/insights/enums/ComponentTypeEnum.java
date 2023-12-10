package com.dtstep.lighthouse.insights.enums;

public enum ComponentTypeEnum {

    FilterComponent(1),

    DatePickerComponent(2),

    ChartComponent(3),

    TableComponent(4),

    ;

    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    ComponentTypeEnum(int type){
        this.type = type;
    }
}
