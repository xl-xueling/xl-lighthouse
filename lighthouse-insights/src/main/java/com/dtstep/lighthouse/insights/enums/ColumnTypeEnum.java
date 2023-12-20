package com.dtstep.lighthouse.insights.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ColumnTypeEnum {

    STRING(1,"string"),

    NUMBERIC(2,"numberic"),

    ;

    private int typeId;

    @JsonValue
    private String typeName;

    ColumnTypeEnum(int typeId,String typeName){
        this.typeId = typeId;
        this.typeName = typeName;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
