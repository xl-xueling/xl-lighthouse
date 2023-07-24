package com.dtstep.lighthouse.web.service.order;

import com.dtstep.lighthouse.common.entity.list.ListViewDataObject;
import com.dtstep.lighthouse.common.entity.order.OrderEntity;
import com.dtstep.lighthouse.common.entity.user.UserEntity;

public interface ApplyService {

    void createOrder(OrderEntity orderEntity) throws Exception;

    ListViewDataObject queryListByPage(UserEntity currentUser, int page, int state) throws Exception;

    OrderEntity queryById(int orderId) throws Exception;

    void retract(int orderId) throws Exception;
}
