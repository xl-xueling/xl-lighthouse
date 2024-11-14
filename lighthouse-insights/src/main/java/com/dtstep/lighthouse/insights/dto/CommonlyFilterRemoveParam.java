package com.dtstep.lighthouse.insights.dto;

import java.io.Serializable;

public class CommonlyFilterRemoveParam implements Serializable {

    private Integer statId;

    private String key;

    public Integer getStatId() {
        return statId;
    }

    public void setStatId(Integer statId) {
        this.statId = statId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
