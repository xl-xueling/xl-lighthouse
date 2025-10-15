package com.dtstep.lighthouse.common.modal;

import java.util.List;

public class ViewDimensQueryParam {

    private List<Integer> statIds;

    private Integer limit;

    public List<Integer> getStatIds() {
        return statIds;
    }

    public void setStatIds(List<Integer> statIds) {
        this.statIds = statIds;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
