package com.dtstep.lighthouse.web.dao;
/*
 * Copyright (C) 2022-2023 XueLing.雪灵
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
import com.dtstep.lighthouse.common.entity.order.OrderEntity;
import com.dtstep.lighthouse.common.enums.order.OrderStateEnum;
import com.dtstep.lighthouse.core.dao.DaoHelper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class OrderDao {

    @Cacheable(value = "ORDER",key = "'queryById' + '_' + #orderId",cacheManager = "redisCacheManager",unless = "#result == null")
    public OrderEntity queryById(int orderId) throws Exception {
        return DaoHelper.sql.getItem(OrderEntity.class,"select * from ldp_order where id = ?",orderId);
    }

    public void save(OrderEntity orderEntity) throws Exception {
        Date date = new Date();
        orderEntity.setCreateTime(date);
        orderEntity.setState(OrderStateEnum.PEND.getState());
        DaoHelper.sql.insert(orderEntity);
    }

    public void delete(String hash,OrderStateEnum stateEnum) throws Exception {
        DaoHelper.sql.execute("delete from ldp_order where hash = ? and state = ?",hash, stateEnum.getState());
    }

    @CacheEvict(value = "ORDER",key = "'queryById' + '_' + #id",cacheManager = "redisCacheManager")
    public void delete(int id) throws Exception {
        DaoHelper.sql.execute("delete from ldp_order where id = ?",id);
    }

    @CacheEvict(value = "ORDER",key = "'queryById' + '_' + #orderId",cacheManager = "redisCacheManager")
    public void changeState(int orderId,int approveUserId,OrderStateEnum orderStateEnum) throws Exception {
        DaoHelper.sql.execute("update ldp_order set state = ?,approve_user = ?,process_time = ? where id = ?",orderStateEnum.getState(),approveUserId,new Date(),orderId);
    }
}
