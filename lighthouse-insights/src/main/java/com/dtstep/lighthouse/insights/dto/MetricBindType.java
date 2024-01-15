package com.dtstep.lighthouse.insights.dto;

public enum MetricBindType {

    Project(0),

    Stat(1),

    ;

    MetricBindType(int type){
        this.type = type;
    }

    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
