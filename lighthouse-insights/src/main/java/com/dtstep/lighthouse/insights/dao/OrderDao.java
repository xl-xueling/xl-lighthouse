package com.dtstep.lighthouse.insights.dao;

import com.dtstep.lighthouse.insights.dto.OrderQueryParam;
import com.dtstep.lighthouse.insights.modal.Order;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDao {

    int insert(Order order);

    Order queryById(Integer id);

    List<Order> queryApplyList(OrderQueryParam queryParam, Integer pageNum, Integer pageSize);

    List<Order> queryApproveList(OrderQueryParam queryParam, Integer pageNum, Integer pageSize);
}
