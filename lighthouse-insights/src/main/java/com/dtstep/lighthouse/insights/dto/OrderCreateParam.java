package com.dtstep.lighthouse.insights.dto;

import com.dtstep.lighthouse.common.enums.OrderTypeEnum;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Map;

public class OrderCreateParam implements Serializable {

    @NotNull
    private Integer userId;

    @NotNull
    private OrderTypeEnum orderType;

    @NotNull
    private Map<String,Object> extendConfig;

    @NotEmpty
    private String reason;

    public OrderTypeEnum getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderTypeEnum orderType) {
        this.orderType = orderType;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Map<String, Object> getExtendConfig() {
        return extendConfig;
    }

    public void setExtendConfig(Map<String, Object> extendConfig) {
        this.extendConfig = extendConfig;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
