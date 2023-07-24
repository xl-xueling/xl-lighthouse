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
import com.dtstep.lighthouse.common.entity.privilege.PrivilegeEntity;
import com.dtstep.lighthouse.common.entity.user.UserEntity;
import com.dtstep.lighthouse.common.enums.role.PrivilegeTypeEnum;
import com.dtstep.lighthouse.core.dao.DaoHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
public class PrivilegeDao {

    private static final Logger logger = LoggerFactory.getLogger(PrivilegeDao.class);

    @Autowired
    private UserDao userDao;

    @Cacheable(value = "PRIVILEGE",key = "'queryRelationIdsByUserId' + '_' + #userId + '_' + #privilegeTypeEnum.privilegeType",cacheManager = "redisCacheManager",unless = "#result == null")
    public List<Integer> queryRelationIdsByUserId(int userId, PrivilegeTypeEnum privilegeTypeEnum){
        List<Integer> resultList = null;
        try{
            List<PrivilegeEntity> roleList = DaoHelper.sql.getList(PrivilegeEntity.class,"select relation_b from ldp_privilege where relation_a = ? and privilege_type = ?",userId, privilegeTypeEnum.getPrivilegeType());
            if(CollectionUtils.isNotEmpty(roleList)){
                resultList = roleList.stream().map(PrivilegeEntity::getRelationB).collect(Collectors.toList());
            }
        }catch (Exception ex){
            logger.error("query privilege list info error!",ex);
        }
        return resultList;
    }

    public boolean checkRole(UserEntity userEntity,int relationB,PrivilegeTypeEnum privilegeTypeEnum){
        boolean hasRole = false;
        try{
            if(privilegeTypeEnum == PrivilegeTypeEnum.ADMIN){
                hasRole = userEntity.getUserName().equals("admin");
            }else{
                List<Integer> relationBList = queryRelationIdsByUserId(userEntity.getId(),privilegeTypeEnum);
                hasRole = CollectionUtils.isNotEmpty(relationBList) && relationBList.contains(relationB);
            }
        }catch (Exception ex){
            logger.error("check role error!",ex);
        }
        return hasRole;
    }

    @Cacheable(value = "PRIVILEGE",key = "'queryAdminsByRelationId' + '_' + #relationB + '_' + #privilegeTypeEnum.privilegeType",cacheManager = "redisCacheManager",unless = "#result == null")
    public List<UserEntity> queryAdminsByRelationId(int relationB, PrivilegeTypeEnum privilegeTypeEnum){
        List<UserEntity> userEntityList = null;
        try{
            if(privilegeTypeEnum == PrivilegeTypeEnum.ADMIN){
                userEntityList = DaoHelper.sql.getList(UserEntity.class,"select * from ldp_user where user_name = 'admin'");
            }else{
                List<PrivilegeEntity> privilegeList = DaoHelper.sql.getList(PrivilegeEntity.class,"select relation_a from ldp_privilege where relation_b = ? and privilege_type = ?",relationB, privilegeTypeEnum.getPrivilegeType());
                if(CollectionUtils.isNotEmpty(privilegeList)){
                    userEntityList = privilegeList.stream().map(x -> userDao.queryById(x.getRelationA())).filter(Objects::nonNull).collect(Collectors.toList());
                }
            }
        }catch (Exception ex){
            logger.error("query user list info error!",ex);
        }
        return userEntityList;
    }

    @Caching(evict = {
            @CacheEvict(value = "PRIVILEGE",key = "'queryRelationIdsByUserId' + '_' + #privilegeEntity.relationA + '_' + #privilegeEntity.privilegeType",cacheManager = "redisCacheManager"),
            @CacheEvict(value = "PRIVILEGE",key = "'queryAdminsByRelationId' + '_' + #privilegeEntity.relationB + '_' + #privilegeEntity.privilegeType",cacheManager = "redisCacheManager")})
    public void save(PrivilegeEntity privilegeEntity) throws Exception {
        DaoHelper.sql.execute("delete from ldp_privilege where relation_b = ? and privilege_type = ? and relation_a = ?", privilegeEntity.getRelationB()
                , privilegeEntity.getPrivilegeType(),privilegeEntity.getRelationA());
        DaoHelper.sql.insert(privilegeEntity);
    }

    public void removeUserPrivilege(int userId) throws Exception {
        Validate.isTrue(userId != 0);
        DaoHelper.sql.execute("delete from ldp_privilege where relation_a = ?",userId);
    }

    public void deleteByRelationId(int relationId, int privilegeType) throws Exception {
        DaoHelper.sql.execute("delete from ldp_privilege where relation_b = ? and privilege_type = ?",relationId,privilegeType);
    }

}
