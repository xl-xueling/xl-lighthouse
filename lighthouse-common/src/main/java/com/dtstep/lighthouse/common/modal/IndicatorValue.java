package com.dtstep.lighthouse.common.modal;

import java.io.Serializable;

public class IndicatorValue implements Serializable {

    private String category;

    private String series;

    private Object value;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
