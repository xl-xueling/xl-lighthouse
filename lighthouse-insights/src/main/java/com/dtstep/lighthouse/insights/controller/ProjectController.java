package com.dtstep.lighthouse.insights.controller;

import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.commonv2.insights.ResultCode;
import com.dtstep.lighthouse.insights.controller.annotation.AuthPermission;
import com.dtstep.lighthouse.insights.dto.ResultData;
import com.dtstep.lighthouse.insights.dto.*;
import com.dtstep.lighthouse.insights.enums.RoleTypeEnum;
import com.dtstep.lighthouse.insights.modal.Project;
import com.dtstep.lighthouse.insights.service.GroupService;
import com.dtstep.lighthouse.insights.service.ProjectService;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@ControllerAdvice
public class ProjectController {

    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);

    @Autowired
    private ProjectService projectService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private GroupService groupService;

    @RequestMapping("/project/create")
    public ResultData<Integer> create(@Validated @RequestBody ProjectCreateParam createParam) {
        int result = projectService.create(createParam);
        return ResultData.success(result);
    }

    @RequestMapping("/project/queryById")
    public ResultData<ProjectExtendDto> queryById(@RequestBody QueryParam queryParam) throws Exception{
        ProjectDto projectDto = projectService.queryById(queryParam.getId());
        ProjectExtendDto projectExtendDto = new ProjectExtendDto(projectDto);
        List<TreeNode> structure = projectService.getStructure(projectDto);
        System.out.println("structure:" + JsonUtil.toJSONString(structure));
        projectExtendDto.setStructure(structure);
        return ResultData.success(projectExtendDto);
    }

    @PostMapping("/project/list")
    public ResultData<ListData<ProjectDto>> queryList(@Validated @RequestBody ListSearchObject<ProjectQueryParam> searchObject){
        Pagination pagination = searchObject.getPagination();
        ListData<ProjectDto> listData = projectService.queryList(searchObject.getQueryParams(),pagination.getPageNum(),pagination.getPageSize());
        return ResultData.success(listData);
    }

    @AuthPermission(roleTypeEnum = RoleTypeEnum.PROJECT_MANAGE_PERMISSION,relationParam = "id")
    @RequestMapping("/project/updateById")
    public ResultData<Integer> updateById(@Validated @RequestBody Project updateParam) {
        int id = projectService.update(updateParam);
        if(id > 0){
            return ResultData.success(id);
        }else{
            return ResultData.result(ResultCode.systemError);
        }
    }

    @AuthPermission(roleTypeEnum = RoleTypeEnum.PROJECT_MANAGE_PERMISSION,relationParam = "id")
    @RequestMapping("/project/deleteById")
    public ResultData<Integer> deleteById(@Validated @RequestBody IDParam idParam) {
        Integer id = idParam.getId();
        Project project = projectService.queryById(id);
        Validate.notNull(project);
        GroupQueryParam queryParam = new GroupQueryParam();
        queryParam.setProjectId(id);
        int groupCount = groupService.count(queryParam);
        if(groupCount > 0){
            return ResultData.result(ResultCode.projectDelErrorGroupExist);
        }
        int result = projectService.delete(project);
        if(result > 0){
            return ResultData.success(id);
        }else{
            return ResultData.result(ResultCode.systemError);
        }
    }
}
