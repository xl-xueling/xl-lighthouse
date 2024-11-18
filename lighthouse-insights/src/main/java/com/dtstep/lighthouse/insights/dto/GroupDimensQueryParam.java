package com.dtstep.lighthouse.insights.dto;

import java.io.Serializable;

public class GroupDimensQueryParam implements Serializable {

    private Integer groupId;

    private String dimens;

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
}
