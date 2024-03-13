package com.dtstep.lighthouse.insights.service;

import com.dtstep.lighthouse.common.entity.ListData;
import com.dtstep.lighthouse.common.entity.ResultCode;
import com.dtstep.lighthouse.insights.dto.ApplyOrderQueryParam;
import com.dtstep.lighthouse.insights.dto.OrderProcessParam;
import com.dtstep.lighthouse.insights.vo.OrderVO;
import com.dtstep.lighthouse.insights.dto.ApproveOrderQueryParam;
import com.dtstep.lighthouse.common.enums.OrderTypeEnum;
import com.dtstep.lighthouse.common.modal.Order;
import com.dtstep.lighthouse.common.modal.User;

import java.util.Map;

public interface OrderService {

    ResultCode submit(User applyUser, OrderTypeEnum orderTypeEnum, String reason, Map<String,Object> extendConfig) throws Exception;

    int process(OrderProcessParam processParam) throws Exception;

    int pendCount(int userId);

    OrderVO queryById(Integer id) throws Exception;

    Object queryRelatedElement(Order order) throws Exception;

    ListData<OrderVO> queryApplyList(ApplyOrderQueryParam queryParam, Integer pageNum, Integer pageSize);

    ListData<OrderVO> queryApproveList(ApproveOrderQueryParam queryParam, Integer pageNum, Integer pageSize);
}
