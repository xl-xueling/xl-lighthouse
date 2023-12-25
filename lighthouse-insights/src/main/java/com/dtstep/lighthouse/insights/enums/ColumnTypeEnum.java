package com.dtstep.lighthouse.insights.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ColumnTypeEnum {

    STRING("string"),

    NUMBER("number"),

    ;


    @JsonValue
    private String type;

    ColumnTypeEnum(String type){
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
