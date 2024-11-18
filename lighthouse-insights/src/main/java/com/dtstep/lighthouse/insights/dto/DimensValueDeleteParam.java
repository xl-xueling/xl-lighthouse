package com.dtstep.lighthouse.insights.dto;

import java.io.Serializable;

public class DimensValueDeleteParam implements Serializable {

    private Integer groupId;

    private String dimens;

    private String dimensValue;

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

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
}
