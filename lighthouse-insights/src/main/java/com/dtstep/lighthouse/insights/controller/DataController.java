package com.dtstep.lighthouse.insights.controller;

import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.enums.RoleTypeEnum;
import com.dtstep.lighthouse.common.modal.LimitDataObject;
import com.dtstep.lighthouse.common.modal.Role;
import com.dtstep.lighthouse.common.entity.ResultCode;
import com.dtstep.lighthouse.common.modal.Stat;
import com.dtstep.lighthouse.core.batch.BatchAdapter;
import com.dtstep.lighthouse.core.builtin.BuiltinLoader;
import com.dtstep.lighthouse.core.wrapper.StatDBWrapper;
import com.dtstep.lighthouse.insights.dto.LimitStatQueryParam;
import com.dtstep.lighthouse.insights.service.*;
import com.dtstep.lighthouse.insights.vo.ResultData;
import com.dtstep.lighthouse.common.modal.StatDataObject;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.insights.dto.DataStatQueryParam;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.Validate;
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

    @Autowired
    private StatService statService;

    @PostMapping("/data/stat")
    public ResultData<List<StatDataObject>> dataQuery(@Validated @RequestBody DataStatQueryParam queryParam) throws Exception{
        int statId = queryParam.getStatId();
        if(!BuiltinLoader.isBuiltinStat(statId)){
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
        }
        StatExtEntity stat = statService.queryById(statId);
        Validate.notNull(stat);
        List<String> dimensList = dataService.dimensArrangement(stat,queryParam.getDimensParams());
        List<StatDataObject> objectList = dataService.dataQuery(stat,queryParam.getStartTime(),queryParam.getEndTime(),dimensList);
        return ResultData.success(objectList);
    }

    @PostMapping("/limit/stat")
    public ResultData<List<LimitDataObject>> limitQuery(@Validated @RequestBody LimitStatQueryParam queryParam) throws Exception {
        int statId = queryParam.getStatId();
        StatExtEntity stat = statService.queryById(statId);
        Validate.notNull(stat);
        List<Long> batchTimeList = queryParam.getBatchTimeList();
        if(CollectionUtils.isEmpty(batchTimeList)){
            batchTimeList = BatchAdapter.queryBatchTimeList(stat.getTimeparam(),0,System.currentTimeMillis(),10);
        }
        List<LimitDataObject> objectList = dataService.limitQuery(stat,batchTimeList);
        return ResultData.success(objectList);
    }

    @PostMapping("/test-data/stat")
    public ResultData<List<StatDataObject>> testDataQuery(@Validated @RequestBody DataStatQueryParam queryParam) throws Exception{
        int statId = queryParam.getStatId();
        if(!BuiltinLoader.isBuiltinStat(statId)){
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
        }
        StatExtEntity statExtEntity = statService.queryById(statId);
        List<String> dimensList = dataService.dimensArrangement(statExtEntity,queryParam.getDimensParams());
        List<StatDataObject> objectList = dataService.testDataQuery(statExtEntity,queryParam.getStartTime(),queryParam.getEndTime(),dimensList);
        System.out.println("objectList is:" + JsonUtil.toJSONString(objectList));
        return ResultData.success(objectList);
    }
}
