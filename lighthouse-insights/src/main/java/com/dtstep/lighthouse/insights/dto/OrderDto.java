package com.dtstep.lighthouse.insights.dto;

import com.dtstep.lighthouse.common.util.BeanCopyUtil;
import com.dtstep.lighthouse.insights.modal.Group;
import com.dtstep.lighthouse.insights.modal.Order;
import com.dtstep.lighthouse.insights.modal.Project;
import com.dtstep.lighthouse.insights.modal.User;

import java.util.List;

public class OrderDto extends Order {

    private UserDto user;

    private List<UserDto> admins;

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

    public List<UserDto> getAdmins() {
        return admins;
    }

    public void setAdmins(List<UserDto> admins) {
        this.admins = admins;
    }
}
