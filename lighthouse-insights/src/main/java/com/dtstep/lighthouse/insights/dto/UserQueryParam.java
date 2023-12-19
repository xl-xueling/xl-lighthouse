package com.dtstep.lighthouse.insights.dto;

import java.util.Date;

public class UserQueryParam {

    private String username;

    private Date createdStartTime;

    private Date createdEndTime;

    private int state;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getCreatedStartTime() {
        return createdStartTime;
    }

    public void setCreatedStartTime(Date createdStartTime) {
        this.createdStartTime = createdStartTime;
    }

    public Date getCreatedEndTime() {
        return createdEndTime;
    }

    public void setCreatedEndTime(Date createdEndTime) {
        this.createdEndTime = createdEndTime;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
