package com.dtstep.lighthouse.insights.dto;

import com.dtstep.lighthouse.common.enums.user.UserStateEnum;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class ChangeUserStateParam implements Serializable {

    @NotNull
    private Integer id;

    @NotNull
    private UserStateEnum state;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserStateEnum getState() {
        return state;
    }

    public void setState(UserStateEnum state) {
        this.state = state;
    }
}
