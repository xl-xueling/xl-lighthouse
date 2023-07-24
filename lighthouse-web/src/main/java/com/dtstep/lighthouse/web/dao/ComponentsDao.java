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
import com.dtstep.lighthouse.common.entity.components.ComponentsEntity;
import com.dtstep.lighthouse.core.dao.DaoHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class ComponentsDao {

    private static final Logger logger = LoggerFactory.getLogger(ComponentsDao.class);

    @Cacheable(value = "COMPONENT",key = "'queryById' + '_' + #id",cacheManager = "redisCacheManager",unless = "#result == null")
    public ComponentsEntity queryById(int id){
        ComponentsEntity componentsEntity = null;
        try{
            componentsEntity = DaoHelper.sql.getItem(ComponentsEntity.class, "select * from ldp_components where id = ?", id);
        }catch (Exception ex){
            logger.error("query components[id:{}] error!",id,ex);
        }
        return componentsEntity;
    }

    @CacheEvict(value = "COMPONENT",key = "'queryById' + '_' + #componentsId",cacheManager = "redisCacheManager")
    public void delete(int componentsId) throws Exception {
        DaoHelper.sql.execute("delete from ldp_components where id = ?",componentsId);
    }

    @CacheEvict(value = "COMPONENT",key = "'queryById' + '_' + #componentsEntity.id",cacheManager = "redisCacheManager")
    public void update(ComponentsEntity componentsEntity) throws Exception{
        DaoHelper.sql.execute("update ldp_components set private_flag = ?,data = ?,title = ?,components_type = ? where id = ?",componentsEntity.getPrivateFlag(),componentsEntity.getData(),componentsEntity.getTitle()
                ,componentsEntity.getType(),componentsEntity.getId());
    }

    public void save(ComponentsEntity componentsEntity) throws Exception {
        componentsEntity.setCreateTime(new Date());
        DaoHelper.sql.insert(componentsEntity);
    }
}
