package com.dtstep.lighthouse.insights.controller;

import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.commonv2.insights.ResultData;
import com.dtstep.lighthouse.insights.dto.ListSearchObject;
import com.dtstep.lighthouse.insights.dto.Pagination;
import com.dtstep.lighthouse.insights.dto.ProjectQueryParam;
import com.dtstep.lighthouse.insights.dto.UserQueryParam;
import com.dtstep.lighthouse.insights.modal.Department;
import com.dtstep.lighthouse.insights.modal.Project;
import com.dtstep.lighthouse.insights.modal.User;
import com.dtstep.lighthouse.insights.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public ResultData<ListData<Project>> queryList(@Validated @RequestBody ListSearchObject<ProjectQueryParam> searchObject){
        Pagination pagination = searchObject.getPagination();
        ListData<Project> listData = projectService.queryList(searchObject.getQueryParams(),pagination.getPageNum(),pagination.getPageSize());
        return ResultData.success(listData);
    }
}
