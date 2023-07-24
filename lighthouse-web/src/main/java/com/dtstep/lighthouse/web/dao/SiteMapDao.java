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
import com.dtstep.lighthouse.common.entity.sitemap.SiteMapEntity;
import com.dtstep.lighthouse.core.dao.DaoHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class SiteMapDao {

    private static final Logger logger = LoggerFactory.getLogger(SiteMapDao.class);

    @Cacheable(value = "SITEMAP",key = "'queryById' + '_' + #siteId",cacheManager = "redisCacheManager",unless = "#result == null")
    public SiteMapEntity queryById(int siteId) throws Exception {
        SiteMapEntity siteMapEntity = null;
        try{
            siteMapEntity = DaoHelper.sql.getItem(SiteMapEntity.class,"select * from ldp_stat_sitemap where id = ?", siteId);
        }catch (Exception ex){
            logger.error("query sitemap error!",ex);
        }
        return siteMapEntity;
    }

    @CacheEvict(value = "SITEMAP",key = "'queryById' + '_' + #entity.id",cacheManager = "redisCacheManager")
    public void update(SiteMapEntity entity) throws Exception {
        entity.setUpdateTime(new Date());
        DaoHelper.sql.execute("update ldp_stat_sitemap set `name` = ?,`config` = ?,`star`= ?,`desc`= ?,`update_time`= ? where id = ?"
                ,entity.getName(),entity.getConfig(),entity.getStar(),entity.getDesc(),entity.getUpdateTime(),entity.getId());
    }

    @CacheEvict(value = "SITEMAP",key = "'queryById' + '_' + #id",cacheManager = "redisCacheManager")
    public void delete(int id) throws Exception {
        DaoHelper.sql.execute("delete from ldp_stat_sitemap where id = ?", id);
    }

    public int save(SiteMapEntity entity) throws Exception {
        Date date = new Date();
        entity.setCreateTime(date);
        entity.setUpdateTime(date);
        return DaoHelper.sql.insert(entity);
    }
}
