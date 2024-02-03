package com.dtstep.lighthouse.insights.controller;

import com.dtstep.lighthouse.common.modal.ListSearchObject;
import com.dtstep.lighthouse.insights.vo.ResultData;
import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.insights.dto.RecordQueryParam;
import com.dtstep.lighthouse.common.modal.Record;
import com.dtstep.lighthouse.insights.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
public class RecordsController {

    @Autowired
    private RecordService recordService;

    @PostMapping("/record/list")
    public ResultData<ListData<Record>> queryList(@Validated @RequestBody ListSearchObject<RecordQueryParam> searchObject){
        RecordQueryParam queryParam = searchObject.getQueryParamOrDefault(new RecordQueryParam());
        Integer pageNum = searchObject.getPagination().getPageNum();
        Integer pageSize = searchObject.getPagination().getPageSize();
        ListData<Record> listData = recordService.queryList(queryParam,pageNum,pageSize);
        return ResultData.success(listData);
    }
}
