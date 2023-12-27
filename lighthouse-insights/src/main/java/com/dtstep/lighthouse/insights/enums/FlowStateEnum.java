package com.dtstep.lighthouse.insights.enums;

public enum FlowStateEnum {

    PROCESSING(1),

    APPROVED(2),

    REJECTED(3),

    RETRACTED(4),

    ;

    FlowStateEnum(int state){
        this.state = state;
    }

    private int state;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
