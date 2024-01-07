package com.dtstep.lighthouse.insights.controller;

import com.dtstep.lighthouse.commonv2.constant.SystemConstant;
import com.dtstep.lighthouse.commonv2.insights.ResultCode;
import com.dtstep.lighthouse.insights.controller.annotation.AuthPermission;
import com.dtstep.lighthouse.insights.dto.ResultData;
import com.dtstep.lighthouse.insights.dto.GroupDto;
import com.dtstep.lighthouse.insights.dto.GroupQueryParam;
import com.dtstep.lighthouse.insights.enums.RoleTypeEnum;
import com.dtstep.lighthouse.insights.modal.Group;
import com.dtstep.lighthouse.insights.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@ControllerAdvice
public class GroupController {

    @Autowired
    private GroupService groupService;

    @AuthPermission(roleTypeEnum = RoleTypeEnum.PROJECT_MANAGE_PERMISSION,relationParam = "projectId")
    @RequestMapping("/group/create")
    public ResultData<Integer> register(@Validated @RequestBody Group createParam) {
        GroupQueryParam countByTokenParam = new GroupQueryParam();
        countByTokenParam.setToken(createParam.getToken());
        int tokenCount = groupService.count(countByTokenParam);
        if(tokenCount > 0){
            return ResultData.failed(ResultCode.createGroupTokenExist);
        }
        GroupQueryParam countByProjectParam = new GroupQueryParam();
        countByProjectParam.setProjectId(createParam.getProjectId());
        int groupCount = groupService.count(countByProjectParam);
        if(groupCount > SystemConstant.PROJECT_MAX_GROUP_SIZE){
            return ResultData.failed(ResultCode.createGroupUnderProjectExceedLimit);
        }
        int id = groupService.create(createParam);
        if(id > 0){
            return ResultData.success(id);
        }else{
            return ResultData.failed(ResultCode.systemError);
        }
    }

    @RequestMapping("/group/queryById")
    public ResultData<Group> queryById(@Validated @RequestBody GroupQueryParam queryParam) {
        GroupDto groupDto = groupService.queryById(queryParam.getId());
        return ResultData.success(groupDto);
    }

    @RequestMapping("/group/queryByProjectId")
    public ResultData<List<Group>> queryByProjectId(@Validated @RequestBody GroupQueryParam queryParam) {
        List<Group> groupList = groupService.queryByProjectId(queryParam.getProjectId());
        return ResultData.success(groupList);
    }
}
