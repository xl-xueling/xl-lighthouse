package com.dtstep.lighthouse.insights.controller;

import com.dtstep.lighthouse.insights.dto.QueryParam;
import com.dtstep.lighthouse.insights.dto_bak.*;
import com.dtstep.lighthouse.insights.service.OrderService;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
public class OrderController {

    @Autowired
    private OrderService orderService;

    @RequestMapping("/order/queryById")
    public ResultData<OrderVO> queryById(@Validated @RequestBody QueryParam queryParam) {
        OrderVO orderVO = orderService.queryById(queryParam.getId());
        Validate.notNull(orderVO);
        Object extend = orderService.queryRelatedElement(orderVO);
        orderVO.setExtend(extend);
        return ResultData.success(orderVO);
    }
}
