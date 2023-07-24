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
import com.dtstep.lighthouse.common.entity.stat.StatEntity;
import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.enums.display.DisplayTypeEnum;
import com.dtstep.lighthouse.common.enums.stat.StatStateEnum;
import com.dtstep.lighthouse.core.dao.DaoHelper;
import com.dtstep.lighthouse.core.wrapper.StatDBWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class StatDao {

    private static final Logger logger = LoggerFactory.getLogger(StatDao.class);

    @Cacheable(value = "STAT",key = "'queryById' + '_' + #id",cacheManager = "redisCacheManager",unless = "#result == null")
    public StatExtEntity queryById(int id) {
        Optional<StatExtEntity> optional = StatDBWrapper.actualQueryById(id);
        return optional.orElse(null);
    }

    @Cacheable(value = "STAT",key = "'queryListByGroupId' + '_' + #groupId",cacheManager = "redisCacheManager",unless = "#result == null")
    public List<StatExtEntity> queryListByGroupId(int groupId) throws Exception {
        return StatDBWrapper.actualQueryListByGroupId(groupId).orElse(null);
    }

    @CacheEvict(value = "STAT",key = "'queryById' + '_' + #statId",cacheManager = "redisCacheManager")
    public void changeState(int statId, StatStateEnum stateEnum) throws Exception {
        StatExtEntity statExtEntity = queryById(statId);
        StatDBWrapper.changeState(statExtEntity,stateEnum);
    }

    @Caching(evict = {
            @CacheEvict(value = "STAT",key = "'queryById' + '_' + #statExtEntity.id",cacheManager = "redisCacheManager"),
            @CacheEvict(value = "STAT",key = "'queryListByGroupId' + '_' + #statExtEntity.groupId",cacheManager = "redisCacheManager")})
    public void update(StatExtEntity statExtEntity) throws Exception {
        statExtEntity.setUpdateTime(new Date());
        DaoHelper.sql.execute("update ldp_stat_item set title = ? , template = ? ,data_expire = ?,update_time = ? where id = ?"
                , statExtEntity.getTitle(), statExtEntity.getTemplate(), statExtEntity.getDataExpire(), statExtEntity.getUpdateTime(), statExtEntity.getId());
    }

    @CacheEvict(value = "STAT",key = "'queryById' + '_' + #statId",cacheManager = "redisCacheManager")
    public void delete(int statId) throws Exception {
        DaoHelper.sql.execute("delete from ldp_stat_item where id = ?",statId);
    }

    @CacheEvict(value = "STAT",key = "'queryById' + '_' + #statId",cacheManager = "redisCacheManager")
    public void changeDisplayType(int statId, DisplayTypeEnum displayTypeEnum) throws Exception {
        DaoHelper.sql.execute("update ldp_stat_item set display_type = ?,update_time = ? where id = ?",displayTypeEnum.getDisplayType(),new Date(), statId);
    }

    @CacheEvict(value = "STAT",key = "'queryListByGroupId' + '_' + #groupId",cacheManager = "redisCacheManager")
    public List<Integer> saveList(int groupId,List<StatEntity> statList) throws Exception {
        return DaoHelper.sql.insertList(statList);
    }

    @CacheEvict(value = "STAT",key = "'queryById' + '_' + #statId",cacheManager = "redisCacheManager")
    public void changeFilterConfig(int statId, String filterConfig) throws Exception {
        DaoHelper.sql.execute("update ldp_stat_item set filter_config = ?,update_time = ? where id = ?",filterConfig,new Date(),statId);
    }
}
