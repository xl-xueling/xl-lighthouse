package com.dtstep.lighthouse.insights.controller;

import com.dtstep.lighthouse.common.enums.RoleTypeEnum;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.commonv2.insights.ResultCode;
import com.dtstep.lighthouse.insights.controller.annotation.AuthPermission;
import com.dtstep.lighthouse.insights.dto.*;
import com.dtstep.lighthouse.insights.dto_bak.*;
import com.dtstep.lighthouse.insights.enums.RelationTypeEnum;
import com.dtstep.lighthouse.insights.modal.Indicator;
import com.dtstep.lighthouse.insights.modal.MetricSet;
import com.dtstep.lighthouse.insights.service.MetricSetService;
import com.dtstep.lighthouse.insights.service.RelationService;
import com.dtstep.lighthouse.insights.vo.MetricSetVO;
import com.dtstep.lighthouse.insights.vo.RelationVO;
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
public class MetricSetController {

    @Autowired
    private MetricSetService metricSetService;

    @Autowired
    private RelationService relationService;

    @RequestMapping("/metricset/create")
    public ResultData<Integer> create(@Validated @RequestBody MetricSetCreateParam createParam) throws Exception {
        MetricSet metricSet = new MetricSet();
        metricSet.setDesc(createParam.getDesc());
        metricSet.setTitle(createParam.getTitle());
        metricSet.setPrivateType(createParam.getPrivateType());
        int id = metricSetService.create(metricSet);
        if(id > 0){
            PermissionGrantParam grantParam = new PermissionGrantParam();
            grantParam.setResourceId(id);
            grantParam.setRoleType(RoleTypeEnum.METRIC_ACCESS_PERMISSION);
            grantParam.setUsersPermissions(createParam.getInitUsersPermission());
            grantParam.setDepartmentsPermissions(createParam.getInitDepartmentsPermission());
            metricSetService.batchGrantPermissions(grantParam);
            return ResultData.success(id);
        }else{
            return ResultData.result(ResultCode.systemError);
        }
    }

    @RequestMapping("/metricset/update")
    public ResultData<Integer> update(@Validated @RequestBody MetricSetUpdateParam updateParam) {
        Integer id = updateParam.getId();
        MetricSet metricSet = metricSetService.queryById(id);
        Validate.notNull(metricSet);
        metricSet.setDesc(updateParam.getDesc());
        metricSet.setTitle(updateParam.getTitle());
        metricSet.setId(updateParam.getId());
        metricSet.setPrivateType(updateParam.getPrivateType());
        int result = metricSetService.update(metricSet);
        if(result > 0){
            return ResultData.success(result);
        }else{
            return ResultData.result(ResultCode.systemError);
        }
    }

    @RequestMapping("/metricset/queryById")
    public ResultData<MetricSet> queryById(@Validated @RequestBody IDParam idParam) throws Exception{
        Integer id = idParam.getId();
        MetricSetVO metricSetVO = metricSetService.queryById(id);
        if(metricSetVO == null){
            return ResultData.result(ResultCode.elementNotFound);
        }
        TreeNode structure = metricSetService.getStructure(metricSetVO);
        metricSetVO.setStructure(structure);
        return ResultData.success(metricSetVO);
    }

    @RequestMapping("/metricset/binded")
    public ResultData<MetricSet> binded(@Validated @RequestBody MetricBindParam bindParam) {
        metricSetService.binded(bindParam);
        return ResultData.success();
    }

    @RequestMapping("/metricset/bindlist")
    public ResultData<ListData<RelationVO>> bindedList(@Validated @RequestBody ListSearchObject<MetricBindQueryParam> searchObject) {
        MetricBindQueryParam bindQueryParam = searchObject.getQueryParamOrDefault(new MetricBindQueryParam());
        Pagination pagination = searchObject.getPagination();
        RelationQueryParam relationQueryParam = new RelationQueryParam();
        relationQueryParam.setRelationType(RelationTypeEnum.MetricSetBindRelation);
        relationQueryParam.setSubjectId(bindQueryParam.getId());
        relationQueryParam.setSearch(bindQueryParam.getSearch());
        ListData<RelationVO> listData = relationService.queryList(relationQueryParam, pagination.getPageNum(), pagination.getPageSize());
        return ResultData.success(listData);
    }


