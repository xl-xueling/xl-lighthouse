package com.dtstep.lighthouse.insights.dao;

import com.dtstep.lighthouse.insights.dto.ApplyOrderQueryParam;
import com.dtstep.lighthouse.insights.dto.ApproveOrderQueryParam;
import com.dtstep.lighthouse.insights.modal.Order;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDao {

    int insert(Order order);

    int update(Order order);

    Order queryById(Integer id);

    boolean isExist(String hash);

    List<Order> queryApplyList(ApplyOrderQueryParam queryParam, Integer pageNum, Integer pageSize);

    List<Order> queryApproveList(ApproveOrderQueryParam queryParam, Integer pageNum, Integer pageSize);
}
