package com.dtstep.lighthouse.common.enums;

public enum SwitchStateEnum {

    CLOSE(0),

    OPEN(1),

    ;

    SwitchStateEnum(int state){
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
