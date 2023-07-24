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
import com.dtstep.lighthouse.common.entity.relations.RelationEntity;
import com.dtstep.lighthouse.common.enums.relations.RelationTypeEnum;
import com.dtstep.lighthouse.common.util.Md5Util;
import com.dtstep.lighthouse.core.dao.DaoHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class RelationDao {

    private static final Logger logger = LoggerFactory.getLogger(RelationDao.class);

    @Cacheable(value = "RELATION",key = "'queryRelationIdsByUserId' + '_' + #userId + '_' + #relationTypeEnum.type",cacheManager = "redisCacheManager",unless = "#result == null")
    public List<Integer> queryRelationIdsByUserId(int userId, RelationTypeEnum relationTypeEnum){
        try{
            return DaoHelper.sql.getList(Integer.class,"select relation_b from ldp_relations where relation_a = ? and relation_type = ?",userId, relationTypeEnum.getType());
        }catch (Exception ex){
            logger.error("query relations list error!",ex);
        }
        return null;
    }

    public boolean existRelation(int userId,int relationId,RelationTypeEnum relationTypeEnum) throws Exception {
        Validate.notNull(relationTypeEnum);
        List<Integer> lists = queryRelationIdsByUserId(userId,relationTypeEnum);
        return CollectionUtils.isNotEmpty(lists) && lists.contains(relationId);
    }

    @CacheEvict(value = "RELATION",key = "'queryRelationIdsByUserId' + '_' + #userId + '_' + #relationTypeEnum.type",cacheManager = "redisCacheManager")
    public void delete(int userId, int relationId, RelationTypeEnum relationTypeEnum) throws Exception {
        String hash = Md5Util.getMD5(String.format("%s_%s_%s",userId,relationId,relationTypeEnum.getType()));
        DaoHelper.sql.execute("delete from ldp_relations where hash = ?",hash);
    }

    public void delete(int relationId, RelationTypeEnum relationTypeEnum) throws Exception {
        DaoHelper.sql.execute("delete from ldp_relations where relation_b = ? and relation_type = ?",relationId,relationTypeEnum.getType());
    }

    @CacheEvict(value = "RELATION",key = "'queryRelationIdsByUserId' + '_' + #relationEntity.relationA + '_' + #relationEntity.relationType",cacheManager = "redisCacheManager")
    public void save(RelationEntity relationEntity) throws Exception {
        relationEntity.setCreateTime(new Date());
        String hash = Md5Util.getMD5(String.format("%s_%s_%s", relationEntity.getRelationA(), relationEntity.getRelationB(), relationEntity.getRelationType()));
        relationEntity.setHash(hash);
        DaoHelper.sql.insert(relationEntity);
    }
}
