package com.dtstep.lighthouse.insights.dto_bak;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class OrderProcessParam implements Serializable {

    @NotNull
    private Integer id;

    @NotNull
    private Integer roleId;

    @NotNull
    private Integer result;

    private String reply;

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
