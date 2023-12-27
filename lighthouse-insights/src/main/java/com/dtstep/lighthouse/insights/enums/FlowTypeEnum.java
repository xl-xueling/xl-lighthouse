package com.dtstep.lighthouse.insights.enums;

public enum FlowTypeEnum {

    PROJECT_ACCESS(1),

    STAT_ACCESS(2),

    GROUP_THRESHOLD_ADJUST(3),

    STAT_ITEM_APPROVE(4),


    ;

    FlowTypeEnum(int type){
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
