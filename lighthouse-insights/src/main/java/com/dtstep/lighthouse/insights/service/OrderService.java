package com.dtstep.lighthouse.insights.service;

import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.commonv2.insights.ResultCode;
import com.dtstep.lighthouse.insights.dto.OrderProcessParam;
import com.dtstep.lighthouse.insights.dto_bak.OrderVO;
import com.dtstep.lighthouse.insights.dto.OrderQueryParam;
import com.dtstep.lighthouse.common.enums.OrderTypeEnum;
import com.dtstep.lighthouse.insights.modal.Order;
import com.dtstep.lighthouse.insights.modal.User;

import java.util.HashMap;
import java.util.Map;

public interface OrderService {

    ResultCode submit(User applyUser, OrderTypeEnum orderTypeEnum, String reason, Map<String,Object> extendConfig) throws Exception;

    int process(OrderProcessParam processParam);

    OrderVO queryById(Integer id);

    ListData<Order> queryApplyList(OrderQueryParam queryParam,Integer pageNum,Integer pageSize);

    ListData<OrderVO> queryApproveList(OrderQueryParam queryParam, Integer pageNum, Integer pageSize);
}
