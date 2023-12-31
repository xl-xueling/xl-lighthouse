package com.dtstep.lighthouse.insights.modal;

import com.dtstep.lighthouse.insights.enums.ApproveStateEnum;

import java.io.Serializable;
import java.time.LocalDateTime;

public class OrderDetail implements Serializable {

    private Integer id;

    private Integer orderId;

    private ApproveStateEnum state;

    private Integer roleId;

    private String reply;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public ApproveStateEnum getState() {
        return state;
    }

    public void setState(ApproveStateEnum state) {
        this.state = state;
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

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}
