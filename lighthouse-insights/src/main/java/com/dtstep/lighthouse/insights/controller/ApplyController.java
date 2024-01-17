package com.dtstep.lighthouse.insights.controller;

import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.insights.dto_bak.ResultData;
import com.dtstep.lighthouse.insights.modal.Order;
import com.dtstep.lighthouse.insights.service.BaseService;
import com.dtstep.lighthouse.insights.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
public class ApplyController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private BaseService baseService;

    @RequestMapping("/apply/create")
    public ResultData<Integer> create(@Validated @RequestBody Order createParam) throws Exception {
        System.out.println("order create...");
        int userId = baseService.getCurrentUserId();
        createParam.setUserId(userId);
//        orderService.create(createParam);
        return ResultData.success(null);
    }

    @RequestMapping("/apply/list")
    public ResultData<ListData<Order>> list(@Validated @RequestBody Order createParam) {
        System.out.println("order create...");
//        orderService.create(createParam);
        return ResultData.success(null);
    }
}
