package com.dtstep.lighthouse.insights.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class IDParam implements Serializable {

    @NotNull
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
