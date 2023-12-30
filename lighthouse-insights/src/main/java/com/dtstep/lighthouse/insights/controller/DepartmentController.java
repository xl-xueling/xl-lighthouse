package com.dtstep.lighthouse.insights.controller;

import com.dtstep.lighthouse.commonv2.insights.ResultCode;
import com.dtstep.lighthouse.insights.dto.ResultData;
import com.dtstep.lighthouse.insights.dto.CommonTreeNode;
import com.dtstep.lighthouse.insights.dto.DeleteParam;
import com.dtstep.lighthouse.insights.modal.Department;
import com.dtstep.lighthouse.insights.service.DepartmentService;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@ControllerAdvice
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @RequestMapping("/department/all")
    public ResultData<List<CommonTreeNode>> all() {
        List<CommonTreeNode> list = departmentService.queryAll();
        return ResultData.success(list);
    }

    @RequestMapping("/department/create")
    public ResultData<Integer> create(@Validated @RequestBody Department createParam) {
        int result = departmentService.create(createParam);
        return ResultData.success(result);
    }

    @RequestMapping("/department/updateById")
    public ResultData<Integer> updateById(@RequestBody Department updateParam) {
        int id = departmentService.update(updateParam);
        if(id > 0){
            return ResultData.success(id);
        }else{
            return ResultData.failed(ResultCode.systemError);
        }
    }

    @RequestMapping("/department/delete")
    public ResultData<Integer> delete(@RequestBody DeleteParam deleteParam) {
        Validate.notNull(deleteParam.getId());
        int result = departmentService.deleteById(List.of(deleteParam.getId()));
        if(result == 1){
            return ResultData.success(result);
        }else{
            return ResultData.failed(ResultCode.systemError);
        }
    }

}
