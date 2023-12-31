package com.dtstep.lighthouse.insights.dao;

import com.dtstep.lighthouse.insights.modal.OrderDetail;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailDao {

    int insert(OrderDetail orderDetail);

    List<OrderDetail> queryList();
}
