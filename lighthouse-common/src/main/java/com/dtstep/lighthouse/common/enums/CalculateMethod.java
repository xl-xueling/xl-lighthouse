package com.dtstep.lighthouse.common.enums;

public enum CalculateMethod {

    ADD(1),

    SUB(2),

    MUL(3),

    DIVIDE(4),

    MAX(5),

    MIN(6),

    AVG(7),

    ;

    private Integer type;


    CalculateMethod(int type){
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

}
