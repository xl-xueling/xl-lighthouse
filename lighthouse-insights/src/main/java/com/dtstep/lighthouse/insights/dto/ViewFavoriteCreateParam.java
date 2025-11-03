package com.dtstep.lighthouse.insights.dto;

import java.io.Serializable;

public class ViewFavoriteCreateParam implements Serializable {

    private String name;

    private Integer cateId;

    private Object config;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCateId() {
        return cateId;
    }

    public void setCateId(Integer cateId) {
        this.cateId = cateId;
    }

    public Object getConfig() {
        return config;
    }

    public void setConfig(Object config) {
        this.config = config;
    }
}
