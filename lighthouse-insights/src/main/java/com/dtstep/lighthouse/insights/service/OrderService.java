package com.dtstep.lighthouse.insights.service;

import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.insights.dto.OrderQueryParam;
import com.dtstep.lighthouse.insights.modal.Order;

import java.util.List;

public interface OrderService {

    int create(Order order);

    ListData<Order> queryApplyList(OrderQueryParam queryParam,Integer pageNum,Integer pageSize);

    ListData<Order> queryApproveList(OrderQueryParam queryParam,Integer pageNum,Integer pageSize);
}
