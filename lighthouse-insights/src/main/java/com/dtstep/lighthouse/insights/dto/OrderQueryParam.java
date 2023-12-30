package com.dtstep.lighthouse.insights.dto;

import com.dtstep.lighthouse.insights.enums.OrderStateEnum;
import com.dtstep.lighthouse.insights.enums.OrderTypeEnum;

import java.time.LocalDateTime;

public class OrderQueryParam {

    private Integer applyUserId;

    private OrderTypeEnum orderType;

    private OrderStateEnum orderState;

    private LocalDateTime createStartTime;

    private LocalDateTime createEndTime;

    private Integer approveUserId;

    public Integer getApplyUserId() {
        return applyUserId;
    }

    public void setApplyUserId(Integer applyUserId) {
        this.applyUserId = applyUserId;
    }

    public Integer getApproveUserId() {
        return approveUserId;
    }

    public void setApproveUserId(Integer approveUserId) {
        this.approveUserId = approveUserId;
    }

    public OrderTypeEnum getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderTypeEnum orderType) {
        this.orderType = orderType;
    }

    public OrderStateEnum getOrderState() {
        return orderState;
    }

    public void setOrderState(OrderStateEnum orderState) {
        this.orderState = orderState;
    }

    public LocalDateTime getCreateStartTime() {
        return createStartTime;
    }

    public void setCreateStartTime(LocalDateTime createStartTime) {
        this.createStartTime = createStartTime;
    }

    public LocalDateTime getCreateEndTime() {
        return createEndTime;
    }

    public void setCreateEndTime(LocalDateTime createEndTime) {
        this.createEndTime = createEndTime;
    }
}
