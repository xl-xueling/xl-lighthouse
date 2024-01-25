package com.dtstep.lighthouse.insights.controller;

import com.dtstep.lighthouse.common.enums.RoleTypeEnum;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.commonv2.insights.ResultCode;
import com.dtstep.lighthouse.insights.dto.*;
import com.dtstep.lighthouse.insights.dto_bak.*;
import com.dtstep.lighthouse.insights.enums.RelationTypeEnum;
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
        List<TreeNode> structure = metricSetService.getStructure(metricSetVO);
        System.out.println("structure:" + JsonUtil.toJSONString(structure));
        metricSetVO.setStructure(structure);
        return ResultData.success(metricSetVO);
    }

    @RequestMapping("/metricset/binded")
    public ResultData<MetricSet> binded(@Validated @RequestBody MetricBindParam bindParam) {
        System.out.println("metricSet:" + JsonUtil.toJSONString(bindParam));
        metricSetService.binded(bindParam);
        return ResultData.success();
    }

    @RequestMapping("/metricset/bindlist")
    public ResultData<ListData<RelationVO>> bindedList(@Validated @RequestBody ListSearchObject<RelationQueryParam> searchObject) {
        RelationQueryParam queryParam = searchObject.getQueryParamOrDefault(new RelationQueryParam());
        Pagination pagination = searchObject.getPagination();
        ListData<RelationVO> listData = relationService.queryList(queryParam, pagination.getPageNum(), pagination.getPageSize());
        System.out.println("relationDtos is:" + JsonUtil.toJSONString(listData));
        return ResultData.success(listData);
    }


    @RequestMapping("/metricset/list")
    public ResultData<ListData<MetricSetVO>> list(@Validated @RequestBody ListSearchObject<MetricSetQueryParam> searchObject) {
        MetricSetQueryParam queryParam = searchObject.getQueryParamOrDefault(new MetricSetQueryParam());
        Pagination pagination = searchObject.getPagination();
        ListData<MetricSetVO> listData = metricSetService.queryList(queryParam, pagination.getPageNum(), pagination.getPageSize());
        return ResultData.success(listData);
    }

}
