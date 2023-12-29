package com.dtstep.lighthouse.insights.controller;

import com.dtstep.lighthouse.commonv2.insights.ResultData;
import com.dtstep.lighthouse.insights.modal.Order;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
public class ApplyController {


    @RequestMapping("/apply/create")
    public ResultData<Integer> create(@Validated @RequestBody Order createParam) {
        System.out.println("order create...");
        return ResultData.success(null);
    }
}