    @RequestMapping("/metricset/list")
    public ResultData<ListData<MetricSetVO>> list(@Validated @RequestBody ListSearchObject<MetricSetQueryParam> searchObject) {
        MetricSetQueryParam queryParam = searchObject.getQueryParamOrDefault(new MetricSetQueryParam());
        Pagination pagination = searchObject.getPagination();
        ListData<MetricSetVO> listData = metricSetService.queryList(queryParam, pagination.getPageNum(), pagination.getPageSize());
        return ResultData.success(listData);
    }

    @RequestMapping("/metricset/bindRemove")
    public ResultData<MetricSet> bindRemove(@Validated @RequestBody MetricBindRemoveParam removeParam) {
        Integer id = removeParam.getId();
        metricSetService.bindRemove(removeParam);
        return ResultData.success();
    }

    @AuthPermission(roleTypeEnum = RoleTypeEnum.METRIC_MANAGE_PERMISSION,relationParam = "id")
    @RequestMapping("/metricset/deleteById")
    public ResultData<Integer> deleteById(@Validated @RequestBody IDParam idParam) {
        Integer id = idParam.getId();
        MetricSet metricSet = metricSetService.queryById(id);
        Validate.notNull(metricSet);
        int result = metricSetService.delete(metricSet);
        if(result > 0){
            return ResultData.success(id);
        }else{
            return ResultData.result(ResultCode.systemError);
        }
    }

    @AuthPermission(roleTypeEnum = RoleTypeEnum.METRIC_MANAGE_PERMISSION,relationParam = "id")
    @RequestMapping("/metricset/updateStructure")
    public ResultData<Integer> updateStructure(@Validated @RequestBody MetricUpdateStructureParam updateStructureParam) {
        metricSetService.updateStructure(updateStructureParam);
        return ResultData.success();
    }

    @AuthPermission(roleTypeEnum = RoleTypeEnum.METRIC_MANAGE_PERMISSION,relationParam = "id")
    @RequestMapping("/metricset/resetStructure")
    public ResultData<Integer> resetStructure(@Validated @RequestBody IDParam idParam) {
        Integer id = idParam.getId();
        MetricSet metricSet = metricSetService.queryById(id);
        Validate.notNull(metricSet);
        metricSet.setStructure(new TreeNode(metricSet.getTitle(),metricSet.getId(),"metric"));
        metricSetService.update(metricSet);
        return ResultData.result(ResultCode.success);
    }

    @RequestMapping("/metricset/indicatorList")
    public ResultData<ListData<Indicator>> indicatorList(@Validated @RequestBody ListSearchObject<MetricPendQueryParam> searchObject) {
        MetricPendQueryParam queryParam = searchObject.getQueryParamOrDefault(new MetricPendQueryParam());
        Pagination pagination = searchObject.getPagination();
        ListData<Indicator> listData = metricSetService.queryIndicatorList(queryParam,pagination.getPageNum(),pagination.getPageSize());
        return ResultData.success(listData);
    }

    @RequestMapping("/metricset/starById")
    public ResultData<Integer> starById(@Validated @RequestBody IDParam idParam) {
        Integer id = idParam.getId();
        MetricSet metricSet = metricSetService.queryById(id);
        Validate.notNull(metricSet);
        ResultCode resultCode = metricSetService.star(metricSet);
        return ResultData.result(resultCode);
    }

    @RequestMapping("/metricset/unStarById")
    public ResultData<Integer> unFocusById(@Validated @RequestBody IDParam idParam) {
        Integer id = idParam.getId();
        MetricSet metricSet = metricSetService.queryById(id);
        Validate.notNull(metricSet);
        ResultCode resultCode = metricSetService.unStar(metricSet);
        return ResultData.result(resultCode);
    }

    @RequestMapping("/metricset/queryStarList")
    public ResultData<List<MetricSetVO>> queryStarList(){
        List<MetricSetVO> list = metricSetService.queryStarList();
        return ResultData.success(list);
    }

}
