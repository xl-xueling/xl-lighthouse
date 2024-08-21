package com.dtstep.lighthouse.insights.dto;

import javax.validation.constraints.NotNull;

public class RollbackCreateParam {

    @NotNull
    private Integer id;

    @NotNull
    private Object config;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Object getConfig() {
        return config;
    }

    public void setConfig(Object config) {
        this.config = config;
    }
}
