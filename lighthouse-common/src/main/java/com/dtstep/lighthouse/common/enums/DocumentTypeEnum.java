package com.dtstep.lighthouse.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

public enum DocumentTypeEnum {

    DOC_DIRECTORY(1),

    DOC_MARKDOWN(2),

    ;

    @JsonValue
    private int type;

    DocumentTypeEnum(int type){
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @JsonCreator
    public static DocumentTypeEnum forValue(int type){
        DocumentTypeEnum[] values = DocumentTypeEnum.values();
        return Stream.of(values).filter(it -> it.getType() == type).findAny().orElse(null);
    }
}
