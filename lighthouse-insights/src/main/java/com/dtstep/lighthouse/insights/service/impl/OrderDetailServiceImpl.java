package com.dtstep.lighthouse.insights.service.impl;
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
import com.dtstep.lighthouse.insights.dao.OrderDetailDao;
import com.dtstep.lighthouse.insights.vo.OrderDetailVO;
import com.dtstep.lighthouse.common.modal.OrderDetail;
import com.dtstep.lighthouse.common.modal.User;
import com.dtstep.lighthouse.insights.service.OrderDetailService;
import com.dtstep.lighthouse.insights.service.UserService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {

    @Autowired
    private OrderDetailDao orderDetailDao;

    @Autowired
    private UserService userService;

    @Override
    public List<OrderDetailVO> queryList(Integer orderId) {
        List<OrderDetail> orderDetails = orderDetailDao.queryList(orderId);
        List<OrderDetailVO> dtoList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(orderDetails)){
            for(OrderDetail orderDetail : orderDetails){
                OrderDetailVO orderDetailVO = new OrderDetailVO(orderDetail);
                Integer userId = orderDetail.getUserId();
                if(userId != null){
                    User user = userService.cacheQueryById(userId);
                    orderDetailVO.setUser(user);
                }
                dtoList.add(orderDetailVO);
            }
        }
        return dtoList;
    }
}
