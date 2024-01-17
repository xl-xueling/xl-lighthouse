package com.dtstep.lighthouse.insights.service;

import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.insights.dto_bak.OrderApproveParam;
import com.dtstep.lighthouse.insights.dto_bak.OrderVO;
import com.dtstep.lighthouse.insights.dto_bak.OrderQueryParam;
import com.dtstep.lighthouse.common.enums.OrderTypeEnum;
import com.dtstep.lighthouse.insights.modal.Order;
import com.dtstep.lighthouse.insights.modal.User;

public interface OrderService {

    <T>int submit(User applyUser, OrderTypeEnum orderTypeEnum, T param) throws Exception;

    void approve(OrderApproveParam approveParam);

    OrderVO queryById(Integer id);

    ListData<Order> queryApplyList(OrderQueryParam queryParam,Integer pageNum,Integer pageSize);

    ListData<OrderVO> queryApproveList(OrderQueryParam queryParam, Integer pageNum, Integer pageSize);
}
