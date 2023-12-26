package com.dtstep.lighthouse.insights.controller;

import com.dtstep.lighthouse.client.LightHouse;
import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.commonv2.insights.ResultCode;
import com.dtstep.lighthouse.commonv2.insights.ResultData;
import com.dtstep.lighthouse.insights.dto.ListSearchObject;
import com.dtstep.lighthouse.insights.dto.StatQueryParam;
import com.dtstep.lighthouse.insights.modal.Stat;
import com.dtstep.lighthouse.insights.service.StatService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@ControllerAdvice
public class StatController {

    private StatService statService;

    @RequestMapping("/stat/list")
    public ResultData<ListData<Stat>> list(@Validated @RequestBody ListSearchObject<StatQueryParam> queryParam) {
        ListData<Stat> listData = new ListData<>();
        return ResultData.success(listData);
    }

    @RequestMapping("/stat/create")
    public ResultData<Integer> register(@Validated @RequestBody Stat createParam) {
        int id = statService.create(createParam);
        if(id > 0){
            return ResultData.success(id);
        }else{
            return ResultData.failed(ResultCode.ERROR);
        }
    }
}
