package com.dtstep.lighthouse.insights.controller;

import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.insights.dto.*;
import com.dtstep.lighthouse.insights.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
public class ApproveController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/approve/list")
    public ResultData<ListData<OrderDto>> queryList(@Validated @RequestBody ListSearchObject<OrderQueryParam> searchObject){
        Pagination pagination = searchObject.getPagination();
        ListData<OrderDto> listData = orderService.queryApproveList(searchObject.getQueryParams(),pagination.getPageNum(),pagination.getPageSize());
        return ResultData.success(listData);
    }
}
