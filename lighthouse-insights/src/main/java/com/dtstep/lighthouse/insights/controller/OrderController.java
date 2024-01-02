package com.dtstep.lighthouse.insights.controller;

import com.dtstep.lighthouse.insights.dto.*;
import com.dtstep.lighthouse.insights.service.OrderService;
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
    public ResultData<ExtendOrderDto> queryById(@Validated @RequestBody QueryParam queryParam) {
        ExtendOrderDto extendOrderDto = orderService.queryById(queryParam.getId());
        return ResultData.success(extendOrderDto);
    }
}
