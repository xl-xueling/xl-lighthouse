package com.dtstep.lighthouse.insights.dto;

import java.io.Serializable;

public class ViewDataQueryParam implements Serializable {

    private String source;

    private String config;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }
}
