package com.dtstep.lighthouse.web.manager.user;
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
import com.dtstep.lighthouse.common.enums.role.PrivilegeTypeEnum;
import com.dtstep.lighthouse.common.enums.user.UserStateEnum;
import com.dtstep.lighthouse.common.util.StringUtil;
import com.dtstep.lighthouse.core.dao.ConnectionManager;
import com.dtstep.lighthouse.core.dao.DBConnection;
import com.dtstep.lighthouse.core.dao.DaoHelper;
import com.dtstep.lighthouse.web.manager.privilege.PrivilegeManager;
import com.dtstep.lighthouse.web.dao.UserDao;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class UserManager {

    @Autowired
    private PrivilegeManager privilegeManager;

    @Autowired
    private UserDao userDao;

    @Cacheable(value = "normal",key = "#targetClass + '_' + 'queryById' + '_' + #userId",cacheManager = "caffeineCacheManager",unless = "#result == null")
    public UserEntity queryById(int userId) throws Exception {
        return userDao.queryById(userId);
    }

    public UserEntity queryByUserName(String userName) throws Exception {
        return DaoHelper.sql.getItem(UserEntity.class, "select * from ldp_user where user_name = ?", userName);
    }

    public int createAdmin(UserEntity userEntity) throws Exception {
        int userId;
        DBConnection dbConnection = ConnectionManager.getConnection();
        ConnectionManager.beginTransaction(dbConnection);
        try{
            userId = userDao.saveUser(userEntity);
            privilegeManager.createPrivilege(PrivilegeTypeEnum.ADMIN,0,userId);
            ConnectionManager.commitTransaction(dbConnection);
        }catch (Exception ex){
            ConnectionManager.rollbackTransaction(dbConnection);
            throw ex;
        }finally {
            ConnectionManager.close(dbConnection);
        }
        return userId;
    }

    public void refreshLoginTime(int userId) throws Exception {
        userDao.refreshLoginTime(userId);
    }

    public boolean check(String userName, String password) throws Exception {
        Validate.notNull(userName);
        Validate.notNull(password);
        return StringUtil.isMD5(password) && DaoHelper.sql.count("select count(1) from ldp_user where binary user_name = ? and binary password = ?", userName, password) == 1;
    }

    public int countByDepartmentId(int departId) throws Exception {
        return DaoHelper.sql.count("select count(1) from ldp_user where department_id = ?",departId);
    }

    public int countByState(UserStateEnum stateEnum) throws Exception {
        Validate.notNull(stateEnum);
        return DaoHelper.sql.count("select count(1) from ldp_user where state = ?",stateEnum.getState());
    }

}
