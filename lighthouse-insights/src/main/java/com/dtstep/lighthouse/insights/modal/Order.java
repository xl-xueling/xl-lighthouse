package com.dtstep.lighthouse.insights.modal;

import com.dtstep.lighthouse.insights.dto.PermissionInfo;
import com.dtstep.lighthouse.insights.enums.OrderStateEnum;
import com.dtstep.lighthouse.insights.enums.OrderTypeEnum;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class Order extends PermissionInfo implements Serializable {

    private Integer id;

    private Integer userId;

    private OrderTypeEnum orderType;

    private OrderStateEnum state;

    private List<Integer> steps;

    private Map<String,Object> extendConfig;

    private String hash;

    private Integer currentNode;

    private String reason;

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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public List<Integer> getSteps() {
        return steps;
    }

    public void setSteps(List<Integer> steps) {
        this.steps = steps;
    }

    public Map<String, Object> getExtendConfig() {
        return extendConfig;
    }

    public void setExtendConfig(Map<String, Object> extendConfig) {
        this.extendConfig = extendConfig;
    }

}
