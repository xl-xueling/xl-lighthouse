package com.dtstep.lighthouse.insights.controller;

import com.dtstep.lighthouse.insights.dto.*;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@ControllerAdvice
public class DataController {

    @PostMapping("/test-data/stat")
    public ResultData<List<StatDataObject>> testDataQuery(){

        return null;
    }

    @PostMapping("/data/stat")
    public ResultData<List<StatDataObject>> dataQuery(){

        return null;
    }
}
