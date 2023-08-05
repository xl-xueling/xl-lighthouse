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
import com.dtstep.lighthouse.common.entity.group.GroupEntity;
import com.dtstep.lighthouse.common.entity.group.GroupExtEntity;
import com.dtstep.lighthouse.common.enums.stat.GroupStateEnum;
import com.dtstep.lighthouse.core.dao.DaoHelper;
import com.dtstep.lighthouse.core.wrapper.GroupDBWrapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class GroupDao {

    private static final Logger logger = LoggerFactory.getLogger(GroupDao.class);

    @Cacheable(value = "GROUP",key = "'queryById' + '_' + #id",cacheManager = "redisCacheManager",unless = "#result.id != #id")
    public GroupExtEntity queryById(int id) throws Exception {
        Optional<GroupExtEntity> optional = GroupDBWrapper.actualQueryGroupById(id);
        return optional.orElse(null);
    }

    @Cacheable(value = "GROUP",key = "'queryListByProjectId' + '_' + #projectId",cacheManager = "redisCacheManager",unless = "#result == null")
    public List<GroupExtEntity> queryListByProjectId(int projectId) throws Exception {
        List<Integer> groupIdList = DaoHelper.sql.getList(Integer.class,"select id from ldp_stat_group where project_id = ? ",projectId);
        if(CollectionUtils.isEmpty(groupIdList)){
            return null;
        }
        return groupIdList.stream().map(x -> {
            GroupExtEntity groupExtEntity = null;
            try{
                groupExtEntity = GroupDBWrapper.actualQueryGroupById(x).orElse(null);
            }catch (Exception ex){
                ex.printStackTrace();
            }
            return groupExtEntity;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    @CacheEvict(value = "GROUP",key = "'queryListByProjectId' + '_' + #groupEntity.projectId",cacheManager = "redisCacheManager")
    public int save(GroupEntity groupEntity) throws Exception {
        Date date = new Date();
        groupEntity.setCreateTime(date);
        groupEntity.setUpdateTime(date);
        groupEntity.setRefreshTime(date);
        groupEntity.setSecretKey(RandomStringUtils.randomAlphanumeric(32));
        groupEntity.setState(GroupStateEnum.RUNNING.getState());
        return DaoHelper.sql.insert(groupEntity);
    }

    @CacheEvict(value = "GROUP",key = "'queryById' + '_' + #groupEntity.id",cacheManager = "redisCacheManager")
    public void update(GroupEntity groupEntity) throws Exception {
        Date date = new Date();
        groupEntity.setUpdateTime(date);
        groupEntity.setRefreshTime(date);
        DaoHelper.sql.execute("update ldp_stat_group set columns = ?,remark = ?,update_time = ?,refresh_time = ? where id = ?", groupEntity.getColumns()
                , groupEntity.getRemark(),groupEntity.getUpdateTime(), groupEntity.getRefreshTime(), groupEntity.getId());
    }

    @CacheEvict(value = "GROUP",key = "'queryById' + '_' + #groupId",cacheManager = "redisCacheManager")
    public void updateThreshold(int groupId,String thresholdConfig) throws Exception {
        Date date = new Date();
        DaoHelper.sql.execute("update ldp_stat_group set limited_threshold = ?,update_time = ?,refresh_time = ? where id = ?", thresholdConfig,date,date,groupId);
    }

    public void refresh(int groupId) throws Exception{
        DaoHelper.sql.execute("update ldp_stat_group set refresh_time = ? where id = ?", new Date(),groupId);
    }

    @Caching(evict = {
            @CacheEvict(value = "GROUP",key = "'queryById' + '_' + #groupExtEntity.id",cacheManager = "redisCacheManager"),
            @CacheEvict(value = "GROUP",key = "'queryListByProjectId' + '_' + #groupExtEntity.projectId",cacheManager = "redisCacheManager")})
    public void delete(GroupExtEntity groupExtEntity) throws Exception {
        DaoHelper.sql.execute("delete from ldp_stat_group where id = ?", groupExtEntity.getId());
    }

    @CacheEvict(value = "GROUP",key = "'queryById' + '_' + #groupId",cacheManager = "redisCacheManager")
    public void changeDebugMode(int groupId, int debugMode,String debugParams) throws Exception {
        Date date = new Date();
        DaoHelper.sql.execute("update ldp_stat_group set debug_mode = ?,update_time = ?,refresh_time = ?,debug_params = ? where id = ?",debugMode,date,date,debugParams,groupId);
    }

}
