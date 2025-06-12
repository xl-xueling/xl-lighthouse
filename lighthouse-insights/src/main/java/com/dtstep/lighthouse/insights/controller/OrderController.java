package com.dtstep.lighthouse.insights.controller;
/*
 * Copyright (C) 2022-2025 XueLing.雪灵
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import com.dtstep.lighthouse.insights.service.BaseService;
import com.dtstep.lighthouse.insights.vo.ResultData;
import com.dtstep.lighthouse.insights.dto.OrderProcessParam;
import com.dtstep.lighthouse.insights.dto.QueryParam;
import com.dtstep.lighthouse.insights.service.OrderService;
import com.dtstep.lighthouse.insights.vo.OrderVO;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@ControllerAdvice
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private BaseService baseService;

    @RequestMapping("/order/queryById")
    public ResultData<OrderVO> queryById(@Validated @RequestBody QueryParam queryParam) throws Exception{
        OrderVO orderVO = orderService.queryById(queryParam.getId());
        Validate.notNull(orderVO);
        Object extend = orderService.queryRelatedElement(orderVO);
        orderVO.setExtend(extend);
        return ResultData.success(orderVO);
    }

    @PostMapping("/order/process")
    public ResultData<Integer> process(@Validated @RequestBody OrderProcessParam approveParam) throws Exception{
        int result = orderService.process(approveParam);
        return ResultData.success(result);
    }

    @PostMapping("/order/pendCount")
    public ResultData<Integer> pendCount(){
        int currentUserId = baseService.getCurrentUserId();
        int count = orderService.pendCount(currentUserId);
        return ResultData.success(count);
    }
}
