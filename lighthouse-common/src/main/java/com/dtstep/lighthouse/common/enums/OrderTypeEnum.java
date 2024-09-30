package com.dtstep.lighthouse.common.enums;

import com.dtstep.lighthouse.common.entity.FlowNode;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.List;
import java.util.stream.Stream;

public enum OrderTypeEnum {

    PROJECT_ACCESS(1,List.of(FlowNode.newBuilder(RoleTypeEnum.PROJECT_MANAGE_PERMISSION))),

    STAT_ACCESS(2,List.of(FlowNode.newBuilder(RoleTypeEnum.PROJECT_MANAGE_PERMISSION))),

    METRIC_ACCESS(3,List.of(FlowNode.newBuilder(RoleTypeEnum.METRIC_MANAGE_PERMISSION))),

    LIMITING_SETTINGS(4,List.of(FlowNode.newBuilder(RoleTypeEnum.OPT_MANAGE_PERMISSION))),

    STAT_PEND_APPROVE(5,List.of(FlowNode.newBuilder(RoleTypeEnum.OPT_MANAGE_PERMISSION))),

    USER_PEND_APPROVE(6,List.of(FlowNode.newBuilder(RoleTypeEnum.OPT_MANAGE_PERMISSION))),

    VIEW_ACCESS(7,List.of(FlowNode.newBuilder(RoleTypeEnum.VIEW_MANAGE_PERMISSION))),

    CALLER_PROJECT_ACCESS(8,List.of(FlowNode.newBuilder(RoleTypeEnum.PROJECT_MANAGE_PERMISSION))),

    CALLER_STAT_ACCESS(9,List.of(FlowNode.newBuilder(RoleTypeEnum.PROJECT_MANAGE_PERMISSION))),

    CALLER_VIEW_ACCESS(10,List.of(FlowNode.newBuilder(RoleTypeEnum.VIEW_MANAGE_PERMISSION))),

    CALLER_PROJECT_ACCESS_EXTENSION(11,List.of(FlowNode.newBuilder(RoleTypeEnum.PROJECT_MANAGE_PERMISSION))),

    CALLER_STAT_ACCESS_EXTENSION(12,List.of(FlowNode.newBuilder(RoleTypeEnum.PROJECT_MANAGE_PERMISSION))),

    CALLER_VIEW_ACCESS_EXTENSION(13,List.of(FlowNode.newBuilder(RoleTypeEnum.VIEW_MANAGE_PERMISSION))),


    ;

    OrderTypeEnum(int orderType){
        this.orderType = orderType;
    }

    OrderTypeEnum(int orderType,List<FlowNode> workFlow)
    {
        this.orderType = orderType;
        this.defaultWorkFlow = workFlow;
    }

    @JsonValue
    private int orderType;

    private List<FlowNode> defaultWorkFlow;

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    @JsonCreator
    public static OrderTypeEnum forValue(int value){
        OrderTypeEnum[] values = OrderTypeEnum.values();
        return Stream.of(values).filter(it -> it.getOrderType() == value).findAny().orElse(null);
    }

    public List<FlowNode> getDefaultWorkFlow() {
        return defaultWorkFlow;
    }

    public void setDefaultWorkFlow(List<FlowNode> defaultWorkFlow) {
        this.defaultWorkFlow = defaultWorkFlow;
    }
}
