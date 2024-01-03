package com.dtstep.lighthouse.insights.controller;

import com.dtstep.lighthouse.commonv2.constant.SystemConstant;
import com.dtstep.lighthouse.commonv2.insights.ResultCode;
import com.dtstep.lighthouse.insights.dto.ResultData;
import com.dtstep.lighthouse.insights.dto.CommonTreeNode;
import com.dtstep.lighthouse.insights.dto.DeleteParam;
import com.dtstep.lighthouse.insights.modal.Department;
import com.dtstep.lighthouse.insights.service.DepartmentService;
import com.dtstep.lighthouse.insights.service.ProjectService;
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

    @Autowired
    private ProjectService projectService;

    @RequestMapping("/department/all")
    public ResultData<List<CommonTreeNode>> all() {
        List<CommonTreeNode> list = departmentService.queryAll();
        return ResultData.success(list);
    }

    @RequestMapping("/department/create")
    public ResultData<Integer> create(@Validated @RequestBody Department createParam) {
        int level = departmentService.getLevel(createParam.getPid());
        if(level >= SystemConstant.DEPARTMENT_MAX_LEVEL){
            return ResultData.failed(ResultCode.departCreateErrorLevelLimit);
        }
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
        Department department = departmentService.queryById(deleteParam.getId());
        Validate.notNull(department);
        int childSize = departmentService.countChildByPid(deleteParam.getId());
        if(childSize > 0){
            return ResultData.failed(ResultCode.departDelErrorChildExist);
        }
        int projectSize = projectService.countByDepartmentId(deleteParam.getId());
        if(projectSize > 0){
            return ResultData.failed(ResultCode.departDelErrorProjectExist);
        }
        int result = departmentService.delete(department);
        if(result == 1){
            return ResultData.success(result);
        }else{
            return ResultData.failed(ResultCode.systemError);
        }
    }

}
