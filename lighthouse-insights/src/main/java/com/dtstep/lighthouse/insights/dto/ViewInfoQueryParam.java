package com.dtstep.lighthouse.insights.dto;

import java.io.Serializable;

public class ViewInfoQueryParam implements Serializable {

    private String from = "preview";

    private Integer id;

    private Integer version;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
