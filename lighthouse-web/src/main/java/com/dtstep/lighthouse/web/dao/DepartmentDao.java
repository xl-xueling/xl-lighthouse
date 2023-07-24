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
import com.dtstep.lighthouse.common.entity.department.DepartmentEntity;
import com.dtstep.lighthouse.core.dao.DaoHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;

@Repository
public class DepartmentDao {

    private static final Logger logger = LoggerFactory.getLogger(DepartmentDao.class);

    @Cacheable(value = "DEPARTMENT",key = "'queryById' + '_' + #id",cacheManager = "redisCacheManager",unless = "#result == null")
    public DepartmentEntity queryById(int id) {
        DepartmentEntity departmentEntity = null;
        try{
            departmentEntity = DaoHelper.sql.getItem(DepartmentEntity.class, "select * from ldp_department where id = ?", id);
        }catch (Exception ex){
            logger.error("query department[id:{}] error!",id,ex);
        }
        return departmentEntity;
    }

    @Cacheable(value = "DEPARTMENT",key="'queryAll'",cacheManager = "redisCacheManager",unless = "#result == null")
    public List<DepartmentEntity> queryAll(){
        List<DepartmentEntity> allList = null;
        try{
            allList = DaoHelper.sql.getList(DepartmentEntity.class, "select * from ldp_department order by pid asc,id asc limit 5000");
        }catch (Exception ex){
            logger.error("query department list error!",ex);
        }
        return allList;
    }


    @Caching(evict = {
            @CacheEvict(value = "DEPARTMENT",key = "'queryById' + '_' + #id",cacheManager = "redisCacheManager"),
            @CacheEvict(value = "DEPARTMENT",key="'queryAll'",cacheManager = "redisCacheManager")})
    public void delete(int id) throws Exception {
        DaoHelper.sql.execute("delete from ldp_department where id = ?", id);
    }


    @Caching(evict = {
            @CacheEvict(value = "DEPARTMENT",key = "'queryById' + '_' + #departmentEntity.id",cacheManager = "redisCacheManager"),
            @CacheEvict(value = "DEPARTMENT",key="'queryAll'",cacheManager = "redisCacheManager")})
    public void update(DepartmentEntity departmentEntity) throws Exception {
        DaoHelper.sql.execute("update ldp_department set name = ? ,update_time = ? where id = ?", departmentEntity.getName(),new Date(), departmentEntity.getId());
    }

    public int save(DepartmentEntity departmentEntity) throws Exception {
        Date date = new Date();
        departmentEntity.setCreateTime(date);
        departmentEntity.setUpdateTime(date);
        return DaoHelper.sql.insert(departmentEntity);
    }

    @Caching(evict = {
            @CacheEvict(value = "DEPARTMENT",key = "'queryById' + '_' + #id",cacheManager = "redisCacheManager"),
            @CacheEvict(value = "DEPARTMENT",key="'queryAll'",cacheManager = "redisCacheManager")})
    public void updateFullPath(String fullPath,int id) throws Exception {
        DaoHelper.sql.execute("update ldp_department set fullpath = ? where id = ?",fullPath,id);
    }

}
