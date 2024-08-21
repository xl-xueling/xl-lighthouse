package com.dtstep.lighthouse.insights.dto;

import java.io.Serializable;

public class ViewInfoQueryParam implements Serializable {

    private Integer id;

    private Integer version;

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
