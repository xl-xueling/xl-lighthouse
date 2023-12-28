package com.dtstep.lighthouse.insights.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum OwnerTypeEnum {

    USER(1),

    DEPARTMENT(2),

    ;

    OwnerTypeEnum(int ownerType){
        this.ownerType = ownerType;
    }

    @JsonValue
    private int ownerType;

    public int getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(int ownerType) {
        this.ownerType = ownerType;
    }
}
