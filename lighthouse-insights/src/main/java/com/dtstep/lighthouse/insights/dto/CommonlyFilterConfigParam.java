package com.dtstep.lighthouse.insights.dto;

import com.dtstep.lighthouse.common.modal.RenderFilterConfig;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;

public class CommonlyFilterConfigParam implements Serializable {

    private String key;

    private Integer statId;

    private LinkedHashMap<String, String[]> filters;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public LinkedHashMap<String, String[]> getFilters() {
        return filters;
    }

    public void setFilters(LinkedHashMap<String, String[]> filters) {
        this.filters = filters;
    }

    public Integer getStatId() {
        return statId;
    }

    public void setStatId(Integer statId) {
        this.statId = statId;
    }
}
