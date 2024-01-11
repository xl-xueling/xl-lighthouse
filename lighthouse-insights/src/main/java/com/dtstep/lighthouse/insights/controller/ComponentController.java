package com.dtstep.lighthouse.insights.controller;

import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.commonv2.insights.ResultCode;
import com.dtstep.lighthouse.insights.dto.CommonTreeNode;
import com.dtstep.lighthouse.insights.dto.ComponentVerifyParam;
import com.dtstep.lighthouse.insights.dto.ResultData;
import com.dtstep.lighthouse.insights.service.ComponentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@ControllerAdvice
public class ComponentController {

    @Autowired
    private ComponentService componentService;

    @RequestMapping("/component/verify")
    public ResultData<List<CommonTreeNode>> verify(@Validated @RequestBody ComponentVerifyParam verifyParam) {
        System.out.println("verifyParam:" + JsonUtil.toJSONString(verifyParam));
        ResultCode resultCode = componentService.verify(verifyParam.getConfiguration());
        return ResultData.result(resultCode);
    }

    @RequestMapping("/component/create")
    public ResultData<Integer> create(@Validated @RequestBody ComponentVerifyParam createParam) {
        System.out.println("createParam:" + JsonUtil.toJSONString(createParam));
        ResultCode resultCode = componentService.verify(createParam.getConfiguration());
        return ResultData.result(resultCode);
    }

}
