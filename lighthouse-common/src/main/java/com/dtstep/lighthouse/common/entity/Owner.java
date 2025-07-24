package com.dtstep.lighthouse.common.entity;

import com.dtstep.lighthouse.common.enums.OwnerTypeEnum;

public class Owner {

    private Integer ownerId;

    private OwnerTypeEnum ownerType;

    public Owner(){}

    public Owner(Integer ownerId,OwnerTypeEnum ownerType){
        this.ownerId = ownerId;
        this.ownerType = ownerType;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public OwnerTypeEnum getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(OwnerTypeEnum ownerType) {
        this.ownerType = ownerType;
    }
}
