package com.dtstep.lighthouse.insights.controller;

import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.commonv2.insights.ResultCode;
import com.dtstep.lighthouse.insights.dto_bak.*;
import com.dtstep.lighthouse.insights.modal.Component;
import com.dtstep.lighthouse.insights.service.BaseService;
import com.dtstep.lighthouse.insights.service.ComponentService;
import org.apache.commons.lang3.Validate;
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
        ResultCode resultCode = componentService.verify(createParam.getConfiguration());
        if(resultCode != ResultCode.success){
            return ResultData.result(resultCode);
        }
        Component component = new Component();
        component.setComponentType(createParam.getComponentType());
        component.setPrivateType(createParam.getPrivateType());
        component.setConfiguration(JsonUtil.toJavaObjectList(createParam.getConfiguration(),TreeNode.class));
        component.setTitle(createParam.getTitle());
        Integer userId = baseService.getCurrentUserId();
        component.setUserId(userId);
        int result = componentService.create(component);
        if(result > 0){
            return ResultData.success();
        }else{
            return ResultData.result(ResultCode.systemError);
        }
    }

    @RequestMapping("/component/update")
    public ResultData<Integer> update(@Validated @RequestBody ComponentUpdateParam createParam) {
        ResultCode resultCode = componentService.verify(createParam.getConfiguration());
        if(resultCode != ResultCode.success){
            return ResultData.result(resultCode);
        }
        Component component = new Component();
        component.setId(createParam.getId());
        component.setComponentType(createParam.getComponentType());
        component.setPrivateType(createParam.getPrivateType());
        component.setConfiguration(JsonUtil.toJavaObjectList(createParam.getConfiguration(),TreeNode.class));
        component.setTitle(createParam.getTitle());
        Integer userId = baseService.getCurrentUserId();
        component.setUserId(userId);
        int result = componentService.update(component);
        if(result > 0){
            return ResultData.success();
        }else{
            return ResultData.result(ResultCode.systemError);
        }
    }


    @RequestMapping("/component/deleteById")
    public ResultData<Integer> deleteById(@Validated @RequestBody IDParam idParam) {
        Integer id = idParam.getId();
        Component component = componentService.queryById(id);
        Validate.notNull(component);
        int result = componentService.delete(component);
        if(result > 0){
            return ResultData.success();
        }else{
            return ResultData.result(ResultCode.systemError);
        }
    }

    @PostMapping("/component/list")
    public ResultData<ListData<ComponentDto>> queryList(@Validated @RequestBody ListSearchObject<ComponentQueryParam> searchObject){
        Pagination pagination = searchObject.getPagination();
        ListData<ComponentDto> listData = componentService.queryList(searchObject.getQueryParams(),pagination.getPageNum(),pagination.getPageSize());
        return ResultData.success(listData);
    }

}
