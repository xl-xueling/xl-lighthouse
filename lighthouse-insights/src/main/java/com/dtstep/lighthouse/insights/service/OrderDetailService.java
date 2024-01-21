package com.dtstep.lighthouse.insights.service;

import com.dtstep.lighthouse.insights.vo.OrderDetailVO;

import java.util.List;

public interface OrderDetailService {

    List<OrderDetailVO> queryList(Integer orderId);
}
