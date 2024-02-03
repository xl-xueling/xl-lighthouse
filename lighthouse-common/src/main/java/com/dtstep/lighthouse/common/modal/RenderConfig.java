package com.dtstep.lighthouse.common.modal;

import java.util.List;

public class RenderConfig {

    private RenderDateConfig datepicker;

    private List<RenderFilterConfig> filters;

    public RenderDateConfig getDatepicker() {
        return datepicker;
    }

    public void setDatepicker(RenderDateConfig datepicker) {
        this.datepicker = datepicker;
    }

    public List<RenderFilterConfig> getFilters() {
        return filters;
    }

    public void setFilters(List<RenderFilterConfig> filters) {
        this.filters = filters;
    }
}
