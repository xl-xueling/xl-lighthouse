package com.dtstep.lighthouse.web.manager.group;
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
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.dtstep.lighthouse.common.constant.SysConst;
import com.dtstep.lighthouse.common.entity.group.GroupExtEntity;
import com.dtstep.lighthouse.common.enums.limiting.LimitingStrategyEnum;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.core.dao.DaoHelper;
import com.dtstep.lighthouse.web.dao.GroupDao;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class GroupManager {

    @Autowired
    private GroupDao groupDao;

    @Cacheable(value = "normal",key = "#targetClass + '_' + 'queryById' + '_' + #groupId",cacheManager = "caffeineCacheManager",unless = "#result == null")
    public GroupExtEntity queryById(int groupId) throws Exception {
        return groupDao.queryById(groupId);
    }

    public GroupExtEntity actualQueryById(int groupId) throws Exception {
        return groupDao.queryById(groupId);
    }

    public List<GroupExtEntity> queryListByProjectId(int projectId) throws Exception {
        return groupDao.queryListByProjectId(projectId);
    }

    public void updateThreshold(int groupId, ObjectNode objectNode) throws Exception {
        Validate.notNull(objectNode);
        GroupExtEntity groupEntity = groupDao.queryById(groupId);
        HashMap<String,Integer> paramMap = groupEntity.getLimitedThresholdMap();
        for(LimitingStrategyEnum limitingStrategyEnum : LimitingStrategyEnum.values()){
            String strategy = limitingStrategyEnum.getStrategy();
            if(objectNode.has(strategy)){
                paramMap.put(strategy, objectNode.get(strategy).asInt());
            }
        }
        groupDao.updateThreshold(groupId,JsonUtil.toJSONString(paramMap));
    }

    public boolean isExist(int id) throws Exception {
        return DaoHelper.sql.count("select count(1) from ldp_stat_group where id = ?", id) >= 1;
    }

    public int countByProjectId(int projectId) throws Exception {
        return DaoHelper.sql.count("select count(1) from ldp_stat_group where project_id = ?",projectId);
    }

    public void changeDebugMode(int groupId, int debugMode) throws Exception {
        String debugParams = null;
        if(debugMode == 1){
            ObjectNode objectNode = JsonUtil.createObjectNode();
            long startTime = System.currentTimeMillis();
            objectNode.put("startTime",startTime);
            objectNode.put("endTime",startTime + TimeUnit.MINUTES.toMillis(SysConst.DEBUG_MODE_DURATION_TIME));
            debugParams = objectNode.toString();
        }
        groupDao.changeDebugMode(groupId,debugMode,debugParams);
    }
}
