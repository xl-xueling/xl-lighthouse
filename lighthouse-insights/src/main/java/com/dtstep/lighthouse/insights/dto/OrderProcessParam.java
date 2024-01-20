package com.dtstep.lighthouse.insights.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class OrderProcessParam implements Serializable {

    @NotNull
    private Integer id;

    @NotNull
    private Integer state;

    private String reply;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
