package com.dtstep.lighthouse.insights.controller;

import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.insights.dto.*;
import com.dtstep.lighthouse.insights.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@ControllerAdvice
public class DataController {

    @Autowired
    private DataService dataService;

    @PostMapping("/data/stat")
    public ResultData<List<StatDataObject>> dataQuery(@Validated @RequestBody DataStatQueryParam queryParam){
        System.out.println("query param is:" + JsonUtil.toJSONString(queryParam));
        Integer statId = queryParam.getStatId();
        return null;
    }

    @PostMapping("/test-data/stat")
    public ResultData<List<StatDataObject>> testDataQuery(@Validated @RequestBody DataStatQueryParam queryParam){
        List<StatDataObject> objectList = dataService.testDataQuery(queryParam.getStatId(),queryParam.getStartTime(),queryParam.getEndTime(),null);
        System.out.println("objectList is:" + JsonUtil.toJSONString(objectList));
        return ResultData.success(objectList);
    }
}
