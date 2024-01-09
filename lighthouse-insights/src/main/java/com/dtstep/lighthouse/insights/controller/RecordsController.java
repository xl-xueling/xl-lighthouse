package com.dtstep.lighthouse.insights.controller;

import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.insights.dto.*;
import com.dtstep.lighthouse.insights.enums.RecordTypeEnum;
import com.dtstep.lighthouse.insights.enums.ResourceTypeEnum;
import com.dtstep.lighthouse.insights.modal.Record;
import com.dtstep.lighthouse.insights.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@ControllerAdvice
public class RecordsController {

    @Autowired
    private RecordService recordService;

    @PostMapping("/record/list")
    public ResultData<ListData<Record>> queryList(@Validated @RequestBody ListSearchObject<RecordQueryParam> searchObject){
        RecordQueryParam queryParam = new RecordQueryParam();
        ListData<Record> listData = recordService.queryList(queryParam);
        return ResultData.success(listData);
    }
}
