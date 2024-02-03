package com.dtstep.lighthouse.insights.vo;

import com.dtstep.lighthouse.common.util.BeanCopyUtil;
import com.dtstep.lighthouse.common.modal.OrderDetail;
import com.dtstep.lighthouse.common.modal.User;

public class OrderDetailVO extends OrderDetail {

    private User user;

    public OrderDetailVO(OrderDetail orderDetail){
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
