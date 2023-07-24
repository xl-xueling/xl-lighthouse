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
import com.dtstep.lighthouse.common.entity.user.UserEntity;
import com.dtstep.lighthouse.common.enums.user.UserStateEnum;
import com.dtstep.lighthouse.core.dao.DaoHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class UserDao {

    private static final Logger logger = LoggerFactory.getLogger(UserDao.class);

    @Cacheable(value = "USER",key = "'queryById' + '_' + #userId",cacheManager = "redisCacheManager",unless = "#result == null")
    public UserEntity queryById(int userId) {
        UserEntity userEntity = null;
        try{
            userEntity = DaoHelper.sql.getItem(UserEntity.class, "select * from ldp_user where id = ?", userId);
        }catch (Exception ex){
            logger.error("query user[id:{}] info error!",userId,ex);
        }
        return userEntity;
    }

    public int saveUser(UserEntity userEntity) throws Exception {
        return DaoHelper.sql.insert(userEntity);
    }

    @CacheEvict(value = "USER",key = "'queryById' + '_' + #userId",cacheManager = "redisCacheManager")
    public void delete(int userId) throws Exception {
        DaoHelper.sql.execute("delete from ldp_user where id = ?", userId);
    }

    @CacheEvict(value = "USER",key = "'queryById' + '_' + #userEntity.id",cacheManager = "redisCacheManager")
    public void update(UserEntity userEntity) throws Exception {
        DaoHelper.sql.execute("update ldp_user set user_name = ?,email=?,department_id = ?,phone = ? where id = ?", userEntity.getUserName(), userEntity.getEmail(), userEntity.getDepartmentId(), userEntity.getPhone(), userEntity.getId());
    }

    @CacheEvict(value = "USER",key = "'queryById' + '_' + #userId",cacheManager = "redisCacheManager")
    public void changeState(int userId, UserStateEnum userStateEnum) throws Exception {
        DaoHelper.sql.execute("update ldp_user set state = ? where id = ?", userStateEnum.getState(), userId);
    }

    @CacheEvict(value = "USER",key = "'queryById' + '_' + #userId",cacheManager = "redisCacheManager")
    public void changePassword(int userId, String password) throws Exception {
        DaoHelper.sql.execute("update ldp_user set password = ? where id = ?", password, userId);
    }

    @CacheEvict(value = "USER",key = "'queryById' + '_' + #userId",cacheManager = "redisCacheManager")
    public void refreshLoginTime(int userId) throws Exception {
        DaoHelper.sql.execute("update ldp_user set last_time = ? where id = ?", new Date(), userId);
    }

}
