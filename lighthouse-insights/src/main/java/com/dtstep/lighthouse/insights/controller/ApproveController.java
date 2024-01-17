package com.dtstep.lighthouse.insights.controller;

import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.insights.dto_bak.*;
import com.dtstep.lighthouse.insights.service.BaseService;
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

    @Autowired
    private BaseService baseService;

    @PostMapping("/approve/list")
    public ResultData<ListData<OrderVO>> queryList(@Validated @RequestBody ListSearchObject<OrderQueryParam> searchObject){
        OrderQueryParam queryParam = searchObject.getQueryParams();
        if(queryParam == null){
            queryParam = new OrderQueryParam();
        }
        Pagination pagination = searchObject.getPagination();
        ListData<OrderVO> listData = orderService.queryApproveList(queryParam,pagination.getPageNum(),pagination.getPageSize());
        return ResultData.success(listData);
    }

    @PostMapping("/approve/process")
    public ResultData<Integer> process(@Validated @RequestBody OrderProcessParam approveParam){
        int result = orderService.process(approveParam);
        return ResultData.success(result);
    }
}
