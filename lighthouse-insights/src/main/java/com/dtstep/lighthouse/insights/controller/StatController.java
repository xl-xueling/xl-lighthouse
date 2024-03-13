package com.dtstep.lighthouse.insights.controller;

import com.dtstep.lighthouse.common.modal.*;
import com.dtstep.lighthouse.common.entity.ListData;
import com.dtstep.lighthouse.common.entity.ResultCode;
import com.dtstep.lighthouse.insights.controller.annotation.AuthPermission;
import com.dtstep.lighthouse.insights.dto.ChangeStatStateParam;
import com.dtstep.lighthouse.insights.dto.StatFilterConfigParam;
import com.dtstep.lighthouse.insights.dto.StatQueryParam;
import com.dtstep.lighthouse.common.enums.RoleTypeEnum;
import com.dtstep.lighthouse.insights.service.StatService;
import com.dtstep.lighthouse.insights.vo.ResultData;
import com.dtstep.lighthouse.insights.vo.StatVO;
import com.dtstep.lighthouse.insights.vo.StatExtendVO;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(StatController.class);

    @Autowired
    private StatService statService;

    @RequestMapping("/stat/list")
    public ResultData<ListData<StatVO>> list(@Validated @RequestBody ListSearchObject<StatQueryParam> searchObject) {
        StatQueryParam queryParam = searchObject.getQueryParamOrDefault(new StatQueryParam());
        ListData<StatVO> listData = statService.queryList(queryParam,searchObject.getPagination().getPageNum(),searchObject.getPagination().getPageSize());
        return ResultData.success(listData);
    }

    @AuthPermission(roleTypeEnum = RoleTypeEnum.PROJECT_MANAGE_PERMISSION,relationParam = "projectId")
    @RequestMapping("/stat/create")
    public ResultData<Integer> create(@Validated @RequestBody Stat createParam) throws Exception {
        ResultCode resultCode = statService.create(createParam);
        return ResultData.result(resultCode);
    }

    @AuthPermission(roleTypeEnum = RoleTypeEnum.PROJECT_MANAGE_PERMISSION,relationParam = "projectId")
    @RequestMapping("/stat/update")
    public ResultData<Integer> update(@Validated @RequestBody Stat createParam) {
        ResultCode resultCode = statService.update(createParam);
        return ResultData.result(resultCode);
    }

    @AuthPermission(roleTypeEnum = RoleTypeEnum.PROJECT_MANAGE_PERMISSION,relationParam = "projectId")
    @AuthPermission(roleTypeEnum = RoleTypeEnum.OPT_MANAGE_PERMISSION)
    @RequestMapping("/stat/changeState")
    public ResultData<Integer> changeState(@Validated @RequestBody ChangeStatStateParam changeParam) throws Exception {
        Integer id = changeParam.getId();
        Stat stat = statService.queryById(id);
        Validate.notNull(stat);
        statService.changeState(stat,changeParam.getState());
        return ResultData.success();
    }

    @AuthPermission(roleTypeEnum = RoleTypeEnum.STAT_MANAGE_PERMISSION,relationParam = "id")
    @RequestMapping("/stat/deleteById")
    public ResultData<Integer> deleteById(@Validated @RequestBody IDParam idParam) throws Exception {
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
    public ResultData<StatExtendVO> queryById(@Validated @RequestBody IDParam idParam) throws Exception {
        Integer id = idParam.getId();
        StatVO stat = statService.queryById(id);
        if(stat == null){
            return ResultData.result(ResultCode.elementNotFound);
        }
        RenderConfig renderConfig = statService.getStatRenderConfig(stat);
        StatExtendVO statExtendDto = new StatExtendVO(stat);
        statExtendDto.setRenderConfig(renderConfig);
        Validate.notNull(stat);
        return ResultData.success(statExtendDto);
    }

    @AuthPermission(roleTypeEnum = RoleTypeEnum.STAT_MANAGE_PERMISSION,relationParam = "id")
    @RequestMapping("/stat/filterConfig")
    public ResultData<Integer> filterConfig(@Validated @RequestBody StatFilterConfigParam filterConfigParam) throws Exception{
        Integer id = filterConfigParam.getId();
        StatVO stat = statService.queryById(id);
        Validate.notNull(stat);
        List<RenderFilterConfig> configList = filterConfigParam.getFilters();
        ResultCode resultCode = statService.filterConfig(stat,configList);
        return ResultData.result(resultCode);
    }


    @AuthPermission(roleTypeEnum = RoleTypeEnum.STAT_MANAGE_PERMISSION,relationParam = "id")
    @RequestMapping("/stat/filterReset")
    public ResultData<Integer> filterReset(@Validated @RequestBody IDParam idParam) throws Exception{
        Integer id = idParam.getId();
        Stat stat = statService.queryById(id);
        Validate.notNull(stat);
        RenderConfig renderConfig = stat.getRenderConfig();
        renderConfig.setFilters(null);
        ResultCode resultCode = statService.update(stat);
        return ResultData.result(resultCode);
    }
}
