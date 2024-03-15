package com.dtstep.lighthouse.insights.dto;

import com.dtstep.lighthouse.common.enums.SwitchStateEnum;

import java.io.Serializable;

public class TrackParam implements Serializable {

    private Integer groupId;

    private Integer statId;

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getStatId() {
        return statId;
    }

    public void setStatId(Integer statId) {
        this.statId = statId;
    }
}
