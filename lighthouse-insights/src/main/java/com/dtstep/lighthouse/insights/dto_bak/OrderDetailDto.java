package com.dtstep.lighthouse.insights.dto_bak;

import com.dtstep.lighthouse.common.util.BeanCopyUtil;
import com.dtstep.lighthouse.insights.modal.OrderDetail;
import com.dtstep.lighthouse.insights.modal.User;

public class OrderDetailDto extends OrderDetail {

    private User user;

    public OrderDetailDto(OrderDetail orderDetail){
        assert orderDetail != null;
        BeanCopyUtil.copy(orderDetail,this);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
