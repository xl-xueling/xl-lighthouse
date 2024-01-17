package com.dtstep.lighthouse.insights.controller;

import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.commonv2.insights.ResultCode;
import com.dtstep.lighthouse.insights.controller.annotation.AuthPermission;
import com.dtstep.lighthouse.insights.dto_bak.*;
import com.dtstep.lighthouse.insights.enums.RoleTypeEnum;
import com.dtstep.lighthouse.insights.modal.RenderConfig;
import com.dtstep.lighthouse.insights.modal.RenderFilterConfig;
import com.dtstep.lighthouse.insights.modal.Stat;
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
public class StatController {

    @Autowired
    private StatService statService;

    @RequestMapping("/stat/list")
    public ResultData<ListData<StatDto>> list(@Validated @RequestBody ListSearchObject<StatQueryParam> searchObject) {
        ListData<StatDto> listData = statService.queryList(searchObject.getQueryParams(),searchObject.getPagination().getPageNum(),searchObject.getPagination().getPageSize());
        return ResultData.success(listData);
    }

    @AuthPermission(roleTypeEnum = RoleTypeEnum.PROJECT_MANAGE_PERMISSION,relationParam = "projectId")
    @RequestMapping("/stat/create")
    public ResultData<Integer> create(@Validated @RequestBody Stat createParam) {
        int id = statService.create(createParam);
        if(id > 0){
            return ResultData.success(id);
        }else{
            return ResultData.result(ResultCode.systemError);
        }
    }

    @AuthPermission(roleTypeEnum = RoleTypeEnum.PROJECT_MANAGE_PERMISSION,relationParam = "projectId")
    @RequestMapping("/stat/update")
    public ResultData<Integer> update(@Validated @RequestBody Stat createParam) {
        int id = statService.update(createParam);
        if(id > 0){
            return ResultData.success(id);
        }else{
            return ResultData.result(ResultCode.systemError);
        }
    }

    @AuthPermission(roleTypeEnum = RoleTypeEnum.PROJECT_MANAGE_PERMISSION,relationParam = "projectId")
    @AuthPermission(roleTypeEnum = RoleTypeEnum.OPT_MANAGE_PERMISSION)
    @RequestMapping("/stat/changeState")
    public ResultData<Integer> changeState(@Validated @RequestBody ChangeStatStateParam changeParam) {
        Integer id = changeParam.getId();
        Stat stat = statService.queryById(id);
        Validate.notNull(stat);
        stat.setState(changeParam.getState());
        int result = statService.update(stat);
        if(result > 0){
            return ResultData.success(result);
        }else{
            return ResultData.result(ResultCode.systemError);
        }
    }

    @AuthPermission(roleTypeEnum = RoleTypeEnum.STAT_MANAGE_PERMISSION,relationParam = "id")
    @RequestMapping("/stat/deleteById")
    public ResultData<Integer> deleteById(@Validated @RequestBody IDParam idParam) {
        Integer id = idParam.getId();
        Stat stat = statService.queryById(id);
        Validate.notNull(stat);
        int result = statService.delete(stat);
        if(result > 0){
            return ResultData.success(id);
        }else{
            return ResultData.result(ResultCode.systemError);
        }
    }

    @RequestMapping("/stat/queryById")
    public ResultData<StatExtendDto> queryById(@Validated @RequestBody IDParam idParam){
        Integer id = idParam.getId();
        StatDto stat = statService.queryById(id);
        RenderConfig renderConfig = statService.getStatRenderConfig(stat);
        StatExtendDto statExtendDto = new StatExtendDto(stat);
        statExtendDto.setRenderConfig(renderConfig);
        Validate.notNull(stat);
        return ResultData.success(statExtendDto);
    }

    @AuthPermission(roleTypeEnum = RoleTypeEnum.STAT_MANAGE_PERMISSION,relationParam = "id")
    @RequestMapping("/stat/filterConfig")
    public ResultData<Integer> filterConfig(@Validated @RequestBody StatFilterConfigParam filterConfigParam) {
        Integer id = filterConfigParam.getId();
        Stat stat = statService.queryById(id);
        Validate.notNull(stat);
        List<RenderFilterConfig> configList = filterConfigParam.getFilters();
        ResultCode resultCode = statService.filterConfig(stat,configList);
        return ResultData.result(resultCode);
    }
}
