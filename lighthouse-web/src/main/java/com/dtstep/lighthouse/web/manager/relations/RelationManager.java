package com.dtstep.lighthouse.web.manager.relations;
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
import com.dtstep.lighthouse.common.enums.relations.RelationTypeEnum;
import com.dtstep.lighthouse.web.dao.RelationDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RelationManager {

    private static final Logger logger = LoggerFactory.getLogger(RelationManager.class);

    @Autowired
    private RelationDao relationDao;

    @Cacheable(value = "short",key = "#targetClass + '_' + 'queryListByUserId' + '_' + #userId + '_' + #relationTypeEnum.type",cacheManager = "caffeineCacheManager")
    public List<Integer> queryListByUserId(int userId, RelationTypeEnum relationTypeEnum){
        return relationDao.queryRelationIdsByUserId(userId, relationTypeEnum);
    }

    public List<Integer> actualQueryListByUserId(int userId, RelationTypeEnum relationTypeEnum){
        return relationDao.queryRelationIdsByUserId(userId, relationTypeEnum);
    }

    public boolean existRelation(int userId,int relationId,RelationTypeEnum relationTypeEnum) throws Exception {
        return relationDao.existRelation(userId,relationId,relationTypeEnum);
    }

    @CacheEvict(value = "short",key = "#targetClass + '_' + 'queryListByUserId' + '_' + #userId + '_' + #relationTypeEnum.type",cacheManager = "caffeineCacheManager")
    public void delete(int userId, int relationId, RelationTypeEnum relationTypeEnum) throws Exception {
        relationDao.delete(userId,relationId,relationTypeEnum);
    }

    public void delete(int relationId, RelationTypeEnum relationTypeEnum) throws Exception {
        relationDao.delete(relationId,relationTypeEnum);
    }
}
