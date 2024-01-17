package com.dtstep.lighthouse.insights.dto_bak;

import com.dtstep.lighthouse.insights.modal.RenderFilterConfig;

import java.io.Serializable;
import java.util.List;

public class StatFilterConfigParam implements Serializable {

    private Integer id;

    private List<RenderFilterConfig> filters;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<RenderFilterConfig> getFilters() {
        return filters;
    }

    public void setFilters(List<RenderFilterConfig> filters) {
        this.filters = filters;
    }
}
