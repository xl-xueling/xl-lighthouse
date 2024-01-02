package com.dtstep.lighthouse.insights.dto;

import com.dtstep.lighthouse.common.util.BeanCopyUtil;
import com.dtstep.lighthouse.insights.modal.*;

import java.util.HashMap;
import java.util.List;

public class OrderDto extends Order {

    private User user;

    private String desc;

    public OrderDto(Order order){
        assert order != null;
        BeanCopyUtil.copy(order,this);
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
}
