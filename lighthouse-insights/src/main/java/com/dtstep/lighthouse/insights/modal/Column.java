package com.dtstep.lighthouse.insights.modal;

import com.dtstep.lighthouse.insights.enums.ColumnTypeEnum;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.Serializable;

public class Column implements Serializable {

    private String name;

//    @JsonDeserialize(using = EnumTypeDeserializer.class)
//    private ColumnTypeEnum type;

    private String type;

    private String comment;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
