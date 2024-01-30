package com.dtstep.lighthouse.insights.dto;

import java.util.List;

public class MetricSetQueryParam {

    private List<Integer> ids;

    private String search;

    private Integer ownerId;

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }
}
