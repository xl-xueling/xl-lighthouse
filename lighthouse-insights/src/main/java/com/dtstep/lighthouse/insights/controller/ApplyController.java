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
import com.dtstep.lighthouse.common.enums.OrderTypeEnum;
import com.dtstep.lighthouse.common.modal.ListSearchObject;
import com.dtstep.lighthouse.common.modal.Pagination;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.insights.service.impl.OrderServiceImpl;
import com.dtstep.lighthouse.insights.vo.ResourceVO;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@ControllerAdvice
public class ApplyController {

    private static final Logger logger = LoggerFactory.getLogger(ApplyController.class);

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
        ResultCode resultCode;
        if(createParam.getOrderType() == OrderTypeEnum.VIEW_ACCESS){
            resultCode = orderService.submit(user,createParam.getOrderType(),createParam.getReason(),createParam.getExtendConfig());
            Map<String,Object> extendConfig = createParam.getExtendConfig();
            if(extendConfig.containsKey("resources")){
                List<ResourceVO> resourceVOList = JsonUtil.toJavaObjectList(JsonUtil.toJSONString(extendConfig.get("resources")),ResourceVO.class);
                Validate.notNull(resourceVOList);
                for(ResourceVO resourceVO : resourceVOList){
                    Map<String,Object> extendMap = new HashMap<>();
                    extendMap.put("statId",resourceVO.getResourceId());
                    resultCode = orderService.submit(user,OrderTypeEnum.STAT_ACCESS,createParam.getReason(),extendMap);
                    if(resultCode != ResultCode.success){
                        break;
                    }
                }
            }
        }else{
            resultCode = orderService.submit(user,createParam.getOrderType(),createParam.getReason(),createParam.getExtendConfig());
        }
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
