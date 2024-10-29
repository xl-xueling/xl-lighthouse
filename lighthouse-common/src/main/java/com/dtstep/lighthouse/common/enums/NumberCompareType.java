package com.dtstep.lighthouse.common.enums;

public enum NumberCompareType {

    GT(1),

    GE(2),

    LT(3),

    LE(4),

    EQ(5),

    ;

    private Integer type;

    NumberCompareType(int type){
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }


}
