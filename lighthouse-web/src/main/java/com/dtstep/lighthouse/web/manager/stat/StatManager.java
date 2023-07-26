package com.dtstep.lighthouse.web.manager.stat;
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
import com.dtstep.lighthouse.web.manager.group.GroupManager;
import com.dtstep.lighthouse.web.manager.order.OrderManager;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.dtstep.lighthouse.common.entity.group.GroupExtEntity;
import com.dtstep.lighthouse.common.entity.order.OrderEntity;
import com.dtstep.lighthouse.common.entity.stat.StatEntity;
import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.enums.limiting.LimitingStrategyEnum;
import com.dtstep.lighthouse.common.enums.order.OrderTypeEnum;
import com.dtstep.lighthouse.common.enums.role.PrivilegeTypeEnum;
import com.dtstep.lighthouse.common.enums.stat.StatStateEnum;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.common.util.Md5Util;
import com.dtstep.lighthouse.core.config.LDPConfig;
import com.dtstep.lighthouse.core.dao.DaoHelper;
import com.dtstep.lighthouse.core.wrapper.StatDBWrapper;
import com.dtstep.lighthouse.web.dao.StatDao;
import com.dtstep.lighthouse.web.manager.meta.MetaTableManager;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatManager {

    @Autowired
    private MetaTableManager metaTableManager;

    @Autowired
    private OrderManager orderManager;

    @Autowired
    private GroupManager groupManager;

    @Autowired
    private StatDao statDao;

    @Cacheable(value = "normal",key = "#targetClass + '_' + 'queryById' + '_' + #statId",cacheManager = "caffeineCacheManager",unless = "#result == null")
    public StatExtEntity queryById(int statId) throws Exception {
        return statDao.queryById(statId);
    }

    public StatExtEntity actualQueryById(int statId) throws Exception {
        return statDao.queryById(statId);
    }

    public void changeState(int statId, StatStateEnum stateEnum) throws Exception {
        StatExtEntity statExtEntity = StatDBWrapper.queryById(statId);
        StatDBWrapper.changeState(statExtEntity,stateEnum);
    }

    public void changeFilterConfig(int statId,String filterConfig) throws Exception {
        statDao.changeFilterConfig(statId,filterConfig);
    }

    @Cacheable(value = "normal",key = "#targetClass + '_' + 'queryListByGroupId' + '_' + #groupId",cacheManager = "caffeineCacheManager",unless = "#result == null")
    public List<StatExtEntity> queryListByGroupId(int groupId) throws Exception{
        return statDao.queryListByGroupId(groupId);
    }

    @Caching(evict = {
            @CacheEvict(value = "normal",key = "#targetClass + '_' + 'queryById' + '_' + #statExtEntity.id",cacheManager = "caffeineCacheManager"),
            @CacheEvict(value = "normal",key = "#targetClass + '_' + 'queryListByGroupId' + '_' + #statExtEntity.groupId",cacheManager = "caffeineCacheManager")})
    public void update(StatExtEntity statExtEntity) throws Exception {
        statDao.update(statExtEntity);
    }

    public List<StatExtEntity> actualQueryListByGroupId(int groupId) throws Exception{
        return StatDBWrapper.actualQueryListByGroupId(groupId).orElse(null);
    }

    public void updateList(int userId, int groupId,List<StatEntity> statList) throws Exception {
        List<StatEntity> dbList = DaoHelper.sql.getList(StatEntity.class, "select * from ldp_stat_item where group_id = ?", groupId);
        if(CollectionUtils.isNotEmpty(dbList)){
            List<Integer> dbIdList = dbList.stream().map(StatEntity::getId).collect(Collectors.toList());
            statList = statList.stream().filter(x -> !dbIdList.contains(x.getId())).collect(Collectors.toList());
        }
        if(CollectionUtils.isEmpty(statList)){
            return;
        }
        GroupExtEntity groupExtEntity = groupManager.queryById(groupId);
        boolean needApprove = needApprove(groupExtEntity.getLimitedThresholdMap());
        Date date = new Date();
        for (StatEntity statEntity : statList){
            int metaId;
            if(statEntity.getSequenceFlag() == 1){
                metaId = metaTableManager.queryVolumeSeqResultTable();
            }else{
                metaId = metaTableManager.queryVolumeStatResultTable();
            }
            statEntity.setResMeta(metaId);
            statEntity.setCreateTime(date);
            statEntity.setUpdateTime(date);
            statEntity.setCreateUser(userId);
            if(needApprove){
                statEntity.setState(StatStateEnum.PENDING_APPROVE.getState());
            }else{
                statEntity.setState(StatStateEnum.RUNNING.getState());
            }
        }
        List<Integer> ids = statDao.saveList(groupId,statList);
        if(needApprove){
            List<OrderEntity> orderEntityList = new ArrayList<>();
            for(Integer statId : ids){
                OrderEntity orderEntity = new OrderEntity();
                orderEntity.setUserId(userId);
                orderEntity.setPrivilegeType(PrivilegeTypeEnum.ADMIN.getPrivilegeType());
                ObjectNode objectNode = JsonUtil.createObjectNode();
                objectNode.put("statId",statId);
                orderEntity.setParams(objectNode.toString());
                int orderType = OrderTypeEnum.STAT_ITEM_APPROVE.getType();
                orderEntity.setOrderType(orderType);
                String hash = Md5Util.getMD5(userId + "_" + orderType + "_" + statId);
                orderEntity.setHash(hash);
                orderEntityList.add(orderEntity);
            }
            orderManager.createOrders(orderEntityList);
        }
    }

    protected boolean needApprove(HashMap<String,Integer> thresholdMap) throws Exception {
        int approveGroupMessageThreshold = LDPConfig.getOrDefault(LDPConfig.KEY_LIMITED_GROUP_MESSAGE_SIZE_APPROVE_THRESHOLD,-1,Integer.class);
        int groupMessageThreshold;
        if(MapUtils.isNotEmpty(thresholdMap) && thresholdMap.containsKey(LimitingStrategyEnum.GROUP_MESSAGE_SIZE_LIMIT.getStrategy())){
            groupMessageThreshold = thresholdMap.get(LimitingStrategyEnum.GROUP_MESSAGE_SIZE_LIMIT.getStrategy());
        }else {
            groupMessageThreshold = LDPConfig.getOrDefault(LDPConfig.KEY_LIMITED_GROUP_MESSAGE_SIZE_PER_SEC,-1,Integer.class);
        }
        if (approveGroupMessageThreshold != -1 && groupMessageThreshold != -1 && groupMessageThreshold >= approveGroupMessageThreshold){
            return true;
        }
        int approveStatResultsThreshold = LDPConfig.getOrDefault(LDPConfig.KEY_LIMITED_STAT_RESULT_SIZE_APPROVE_THRESHOLD,-1,Integer.class);
        int statResultsThreshold;
        if(MapUtils.isNotEmpty(thresholdMap) && thresholdMap.containsKey(LimitingStrategyEnum.STAT_RESULT_SIZE_LIMIT.getStrategy())){
            statResultsThreshold = thresholdMap.get(LimitingStrategyEnum.STAT_RESULT_SIZE_LIMIT.getStrategy());
        }else {
            statResultsThreshold = LDPConfig.getOrDefault(LDPConfig.KEY_LIMITED_STAT_RESULT_SIZE_PER_SEC,-1,Integer.class);
        }
        return approveStatResultsThreshold != -1 && statResultsThreshold != -1 && statResultsThreshold >= approveStatResultsThreshold;
    }

    public int countByGroupId(int groupId) throws Exception {
        return DaoHelper.sql.count("select count(1) from ldp_stat_item where group_id = ?",groupId);
    }

    public int countWithDuration(long startTime, long endTime) throws Exception {
        return DaoHelper.sql.count("select count(1) from ldp_stat_item where create_time > ? and create_time < ?",new Date(startTime),new Date(endTime));
    }

    public int totalCount() throws Exception {
        return DaoHelper.sql.count("select count(1) from ldp_stat_item");
    }

    public int countByProjectId(int projectId) throws Exception {
        return DaoHelper.sql.count("select count(1) from ldp_stat_item where project_id = ?",projectId);
    }

    public boolean isExist(int statId) throws Exception{
        return DaoHelper.sql.count("select count(1) from ldp_stat_item where id = ?",statId) == 1;
    }
}
