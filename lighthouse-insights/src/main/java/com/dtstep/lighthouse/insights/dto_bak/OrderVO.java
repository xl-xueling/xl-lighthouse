package com.dtstep.lighthouse.insights.dto_bak;

import com.dtstep.lighthouse.common.util.BeanCopyUtil;
import com.dtstep.lighthouse.insights.modal.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OrderVO extends Order {

    private User user;

    private String desc;

    private HashMap<Integer, List<User>> adminsMap;

    private List<OrderDetailDto> orderDetails;

    private Object extend;

    public OrderVO(Order order){
        assert order != null;
        BeanCopyUtil.copy(order,this);
    }

    private Set<PermissionEnum> permissions = new HashSet<>();

    public Set<PermissionEnum> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<PermissionEnum> permissions) {
        this.permissions = permissions;
    }

    public void addPermission(PermissionEnum permission){
        if(permission != null){
            permissions.add(permission);
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public HashMap<Integer, List<User>> getAdminsMap() {
        return adminsMap;
    }

    public void setAdminsMap(HashMap<Integer, List<User>> adminsMap) {
        this.adminsMap = adminsMap;
    }

    public List<OrderDetailDto> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetailDto> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public Object getExtend() {
        return extend;
    }

    public void setExtend(Object extend) {
        this.extend = extend;
    }
}
