package com.dtstep.lighthouse.common.modal;

import java.util.List;

public class StatDataObject {

    private Integer statId;

    private String dimens;

    private String dimensValue;

    private String displayDimensValue;

    private List<StatValue> valuesList;

    public String getDimens() {
        return dimens;
    }

    public void setDimens(String dimens) {
        this.dimens = dimens;
    }

    public String getDimensValue() {
        return dimensValue;
    }

    public void setDimensValue(String dimensValue) {
        this.dimensValue = dimensValue;
    }

    public Integer getStatId() {
        return statId;
    }

    public void setStatId(Integer statId) {
        this.statId = statId;
    }

    public List<StatValue> getValuesList() {
        return valuesList;
    }

    public void setValuesList(List<StatValue> valuesList) {
        this.valuesList = valuesList;
    }

    public String getDisplayDimensValue() {
        return displayDimensValue;
    }

    public void setDisplayDimensValue(String displayDimensValue) {
        this.displayDimensValue = displayDimensValue;
    }
}
