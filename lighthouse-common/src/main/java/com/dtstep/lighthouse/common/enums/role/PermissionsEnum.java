package com.dtstep.lighthouse.common.enums.role;

import com.dtstep.lighthouse.common.entity.annotation.EnumSerializerAnnotation;

public enum PermissionsEnum {

    NONE(0),
    USER(1),
    ADMIN(2),

    ;

    PermissionsEnum(int type){
        this.type = type;
    }

    @EnumSerializerAnnotation
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
