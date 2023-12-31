package com.dtstep.lighthouse.insights.service;

import com.dtstep.lighthouse.insights.dto.OrderDetailDto;

import java.util.List;

public interface OrderDetailService {

    List<OrderDetailDto> queryList(Integer orderId);
}
