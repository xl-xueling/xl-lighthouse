package com.dtstep.lighthouse.common.modal;


import com.dtstep.lighthouse.common.enums.ColumnTypeEnum;

import java.io.Serializable;

public class Column implements Serializable {

    private String name;

    private ColumnTypeEnum type;

    private String comment;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ColumnTypeEnum getType() {
        return type;
    }

    public void setType(ColumnTypeEnum type) {
        this.type = type;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
