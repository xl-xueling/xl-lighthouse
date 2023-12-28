package com.dtstep.lighthouse.insights.modal;

import com.dtstep.lighthouse.insights.enums.OrderStateEnum;
import com.dtstep.lighthouse.insights.enums.OrderTypeEnum;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Order implements Serializable {

    private Integer id;

    private Integer userId;

    private OrderTypeEnum orderType;

    private OrderStateEnum state;

    private OrderExtendConfig extendConfig;

    private String hash;

    private Integer currentNode;

    private String desc;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public OrderStateEnum getState() {
        return state;
    }

    public void setState(OrderStateEnum state) {
        this.state = state;
    }

    public OrderExtendConfig getExtendConfig() {
        return extendConfig;
    }

    public void setExtendConfig(OrderExtendConfig extendConfig) {
        this.extendConfig = extendConfig;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
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

    public Integer getCurrentNode() {
        return currentNode;
    }

    public void setCurrentNode(Integer currentNode) {
        this.currentNode = currentNode;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public OrderTypeEnum getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderTypeEnum orderType) {
        this.orderType = orderType;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
