package com.dtstep.lighthouse.web.manager.order;
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
import com.dtstep.lighthouse.web.manager.project.ProjectManager;
import com.fasterxml.jackson.databind.JsonNode;
import com.dtstep.lighthouse.common.entity.group.GroupExtEntity;
import com.dtstep.lighthouse.common.entity.order.OrderEntity;
import com.dtstep.lighthouse.common.entity.project.ProjectEntity;
import com.dtstep.lighthouse.common.entity.sitemap.SiteMapEntity;
import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.enums.order.OrderStateEnum;
import com.dtstep.lighthouse.common.enums.order.OrderTypeEnum;
import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.common.util.StringUtil;
import com.dtstep.lighthouse.core.dao.ConnectionManager;
import com.dtstep.lighthouse.core.dao.DBConnection;
import com.dtstep.lighthouse.core.dao.DaoHelper;
import com.dtstep.lighthouse.web.dao.OrderDao;
import com.dtstep.lighthouse.web.manager.group.GroupManager;
import com.dtstep.lighthouse.web.manager.stat.StatManager;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OrderManager {

    private static final Logger logger = LoggerFactory.getLogger(OrderManager.class);

    @Autowired
    private ProjectManager projectManager;

    @Autowired
    private StatManager statManager;

    @Autowired
    private GroupManager groupManager;

    @Autowired
    private OrderDao orderDao;

    public boolean isApply(String hash) throws Exception {
        long startTime = DateUtil.getDayBefore(System.currentTimeMillis(),7);
        int count =  DaoHelper.sql.count("select count(1) from ldp_order where `hash` = ? and `state` = ? and create_time > ?",hash, OrderStateEnum.PEND.getState(),new Date(startTime));
        return count >= 1;
    }

    @Cacheable(value = "normal",key = "#targetClass + '_' + 'getDescription' + '_' + #orderEntity.id",cacheManager = "caffeineCacheManager",unless = "#result == null")
    public String getDescription(OrderEntity orderEntity) throws Exception {
        String params = orderEntity.getParams();
        OrderTypeEnum orderTypeEnum = OrderTypeEnum.getOrderType(orderEntity.getOrderType());
        String desc = null;
        JsonNode paramNode = JsonUtil.readTree(params);
        if(orderTypeEnum == OrderTypeEnum.PROJECT_ACCESS){
            int projectId = paramNode.get("projectId").asInt();
            ProjectEntity projectEntity = projectManager.queryById(projectId);
            if(projectEntity != null){
                String link = String.format("/display/project.shtml?projectId=%s",projectEntity.getId());
                desc = String.format("i18n(ldp_i18n_privilege_apply_1009,%s,%s)",link,projectEntity.getName());
            }
        }else if(orderTypeEnum == OrderTypeEnum.STAT_ACCESS){
            int statId = paramNode.get("statId").asInt();
            StatExtEntity statExtEntity = statManager.queryById(statId);
            if(statExtEntity != null){
                Validate.notNull(statExtEntity);
                ProjectEntity projectEntity = projectManager.queryById(statExtEntity.getProjectId());
                if(projectEntity != null){
                    String link = String.format("/display/stat.shtml?statId=%s", statExtEntity.getId());
                    desc = String.format("i18n(ldp_i18n_privilege_apply_1010,%s,%s,%s,%s)",link,projectEntity.getName(), statExtEntity.getToken(), statExtEntity.getTitle());
                }
            }
        }else if(orderTypeEnum == OrderTypeEnum.STAT_ITEM_APPROVE){
            int statId = paramNode.get("statId").asInt();
            StatExtEntity statExtEntity = statManager.queryById(statId);
            if(statExtEntity != null){
                ProjectEntity projectEntity = projectManager.queryById(statExtEntity.getProjectId());
                if(projectEntity != null){
                    String statLink = String.format("/display/stat.shtml?statId=%s", statExtEntity.getId());
                    desc =String.format("i18n(ldp_i18n_order_apply_1012,%s,%s,%s,%s)",statLink, projectEntity.getName(),statExtEntity.getToken(),statExtEntity.getTitle());
                }
            }
        }else if(orderTypeEnum == OrderTypeEnum.GROUP_THRESHOLD_ADJUST){
            int groupId = paramNode.get("groupId").asInt();
            String strategy = paramNode.get("strategy").asText();
            String newValue = paramNode.get("newValue").asText();
            GroupExtEntity groupExtEntity = groupManager.queryById(groupId);
            if(groupExtEntity != null){
                ProjectEntity projectEntity = projectManager.queryById(groupExtEntity.getProjectId());
                if(projectEntity != null){
                    String link = String.format("/display/project.shtml?projectId=%s&groupId=%s",groupExtEntity.getProjectId(),groupExtEntity.getId());
                    desc = String.format("i18n(ldp_i18n_order_apply_1013,%s,%s,%s,%s,%s)",link, projectEntity.getName(),groupExtEntity.getToken(),strategy,newValue);
                }
            }
        }
        if(StringUtil.isEmpty(desc)){
            desc = "i18n(ldp_i18n_privilege_apply_1012)";
        }
        return desc;
    }

    public OrderEntity queryById(int orderId) throws Exception {
        return orderDao.queryById(orderId);
    }

    public void createOrder(OrderEntity orderEntity) throws Exception {
        Validate.notNull(orderEntity);
        DBConnection dbConnection = ConnectionManager.getConnection();
        ConnectionManager.beginTransaction(dbConnection);
        try{
            orderDao.delete(orderEntity.getHash(),OrderStateEnum.PEND);
            orderDao.save(orderEntity);
            ConnectionManager.commitTransaction(dbConnection);
        }catch (Exception ex){
            logger.error("create order error!",ex);
            ConnectionManager.rollbackTransaction(dbConnection);
            throw ex;
        }finally {
            ConnectionManager.close(dbConnection);
        }
    }

    public void createOrders(List<OrderEntity> orderEntityList) throws Exception {
        Validate.notNull(orderEntityList);
        DBConnection dbConnection = ConnectionManager.getConnection();
        ConnectionManager.beginTransaction(dbConnection);
        try{
            for(OrderEntity orderEntity : orderEntityList){
                orderDao.delete(orderEntity.getHash(),OrderStateEnum.PEND);
                orderDao.save(orderEntity);
            }
            ConnectionManager.commitTransaction(dbConnection);
        }catch (Exception ex){
            logger.error("create order error!",ex);
            ConnectionManager.rollbackTransaction(dbConnection);
            throw ex;
        }finally {
            ConnectionManager.close(dbConnection);
        }
    }

    public void changeState(int id,int approveUserId,OrderStateEnum orderStateEnum) throws Exception {
        orderDao.changeState(id,approveUserId,orderStateEnum);
    }
}
