package com.dtstep.lighthouse.insights.controller;

import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.commonv2.constant.SystemConstant;
import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.commonv2.insights.ResultCode;
import com.dtstep.lighthouse.insights.controller.annotation.AuthPermission;
import com.dtstep.lighthouse.insights.dto.*;
import com.dtstep.lighthouse.insights.enums.RoleTypeEnum;
import com.dtstep.lighthouse.insights.modal.Group;
import com.dtstep.lighthouse.insights.modal.MetricSet;
import com.dtstep.lighthouse.insights.service.MetricSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
public class MetricSetController {

    @Autowired
    private MetricSetService metricSetService;

    @RequestMapping("/metricset/create")
    public ResultData<Integer> create(@Validated @RequestBody MetricSetCreateParam createParam) {
        System.out.println("createParam is:" + JsonUtil.toJSONString(createParam));
        MetricSet metricSet = new MetricSet();
        metricSet.setDesc(createParam.getDesc());
        metricSet.setTitle(createParam.getTitle());
        metricSet.setPrivateType(createParam.getPrivateType());
        int result = metricSetService.create(metricSet);
        if(result > 0){
            return ResultData.success(result);
        }else{
            return ResultData.result(ResultCode.systemError);
        }
    }

    @RequestMapping("/metricset/update")
    public ResultData<Integer> update(@Validated @RequestBody MetricSetCreateParam updateParam) {
        System.out.println("createParam is:" + JsonUtil.toJSONString(updateParam));
        MetricSet metricSet = new MetricSet();
        metricSet.setDesc(updateParam.getDesc());
        metricSet.setTitle(updateParam.getTitle());
        metricSet.setPrivateType(updateParam.getPrivateType());
        int result = metricSetService.create(metricSet);
        if(result > 0){
            return ResultData.success(result);
        }else{
            return ResultData.result(ResultCode.systemError);
        }
    }

    @RequestMapping("/metricset/list")
    public ResultData<ListData<MetricSet>> list(@Validated @RequestBody ListSearchObject<MetricSetQueryParam> searchObject) {
        MetricSetQueryParam queryParam = searchObject.getQueryParams();
        Pagination pagination = searchObject.getPagination();
        ListData<MetricSet> listData = metricSetService.queryList(queryParam, pagination.getPageNum(), pagination.getPageSize());
        return ResultData.success(listData);
    }
}
