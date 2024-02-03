package com.dtstep.lighthouse.insights.controller;

import com.dtstep.lighthouse.commonv2.constant.SystemConstant;
import com.dtstep.lighthouse.commonv2.insights.ResultCode;
import com.dtstep.lighthouse.insights.controller.annotation.AuthPermission;
import com.dtstep.lighthouse.insights.dto.GroupCreateParam;
import com.dtstep.lighthouse.insights.dto.GroupUpdateParam;
import com.dtstep.lighthouse.insights.dto.StatQueryParam;
import com.dtstep.lighthouse.common.modal.IDParam;
import com.dtstep.lighthouse.insights.vo.ResultData;
import com.dtstep.lighthouse.insights.dto.GroupQueryParam;
import com.dtstep.lighthouse.common.enums.RoleTypeEnum;
import com.dtstep.lighthouse.common.modal.Domain;
import com.dtstep.lighthouse.common.modal.Group;
import com.dtstep.lighthouse.insights.service.DomainService;
import com.dtstep.lighthouse.insights.service.GroupService;
import com.dtstep.lighthouse.insights.service.StatService;
import org.apache.commons.lang3.Validate;
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

    @Autowired
    private StatService statService;

    @Autowired
    private DomainService domainService;

    @AuthPermission(roleTypeEnum = RoleTypeEnum.PROJECT_MANAGE_PERMISSION,relationParam = "projectId")
    @RequestMapping("/group/create")
    public ResultData<Integer> create(@Validated @RequestBody GroupCreateParam createParam) {
        GroupQueryParam countByTokenParam = new GroupQueryParam();
        Domain domain = domainService.queryDefault();
        String token = String.format("%s:%s",domain.getDefaultTokenPrefix(),createParam.getToken());
        countByTokenParam.setToken(token);
        int tokenCount = groupService.count(countByTokenParam);
        if(tokenCount > 0){
            return ResultData.result(ResultCode.createGroupTokenExist);
        }
        GroupQueryParam countByProjectParam = new GroupQueryParam();
        countByProjectParam.setProjectId(createParam.getProjectId());
        int groupCount = groupService.count(countByProjectParam);
        if(groupCount > SystemConstant.PROJECT_MAX_GROUP_SIZE){
            return ResultData.result(ResultCode.createGroupUnderProjectExceedLimit);
        }
        Group group = new Group();
        group.setToken(token);
        group.setColumns(createParam.getColumns());
        group.setDesc(createParam.getDesc());
        group.setProjectId(createParam.getProjectId());
        int id = groupService.create(group);
        if(id > 0){
            return ResultData.success(id);
        }else{
            return ResultData.result(ResultCode.systemError);
        }
    }

    @AuthPermission(roleTypeEnum = RoleTypeEnum.PROJECT_MANAGE_PERMISSION,relationParam = "projectId")
    @RequestMapping("/group/update")
    public ResultData<Integer> update(@Validated @RequestBody GroupUpdateParam updateParam) {
        Integer id = updateParam.getId();
        Group group = groupService.queryById(id);
        group.setColumns(updateParam.getColumns());
        group.setDesc(updateParam.getDesc());
        int result = groupService.update(group);
        if(result > 0){
            return ResultData.success(result);
        }else{
            return ResultData.result(ResultCode.systemError);
        }
    }

    @AuthPermission(roleTypeEnum = RoleTypeEnum.GROUP_MANAGE_PERMISSION,relationParam = "id")
    @RequestMapping("/group/queryById")
    public ResultData<Group> queryById(@Validated @RequestBody GroupQueryParam queryParam) {
        Group group = groupService.queryById(queryParam.getId());
        return ResultData.success(group);
    }

    @RequestMapping("/group/deleteById")
    public ResultData<Integer> deleteById(@Validated @RequestBody IDParam idParam) {
        Group group = groupService.queryById(idParam.getId());
        Validate.notNull(group);
        StatQueryParam queryParam = new StatQueryParam();
        queryParam.setGroupIds(List.of(group.getId()));
        int countStat = statService.count(queryParam);
        if(countStat > 0){
            return ResultData.result(ResultCode.groupDelExistSubStat);
        }
        int result = groupService.delete(group);
        if(result > 0) {
            return ResultData.success(result);
        }else{
            return ResultData.result(ResultCode.systemError);
        }
    }

    @AuthPermission(roleTypeEnum = RoleTypeEnum.GROUP_MANAGE_PERMISSION,relationParam = "id")
    @RequestMapping("/group/getSecretKey")
    public ResultData<String> querySecretKey(@Validated @RequestBody IDParam idParam) {
        Integer id = idParam.getId();
        String secretKey = groupService.getSecretKey(id);
        return ResultData.success(secretKey);
    }

    @RequestMapping("/group/queryByProjectId")
    public ResultData<List<Group>> queryByProjectId(@Validated @RequestBody GroupQueryParam queryParam) {
        List<Group> groupList = groupService.queryByProjectId(queryParam.getProjectId());
        return ResultData.success(groupList);
    }
}
