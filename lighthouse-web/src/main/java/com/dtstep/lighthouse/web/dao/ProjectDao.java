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
import com.dtstep.lighthouse.common.entity.project.ProjectEntity;
import com.dtstep.lighthouse.core.builtin.BuiltinLoader;
import com.dtstep.lighthouse.core.dao.DaoHelper;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class ProjectDao {

    private static final Logger logger = LoggerFactory.getLogger(ProjectDao.class);

    @Cacheable(value = "PROJECT",key = "'queryById' + '_' + #projectId",cacheManager = "redisCacheManager",unless = "#result == null")
    public ProjectEntity queryById(int projectId) {
        if(BuiltinLoader.isBuiltinProject(projectId)){
            return BuiltinLoader.getBuiltinProject();
        }
        ProjectEntity projectEntity = null;
        try{
            projectEntity = DaoHelper.sql.getItem(ProjectEntity.class, "select * from ldp_stat_project where id = ?", projectId);
        }catch (Exception ex){
            logger.error("query project info error!",ex);
        }
        return projectEntity;
    }

    @Cacheable(value = "PROJECT",key="'queryAll'",cacheManager = "redisCacheManager",unless = "#result == null")
    public List<ProjectEntity> queryAll() throws Exception {
        return DaoHelper.sql.getList(ProjectEntity.class,"select * from ldp_stat_project order by create_time desc limit 10000");
    }

    public int save(ProjectEntity projectEntity) throws Exception {
        Date date = new Date();
        projectEntity.setCreateTime(date);
        projectEntity.setUpdateTime(date);
        return DaoHelper.sql.insert(projectEntity);
    }

    @Caching(evict = {
            @CacheEvict(value = "PROJECT",key = "'queryById' + '_' + #projectEntity.id",cacheManager = "redisCacheManager"),
            @CacheEvict(value = "PROJECT",key="'queryAll'",cacheManager = "redisCacheManager")})
    public void update(ProjectEntity projectEntity) throws Exception {
        Validate.notNull(projectEntity);
        projectEntity.setUpdateTime(new Date());
        DaoHelper.sql.execute("update ldp_stat_project set name= ?,department_id= ?,`desc`= ?,update_time= ?,private_flag = ? where id = ?", projectEntity.getName(),
                projectEntity.getDepartmentId(), projectEntity.getDesc(), projectEntity.getUpdateTime(),projectEntity.getPrivateFlag(), projectEntity.getId());
    }

    @Caching(evict = {
            @CacheEvict(value = "PROJECT",key = "'queryById' + '_' + #id",cacheManager = "redisCacheManager"),
            @CacheEvict(value = "PROJECT",key="'queryAll'",cacheManager = "redisCacheManager")})
    public void delete(int id) throws Exception {
        DaoHelper.sql.execute("delete from ldp_stat_project where id = ?", id);
    }
}
