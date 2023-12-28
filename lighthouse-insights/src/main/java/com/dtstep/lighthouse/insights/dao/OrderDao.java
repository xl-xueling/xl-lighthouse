package com.dtstep.lighthouse.insights.dao;

import com.dtstep.lighthouse.insights.modal.Order;

public interface OrderDao {

    int insert(Order order);

    Order queryById(Integer id);
}
