package com.dtstep.lighthouse.insights.controller;

import com.dtstep.lighthouse.common.modal.ListSearchObject;
import com.dtstep.lighthouse.common.modal.Pagination;
import com.dtstep.lighthouse.insights.vo.ResultData;
import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.insights.dto.ApproveOrderQueryParam;
import com.dtstep.lighthouse.insights.service.BaseService;
import com.dtstep.lighthouse.insights.service.OrderService;
import com.dtstep.lighthouse.insights.vo.OrderVO;
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
    public ResultData<ListData<OrderVO>> queryList(@Validated @RequestBody ListSearchObject<ApproveOrderQueryParam> searchObject){
        ApproveOrderQueryParam queryParam = searchObject.getQueryParamOrDefault(new ApproveOrderQueryParam());
        Pagination pagination = searchObject.getPagination();
        ListData<OrderVO> listData = orderService.queryApproveList(queryParam,pagination.getPageNum(),pagination.getPageSize());
        return ResultData.success(listData);
    }
}
