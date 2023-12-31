package com.dtstep.lighthouse.insights.service;

import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.insights.dto.OrderApproveParam;
import com.dtstep.lighthouse.insights.dto.OrderDto;
import com.dtstep.lighthouse.insights.dto.OrderQueryParam;
import com.dtstep.lighthouse.insights.modal.Order;

import java.util.List;

public interface OrderService {

    int create(Order order);

    int approve(OrderApproveParam approveParam);

    ListData<Order> queryApplyList(OrderQueryParam queryParam,Integer pageNum,Integer pageSize);

    ListData<OrderDto> queryApproveList(OrderQueryParam queryParam, Integer pageNum, Integer pageSize);
}
