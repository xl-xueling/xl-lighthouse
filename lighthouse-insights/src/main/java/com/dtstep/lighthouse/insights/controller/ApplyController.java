package com.dtstep.lighthouse.insights.controller;
/*
 * Copyright (C) 2022-2024 XueLing.雪灵
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
import com.dtstep.lighthouse.common.modal.ListSearchObject;
import com.dtstep.lighthouse.common.modal.Pagination;
import com.dtstep.lighthouse.insights.vo.ResultData;
import com.dtstep.lighthouse.common.entity.ListData;
import com.dtstep.lighthouse.common.entity.ResultCode;
import com.dtstep.lighthouse.insights.dto.ApplyOrderQueryParam;
import com.dtstep.lighthouse.insights.dto.OrderCreateParam;
import com.dtstep.lighthouse.common.modal.User;
import com.dtstep.lighthouse.insights.service.BaseService;
import com.dtstep.lighthouse.insights.service.OrderService;
import com.dtstep.lighthouse.insights.service.UserService;
import com.dtstep.lighthouse.insights.vo.OrderVO;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
public class ApplyController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private BaseService baseService;

    @RequestMapping("/apply/create")
    public ResultData<Integer> create(@Validated @RequestBody OrderCreateParam createParam) throws Exception {
        int currentUserId = baseService.getCurrentUserId();
        Validate.isTrue(currentUserId == createParam.getUserId());
        User user = userService.queryById(currentUserId);
        ResultCode resultCode = orderService.submit(user,createParam.getOrderType(),createParam.getReason(),createParam.getExtendConfig());
        return ResultData.result(resultCode);
    }

    @RequestMapping("/apply/list")
    public ResultData<ListData<OrderVO>> list(@Validated @RequestBody ListSearchObject<ApplyOrderQueryParam> searchObject) {
        ApplyOrderQueryParam queryParam = searchObject.getQueryParamOrDefault(new ApplyOrderQueryParam());
        Pagination pagination = searchObject.getPagination();
        ListData<OrderVO> listData = orderService.queryApplyList(queryParam, pagination.getPageNum(), pagination.getPageSize());
        return ResultData.success(listData);
    }
}
