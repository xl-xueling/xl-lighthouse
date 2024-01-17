package com.dtstep.lighthouse.insights.service;

import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.insights.dto_bak.ExtendOrderDto;
import com.dtstep.lighthouse.insights.dto_bak.OrderApproveParam;
import com.dtstep.lighthouse.insights.dto_bak.OrderDto;
import com.dtstep.lighthouse.insights.dto_bak.OrderQueryParam;
import com.dtstep.lighthouse.insights.modal.Order;

public interface OrderService {

    int create(Order order);

    void approve(OrderApproveParam approveParam);

    ExtendOrderDto queryById(Integer id);

    ListData<Order> queryApplyList(OrderQueryParam queryParam,Integer pageNum,Integer pageSize);

    ListData<OrderDto> queryApproveList(OrderQueryParam queryParam, Integer pageNum, Integer pageSize);
}
