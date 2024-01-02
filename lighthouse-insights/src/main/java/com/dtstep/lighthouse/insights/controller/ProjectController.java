package com.dtstep.lighthouse.insights.controller;

import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.commonv2.insights.ResultCode;
import com.dtstep.lighthouse.insights.dto.ResultData;
import com.dtstep.lighthouse.insights.dto.*;
import com.dtstep.lighthouse.insights.modal.Project;
import com.dtstep.lighthouse.insights.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@ControllerAdvice
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private MessageSource messageSource;

    @RequestMapping("/project/create")
    public ResultData<Integer> create(@Validated @RequestBody Project createParam) {
        int result = projectService.create(createParam);
        return ResultData.success(result);
    }

    @RequestMapping("/project/queryById")
    public ResultData<ProjectDto> queryById(@RequestBody QueryParam queryParam) {
        ProjectDto projectDto = projectService.queryById(queryParam.getId());
        return ResultData.success(projectDto);
    }

    @PostMapping("/project/list")
    public ResultData<ListData<ProjectDto>> queryList(@Validated @RequestBody ListSearchObject<ProjectQueryParam> searchObject){
        Pagination pagination = searchObject.getPagination();
        ListData<ProjectDto> listData = projectService.queryList(searchObject.getQueryParams(),pagination.getPageNum(),pagination.getPageSize());
        return ResultData.success(listData);
    }

    @RequestMapping("/project/updateById")
    public ResultData<Integer> updateById(@RequestBody Project updateParam) {
        int id = projectService.update(updateParam);
        if(id > 0){
            return ResultData.success(id);
        }else{
            return ResultData.failed(ResultCode.systemError);
        }
    }
}
