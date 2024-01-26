package com.dtstep.lighthouse.insights.controller;

import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.commonv2.insights.ResultCode;
import com.dtstep.lighthouse.insights.controller.annotation.AuthPermission;
import com.dtstep.lighthouse.insights.dto.*;
import com.dtstep.lighthouse.insights.dto_bak.ResultData;
import com.dtstep.lighthouse.insights.dto_bak.*;
import com.dtstep.lighthouse.common.enums.RoleTypeEnum;
import com.dtstep.lighthouse.insights.modal.MetricSet;
import com.dtstep.lighthouse.insights.modal.Project;
import com.dtstep.lighthouse.insights.service.GroupService;
import com.dtstep.lighthouse.insights.service.ProjectService;
import com.dtstep.lighthouse.insights.vo.ProjectVO;
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
    public ResultData<Integer> create(@Validated @RequestBody ProjectCreateParam createParam) throws Exception {
        Project project = new Project();
        project.setTitle(createParam.getTitle());
        project.setDepartmentId(createParam.getDepartmentId());
        project.setPrivateType(createParam.getPrivateType());
        project.setDesc(createParam.getDesc());
        int id = projectService.create(project);
        if(id > 0){
            PermissionGrantParam grantParam = new PermissionGrantParam();
            grantParam.setResourceId(id);
            grantParam.setRoleType(RoleTypeEnum.PROJECT_ACCESS_PERMISSION);
            grantParam.setUsersPermissions(createParam.getUsersPermission());
            grantParam.setDepartmentsPermissions(createParam.getDepartmentsPermission());
            projectService.batchGrantPermissions(grantParam);
            return ResultData.success(id);
        }else{
            return ResultData.result(ResultCode.systemError);
        }
    }

    @RequestMapping("/project/queryById")
    public ResultData<ProjectVO> queryById(@RequestBody QueryParam queryParam) throws Exception{
        ProjectVO projectVO = projectService.queryById(queryParam.getId());
        if(projectVO == null){
            return ResultData.result(ResultCode.elementNotFound);
        }
        TreeNode structure = projectService.getStructure(projectVO);
        projectVO.setStructure(structure);
        return ResultData.success(projectVO);
    }

    @PostMapping("/project/list")
    public ResultData<ListData<ProjectVO>> queryList(@Validated @RequestBody ListSearchObject<ProjectQueryParam> searchObject){
        ProjectQueryParam queryParam = searchObject.getQueryParamOrDefault(new ProjectQueryParam());
        Pagination pagination = searchObject.getPagination();
        ListData<ProjectVO> listData = projectService.queryList(queryParam,pagination.getPageNum(),pagination.getPageSize());
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

    @AuthPermission(roleTypeEnum = RoleTypeEnum.PROJECT_MANAGE_PERMISSION,relationParam = "resourceId")
    @RequestMapping("/project/grant")
    public ResultData<Integer> grant(@Validated @RequestBody PermissionGrantParam grantParam) throws Exception{
        ResultCode resultCode = projectService.batchGrantPermissions(grantParam);
        return ResultData.result(resultCode);
    }

    @AuthPermission(roleTypeEnum = RoleTypeEnum.PROJECT_MANAGE_PERMISSION,relationParam = "resourceId")
    @RequestMapping("/project/release")
    public ResultData<Integer> release(@Validated @RequestBody PermissionReleaseParam releaseParam) throws Exception{
        ResultCode resultCode = projectService.releasePermission(releaseParam);
        return ResultData.result(resultCode);
    }
}
