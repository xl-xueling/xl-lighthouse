package com.dtstep.lighthouse.insights.dto;

import java.io.Serializable;

public class ViewDataQueryParam implements Serializable {

    private String type;

    private String config;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }
}
