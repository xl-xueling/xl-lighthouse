package com.dtstep.lighthouse.web.manager.privilege;
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
import com.dtstep.lighthouse.common.constant.SysConst;
import com.dtstep.lighthouse.common.entity.privilege.PrivilegeEntity;
import com.dtstep.lighthouse.common.entity.project.ProjectEntity;
import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.entity.user.UserEntity;
import com.dtstep.lighthouse.common.enums.role.PrivilegeTypeEnum;
import com.dtstep.lighthouse.core.builtin.BuiltinLoader;
import com.dtstep.lighthouse.core.dao.ConnectionManager;
import com.dtstep.lighthouse.core.dao.DBConnection;
import com.dtstep.lighthouse.web.dao.PrivilegeDao;
import com.dtstep.lighthouse.web.manager.project.ProjectManager;
import com.dtstep.lighthouse.web.manager.stat.StatManager;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class PrivilegeManager {

    @Autowired
    private StatManager statManager;

    @Autowired
    private ProjectManager projectManager;

    @Autowired
    private PrivilegeDao privilegeDao;

    public boolean isSysAdmin(UserEntity userEntity) {
        return userEntity != null && userEntity.getUserName().equals(SysConst._ADMIN_USER_NAME);
    }

    public void removeUserPrivilege(int userId) throws Exception {
        privilegeDao.removeUserPrivilege(userId);
    }

    @Cacheable(value = "short",key = "#targetClass + '_' + 'queryAdmins' + '_' + #relationB + '_' + #roleType",cacheManager = "caffeineCacheManager",unless = "#result == null")
    public List<UserEntity> queryAdmins(int relationB, int roleType) {
        return privilegeDao.queryAdminsByRelationId(relationB, Objects.requireNonNull(PrivilegeTypeEnum.getPrivilegeTypeEnum(roleType)));
    }

    @Cacheable(value = "short",key = "#targetClass + '_' + 'queryRelationIdsByUserId' + '_' + #userId + '_' + #privilegeTypeEnum.privilegeType",cacheManager = "caffeineCacheManager",unless = "#result == null")
    public List<Integer> queryRelationIdsByUserId(int userId,PrivilegeTypeEnum privilegeTypeEnum){
        return privilegeDao.queryRelationIdsByUserId(userId, privilegeTypeEnum);
    }

    @Cacheable(value = "short",key = "#targetClass + '_' + 'hasRole' + '_' + #userEntity.id + '_' + #relationB + '_' + #privilegeType",cacheManager = "caffeineCacheManager")
    public boolean hasRole(UserEntity userEntity, int relationB, int privilegeType) throws Exception {
        if(relationB == -1){
            return false;
        }
        if(userEntity.getUserName().equals(SysConst._ADMIN_USER_NAME)){
            return true;
        }
        if(privilegeType == PrivilegeTypeEnum.STAT_ITEM_USER.getPrivilegeType()){
            StatExtEntity statExtEntity = statManager.queryById(relationB);
            if(statExtEntity == null){
                return false;
            }
            if(BuiltinLoader.isBuiltinStat(statExtEntity.getId())){
                return true;
            }
            int projectId = statExtEntity.getProjectId();
            ProjectEntity projectEntity = projectManager.queryById(projectId);
            if(projectEntity != null && projectEntity.getPrivateFlag() == 0){
                return true;
            }
            if(privilegeDao.checkRole(userEntity,projectId,PrivilegeTypeEnum.STAT_PROJECT_ADMIN)){
                return true;
            }
            if(privilegeDao.checkRole(userEntity,projectId,PrivilegeTypeEnum.STAT_PROJECT_USER)){
                return true;
            }
            return privilegeDao.checkRole(userEntity,relationB,PrivilegeTypeEnum.STAT_ITEM_USER);
        }else if(privilegeType == PrivilegeTypeEnum.STAT_PROJECT_USER.getPrivilegeType()){
            ProjectEntity projectEntity = projectManager.queryById(relationB);
            if(projectEntity == null){
                return false;
            }
            if(projectEntity.getPrivateFlag() == 0){
                return true;
            }
            if(checkRole(userEntity,relationB,PrivilegeTypeEnum.STAT_PROJECT_ADMIN)){
                return true;
            }
            return checkRole(userEntity,relationB,PrivilegeTypeEnum.STAT_PROJECT_USER);
        }else if(privilegeType == PrivilegeTypeEnum.STAT_PROJECT_ADMIN.getPrivilegeType()){
            return checkRole(userEntity,relationB,PrivilegeTypeEnum.STAT_PROJECT_ADMIN);
        }else if(privilegeType == PrivilegeTypeEnum.STAT_ITEM_ADMIN.getPrivilegeType()){
            StatExtEntity statExtEntity = statManager.queryById(relationB);
            int projectId = statExtEntity.getProjectId();
            return checkRole(userEntity,projectId,PrivilegeTypeEnum.STAT_PROJECT_ADMIN);
        }else if(privilegeType == PrivilegeTypeEnum.SITE_MAP_ADMIN.getPrivilegeType()){
            return checkRole(userEntity,relationB,PrivilegeTypeEnum.SITE_MAP_ADMIN);
        }else if(privilegeType == PrivilegeTypeEnum.SITE_MAP_USER.getPrivilegeType()){
            if(checkRole(userEntity,relationB,PrivilegeTypeEnum.SITE_MAP_ADMIN)){
                return true;
            }
            return checkRole(userEntity,relationB,PrivilegeTypeEnum.SITE_MAP_USER);
        }
        return false;
    }

    public boolean checkRole(UserEntity userEntity,int relationB,PrivilegeTypeEnum privilegeTypeEnum){
        return privilegeDao.checkRole(userEntity,relationB,privilegeTypeEnum);
    }

    public void deleteByRelationId(int relationId, int privilegeType) throws Exception {
        privilegeDao.deleteByRelationId(relationId,privilegeType);
    }

    public void updatePrivilege(PrivilegeTypeEnum privilegeTypeEnum, int relationId, Set<Integer> userSet) throws Exception {
        Validate.isTrue(CollectionUtils.isNotEmpty(userSet));
        DBConnection connection = ConnectionManager.getConnection();
        ConnectionManager.beginTransaction(connection);
        try{
            privilegeDao.deleteByRelationId(relationId,privilegeTypeEnum.getPrivilegeType());
            for(Integer userId:userSet){
                createPrivilege(privilegeTypeEnum,relationId,userId);
            }
            ConnectionManager.commitTransaction(connection);
        }catch (Exception ex){
            ConnectionManager.rollbackTransaction(connection);
            throw ex;
        }finally {
            ConnectionManager.close(connection);
        }
    }

    public void createPrivilege(PrivilegeTypeEnum privilegeTypeEnum, int relationId, int userId) throws Exception {
        PrivilegeEntity privilegeEntity = new PrivilegeEntity();
        privilegeEntity.setCreateTime(new Date());
        privilegeEntity.setRelationA(userId);
        privilegeEntity.setRelationB(relationId);
        privilegeEntity.setRoleTypeEnum(privilegeTypeEnum);
        privilegeDao.save(privilegeEntity);
    }

}
