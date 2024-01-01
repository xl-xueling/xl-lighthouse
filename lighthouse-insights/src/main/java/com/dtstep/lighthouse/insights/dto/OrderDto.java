package com.dtstep.lighthouse.insights.dto;

import com.dtstep.lighthouse.common.util.BeanCopyUtil;
import com.dtstep.lighthouse.insights.modal.*;

import java.util.HashMap;
import java.util.List;

public class OrderDto extends Order {

    private UserDto user;

    private String desc;

    private HashMap<Integer,List<UserDto>> adminsMap;

    private List<OrderDetailDto> orderDetails;

    public OrderDto(Order order){
        assert order != null;
        BeanCopyUtil.copy(order,this);
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public HashMap<Integer, List<UserDto>> getAdminsMap() {
        return adminsMap;
    }

    public void setAdminsMap(HashMap<Integer, List<UserDto>> adminsMap) {
        this.adminsMap = adminsMap;
    }

    public List<OrderDetailDto> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetailDto> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
