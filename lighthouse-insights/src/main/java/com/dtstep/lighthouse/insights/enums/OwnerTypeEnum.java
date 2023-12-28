package com.dtstep.lighthouse.insights.enums;

public enum OwnerTypeEnum {

    USER(1),

    DEPARTMENT(2),

    ;

    OwnerTypeEnum(int ownerType){
        this.ownerType = ownerType;
    }

    private int ownerType;

    public int getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(int ownerType) {
        this.ownerType = ownerType;
    }
}
