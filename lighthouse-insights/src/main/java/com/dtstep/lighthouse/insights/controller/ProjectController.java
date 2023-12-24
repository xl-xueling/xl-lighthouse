package com.dtstep.lighthouse.insights.controller;

import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.commonv2.insights.ResultData;
import com.dtstep.lighthouse.insights.dto.*;
import com.dtstep.lighthouse.insights.modal.Department;
import com.dtstep.lighthouse.insights.modal.Project;
import com.dtstep.lighthouse.insights.modal.User;
import com.dtstep.lighthouse.insights.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@ControllerAdvice
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @RequestMapping("/project/create")
    public ResultData<Integer> create(@Validated @RequestBody Project createParam) {
        int result = projectService.create(createParam);
        return ResultData.success(result);
    }

    @PostMapping("/project/list")
    public ResultData<ListData<ProjectDto>> queryList(@Validated @RequestBody ListSearchObject<ProjectQueryParam> searchObject){
        Pagination pagination = searchObject.getPagination();
        ListData<ProjectDto> listData = projectService.queryList(searchObject.getQueryParams(),pagination.getPageNum(),pagination.getPageSize());
        return ResultData.success(listData);
    }
}
