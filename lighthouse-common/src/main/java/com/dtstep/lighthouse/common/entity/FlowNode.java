package com.dtstep.lighthouse.common.entity;

import com.dtstep.lighthouse.common.enums.RoleTypeEnum;

public class FlowNode {

    private RoleTypeEnum roleTypeEnum;

    private Extend extend;

    public RoleTypeEnum getRoleTypeEnum() {
        return roleTypeEnum;
    }

    public void setRoleTypeEnum(RoleTypeEnum roleTypeEnum) {
        this.roleTypeEnum = roleTypeEnum;
    }

    public Extend getExtend() {
        return extend;
    }

    public void setExtend(Extend extend) {
        this.extend = extend;
    }

    public static class Extend {

        private boolean itemNear = true;

        private Object param;

        public boolean isItemNear() {
            return itemNear;
        }

        public void setItemNear(boolean itemNear) {
            this.itemNear = itemNear;
        }

        public Object getParam() {
            return param;
        }

        public void setParam(Object param) {
            this.param = param;
        }
    }

    public static FlowNode newBuilder(RoleTypeEnum roleTypeEnum){
        FlowNode flowNode = new FlowNode();
        flowNode.setRoleTypeEnum(roleTypeEnum);
        return flowNode;
    }

    public static FlowNode newBuilder(RoleTypeEnum roleTypeEnum, Extend extend){
        FlowNode flowNode = new FlowNode();
        flowNode.setExtend(extend);
        flowNode.setRoleTypeEnum(roleTypeEnum);
        return flowNode;
    }

}
