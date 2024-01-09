package com.dtstep.lighthouse.insights.controller;

import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.insights.dto.*;
import com.dtstep.lighthouse.insights.modal.Record;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
public class RecordsController {

    @PostMapping("/records/list")
    public ResultData<ListData<Record>> queryList(@Validated @RequestBody ListSearchObject<RecordQueryParam> searchObject){
        return null;
    }
}
