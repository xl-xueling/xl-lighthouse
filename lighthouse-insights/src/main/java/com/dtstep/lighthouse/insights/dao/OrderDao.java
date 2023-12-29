package com.dtstep.lighthouse.insights.dao;

import com.dtstep.lighthouse.insights.dto.OrderQueryParam;
import com.dtstep.lighthouse.insights.modal.Order;

import java.util.List;

public interface OrderDao {

    int insert(Order order);

    Order queryById(Integer id);

    List<Order> queryApplyList(OrderQueryParam queryParam, Integer pageNum, Integer pageSize);

    List<Order> queryApproveList(OrderQueryParam queryParam, Integer pageNum, Integer pageSize);
}
