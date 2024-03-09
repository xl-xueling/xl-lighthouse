package com.dtstep.lighthouse.insights.controller;

import com.dtstep.lighthouse.common.enums.RoleTypeEnum;
import com.dtstep.lighthouse.common.modal.Role;
import com.dtstep.lighthouse.common.entity.ResultCode;
import com.dtstep.lighthouse.insights.service.BaseService;
import com.dtstep.lighthouse.insights.service.PermissionService;
import com.dtstep.lighthouse.insights.service.RoleService;
import com.dtstep.lighthouse.insights.vo.ResultData;
import com.dtstep.lighthouse.common.modal.StatDataObject;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.insights.dto.DataStatQueryParam;
import com.dtstep.lighthouse.insights.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@ControllerAdvice
public class DataController {

    @Autowired
    private DataService dataService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private BaseService baseService;

    @PostMapping("/data/stat")
    public ResultData<List<StatDataObject>> dataQuery(@Validated @RequestBody DataStatQueryParam queryParam) throws Exception{
        int statId = queryParam.getStatId();
        int userId = baseService.getCurrentUserId();
        Role manageRole = roleService.queryRole(RoleTypeEnum.STAT_MANAGE_PERMISSION,statId);
        boolean hasManagePermission = permissionService.checkUserPermission(userId,manageRole.getId());
        if(!hasManagePermission){
            Role accessRole = roleService.queryRole(RoleTypeEnum.STAT_ACCESS_PERMISSION,statId);
            boolean hasAccessPermission = permissionService.checkUserPermission(userId,accessRole.getId());
            if(!hasAccessPermission){
                return ResultData.result(ResultCode.accessDenied);
            }
        }
        List<StatDataObject> objectList = dataService.dataQuery(queryParam.getStatId(),queryParam.getStartTime(),queryParam.getEndTime(),null);
        return ResultData.success(objectList);
    }

    @PostMapping("/test-data/stat")
    public ResultData<List<StatDataObject>> testDataQuery(@Validated @RequestBody DataStatQueryParam queryParam) throws Exception{
        int statId = queryParam.getStatId();
        int userId = baseService.getCurrentUserId();
        Role manageRole = roleService.queryRole(RoleTypeEnum.STAT_MANAGE_PERMISSION,statId);
        boolean hasManagePermission = permissionService.checkUserPermission(userId,manageRole.getId());
        if(!hasManagePermission){
            Role accessRole = roleService.queryRole(RoleTypeEnum.STAT_ACCESS_PERMISSION,statId);
            boolean hasAccessPermission = permissionService.checkUserPermission(userId,accessRole.getId());
            if(!hasAccessPermission){
                return ResultData.result(ResultCode.accessDenied);
            }
        }
        List<StatDataObject> objectList = dataService.testDataQuery(queryParam.getStatId(),queryParam.getStartTime(),queryParam.getEndTime(),null);
        System.out.println("objectList is:" + JsonUtil.toJSONString(objectList));
        return ResultData.success(objectList);
    }
}
