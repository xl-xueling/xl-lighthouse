package com.dtstep.lighthouse.insights.dto;

import com.dtstep.lighthouse.common.util.BeanCopyUtil;
import com.dtstep.lighthouse.insights.modal.Group;
import com.dtstep.lighthouse.insights.modal.OrderDetail;
import com.dtstep.lighthouse.insights.modal.User;

public class OrderDetailDto extends OrderDetail {

    private UserDto user;

    public OrderDetailDto(OrderDetail orderDetail){
        assert orderDetail != null;
        BeanCopyUtil.copy(orderDetail,this);
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }
}
