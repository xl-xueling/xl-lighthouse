package com.dtstep.lighthouse.insights.dto;

import com.dtstep.lighthouse.common.enums.stat.StatStateEnum;
import com.dtstep.lighthouse.common.enums.user.UserStateEnum;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class ChangeStatStateParam implements Serializable {

    @NotNull
    private Integer id;

    @NotNull
    private StatStateEnum state;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public StatStateEnum getState() {
        return state;
    }

    public void setState(StatStateEnum state) {
        this.state = state;
    }
}
