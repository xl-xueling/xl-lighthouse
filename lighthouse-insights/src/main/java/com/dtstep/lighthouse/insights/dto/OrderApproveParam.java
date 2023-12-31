package com.dtstep.lighthouse.insights.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class OrderApproveParam implements Serializable {

    @NotNull
    private Integer id;

    @NotNull
    private Integer roleId;

    private String reply;

    @NotNull
    private Integer result;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }
}
