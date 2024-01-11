package com.dtstep.lighthouse.insights.controller;

import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.commonv2.insights.ResultCode;
import com.dtstep.lighthouse.insights.dto.*;
import com.dtstep.lighthouse.insights.modal.Component;
import com.dtstep.lighthouse.insights.service.BaseService;
import com.dtstep.lighthouse.insights.service.ComponentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@ControllerAdvice
public class ComponentController {

    @Autowired
    private ComponentService componentService;

    @Autowired
    private BaseService baseService;

    @RequestMapping("/component/verify")
    public ResultData<List<CommonTreeNode>> verify(@Validated @RequestBody ComponentVerifyParam verifyParam) {
        System.out.println("verifyParam:" + JsonUtil.toJSONString(verifyParam));
        ResultCode resultCode = componentService.verify(verifyParam.getConfiguration());
        return ResultData.result(resultCode);
    }

    @RequestMapping("/component/create")
    public ResultData<Integer> create(@Validated @RequestBody ComponentCreateParam createParam) {
        System.out.println("createParam:" + JsonUtil.toJSONString(createParam));
        Component component = new Component();
        component.setComponentType(createParam.getComponentType());
        component.setPrivateType(createParam.getPrivateType());
        component.setConfiguration(createParam.getConfiguration());
        component.setTitle(createParam.getTitle());
        Integer userId = baseService.getCurrentUserId();
        component.setUserId(userId);
        ResultCode resultCode = componentService.create(component);
        return ResultData.result(resultCode);
    }

    @PostMapping("/component/list")
    public ResultData<ListData<ComponentDto>> queryList(@Validated @RequestBody ListSearchObject<ComponentQueryParam> searchObject){
        Pagination pagination = searchObject.getPagination();
        ListData<ComponentDto> listData = componentService.queryList(searchObject.getQueryParams(),pagination.getPageNum(),pagination.getPageSize());
        return ResultData.success(listData);
    }

}
