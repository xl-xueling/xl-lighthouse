package com.dtstep.lighthouse.common.modal;

import java.io.Serializable;

public class IndicatorValue implements Serializable {

    private String category;

    private String dimens;

    private Object value;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDimens() {
        return dimens;
    }

    public void setDimens(String dimens) {
        this.dimens = dimens;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
