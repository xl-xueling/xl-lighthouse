package com.dtstep.lighthouse.insights.dto;

import com.dtstep.lighthouse.insights.modal.Order;
import com.dtstep.lighthouse.insights.modal.User;

import java.util.HashMap;
import java.util.List;

public class ExtendOrderDto extends OrderDto {

    private HashMap<Integer, List<User>> adminsMap;

    private List<OrderDetailDto> orderDetails;

    public ExtendOrderDto(Order order) {
        super(order);
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
}
