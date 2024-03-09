package com.dtstep.lighthouse.common.modal;

import com.dtstep.lighthouse.common.entity.view.StatValue;

import java.util.List;

public class StatDataObject {

    private Integer statId;

    private String dimensValue;

    private List<StatValue> valuesList;

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

}
