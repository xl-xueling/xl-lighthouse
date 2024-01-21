package com.dtstep.lighthouse.insights.controller;

import com.dtstep.lighthouse.insights.dto.OrderProcessParam;
import com.dtstep.lighthouse.insights.dto.QueryParam;
import com.dtstep.lighthouse.insights.dto_bak.*;
import com.dtstep.lighthouse.insights.service.OrderService;
import com.dtstep.lighthouse.insights.vo.OrderVO;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/order/process")
    public ResultData<Integer> process(@Validated @RequestBody OrderProcessParam approveParam){
        int result = orderService.process(approveParam);
        return ResultData.success(result);
    }
}
