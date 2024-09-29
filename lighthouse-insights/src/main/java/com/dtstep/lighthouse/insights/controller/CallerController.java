package com.dtstep.lighthouse.insights.controller;

import com.dtstep.lighthouse.common.entity.ListData;
import com.dtstep.lighthouse.common.entity.ResultCode;
import com.dtstep.lighthouse.common.entity.ServiceResult;
import com.dtstep.lighthouse.common.enums.RoleTypeEnum;
import com.dtstep.lighthouse.common.modal.*;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.insights.controller.annotation.AuthPermission;
import com.dtstep.lighthouse.insights.dto.CallerCreateParam;
import com.dtstep.lighthouse.insights.dto.CallerUpdateParam;
import com.dtstep.lighthouse.insights.service.BaseService;
import com.dtstep.lighthouse.insights.vo.CallerVO;
import com.dtstep.lighthouse.insights.vo.ResultData;
import com.dtstep.lighthouse.insights.service.CallerService;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
public class CallerController {

    private static final Logger logger = LoggerFactory.getLogger(CallerController.class);

    @Autowired
    private CallerService callerService;

    @Autowired
    private BaseService baseService;

    @RequestMapping(value = "/caller/create")
    public ResultData<Integer> createView(@Validated @RequestBody CallerCreateParam createParam) throws Exception {
        Caller caller = new Caller();
        caller.setName(createParam.getName());
        caller.setDesc(createParam.getDesc());
        ServiceResult<Integer> serviceResult = callerService.create(caller);
        return ResultData.result(serviceResult.getResultCode());
    }

    @AuthPermission(roleTypeEnum = RoleTypeEnum.CALLER_MANAGER_PERMISSION,relationParam = "id")
    @RequestMapping("/caller/update")
    public ResultData<Integer> update(@Validated @RequestBody CallerUpdateParam updateParam) throws Exception {
        Integer id = updateParam.getId();
        Caller caller = callerService.queryById(id);
        Validate.notNull(caller);
        caller.setName(updateParam.getName());
        caller.setDesc(updateParam.getDesc());
        int result = callerService.update(caller);
        if(result > 0){
            return ResultData.success(result);
        }else{
            return ResultData.result(ResultCode.systemError);
        }
    }

    @RequestMapping("/caller/queryById")
    public ResultData<CallerVO> queryById(@Validated @RequestBody IDParam idParam) throws Exception{
        Integer id = idParam.getId();
        CallerVO caller = callerService.queryById(id);
        if(caller == null){
            return ResultData.result(ResultCode.elementNotFound);
        }
        return ResultData.success(caller);
    }

    @RequestMapping("/caller/list")
    public ResultData<ListData<CallerVO>> list(@Validated @RequestBody ListSearchObject<CallerQueryParam> searchObject) throws Exception {
        CallerQueryParam queryParam = searchObject.getQueryParamOrDefault(new CallerQueryParam());
        queryParam.setOwnerId(baseService.getCurrentUserId());
        Pagination pagination = searchObject.getPagination();
        ListData<CallerVO> listData = callerService.queryList(queryParam, pagination.getPageNum(), pagination.getPageSize());
        return ResultData.success(listData);
    }

    @AuthPermission(roleTypeEnum = RoleTypeEnum.CALLER_MANAGER_PERMISSION,relationParam = "id")
    @RequestMapping("/caller/deleteById")
    public ResultData<Integer> deleteById(@Validated @RequestBody IDParam idParam) throws Exception {
        Integer id = idParam.getId();
        callerService.deleteById(id);
        return ResultData.success();
    }

    @AuthPermission(roleTypeEnum = RoleTypeEnum.CALLER_MANAGER_PERMISSION,relationParam = "id")
    @RequestMapping("/caller/getSecretKey")
    public ResultData<String> getSecretKey(@Validated @RequestBody IDParam idParam) throws Exception {
        Integer id = idParam.getId();
        String secretKey = callerService.getSecretKey(id);
        return ResultData.success(secretKey);
    }

    @AuthPermission(roleTypeEnum = RoleTypeEnum.CALLER_MANAGER_PERMISSION,relationParam = "id")
    @RequestMapping("/caller/authApply")
    public ResultData<Integer> authApply(@Validated @RequestBody AuthApplyParam authApplyParam) throws Exception {
        System.out.println("authApplyParam is:" + JsonUtil.toJSONString(authApplyParam));
        return ResultData.success(1);
    }

}
