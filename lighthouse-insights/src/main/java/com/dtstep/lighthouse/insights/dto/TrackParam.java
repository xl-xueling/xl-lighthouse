package com.dtstep.lighthouse.insights.dto;

import com.dtstep.lighthouse.common.enums.SwitchStateEnum;

import java.io.Serializable;

public class TrackParam implements Serializable {

    private Integer groupId;

    private SwitchStateEnum switchState;

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public SwitchStateEnum getSwitchState() {
        return switchState;
    }

    public void setSwitchState(SwitchStateEnum switchState) {
        this.switchState = switchState;
    }
}
