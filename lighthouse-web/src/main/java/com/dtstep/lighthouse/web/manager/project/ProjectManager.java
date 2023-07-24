package com.dtstep.lighthouse.web.manager.project;
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
import com.dtstep.lighthouse.common.entity.department.DepartmentViewEntity;
import com.dtstep.lighthouse.common.entity.project.ProjectEntity;
import com.dtstep.lighthouse.common.entity.project.ProjectViewEntity;
import com.dtstep.lighthouse.common.entity.user.UserEntity;
import com.dtstep.lighthouse.common.enums.order.OrderTypeEnum;
import com.dtstep.lighthouse.common.enums.relations.RelationTypeEnum;
import com.dtstep.lighthouse.common.enums.role.PrivilegeTypeEnum;
import com.dtstep.lighthouse.common.util.Md5Util;
import com.dtstep.lighthouse.core.dao.DaoHelper;
import com.dtstep.lighthouse.web.manager.department.DepartmentManager;
import com.dtstep.lighthouse.web.manager.privilege.PrivilegeManager;
import com.dtstep.lighthouse.web.manager.relations.RelationManager;
import com.dtstep.lighthouse.web.dao.ProjectDao;
import com.dtstep.lighthouse.web.manager.order.OrderManager;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectManager {

    @Autowired
    private RelationManager relationManager;

    @Autowired
    private OrderManager orderManager;

    @Autowired
    private PrivilegeManager privilegeManager;

    @Autowired
    private DepartmentManager departmentManager;

    @Autowired
    private ProjectDao projectDao;

    @Cacheable(value = "normal",key = "#targetClass + '_' + 'queryById' + '_' + #projectId",cacheManager = "caffeineCacheManager",unless = "#result == null")
    public ProjectEntity queryById(int projectId) throws Exception {
        return projectDao.queryById(projectId);
    }

    @Cacheable(value = "normal",key = "#targetClass + '_' + 'queryAll'",cacheManager = "caffeineCacheManager",unless = "#result == null")
    public List<ProjectEntity> queryAll() throws Exception {
        return projectDao.queryAll();
    }

    @Cacheable(value = "normal",key = "#targetClass + '_' + 'queryViewInfoById' + '_' + #currentUser.id + '_' + #projectId",cacheManager = "caffeineCacheManager",unless = "#result == null")
    public ProjectViewEntity queryViewInfoById(UserEntity currentUser, int projectId) throws Exception {
        ProjectEntity projectEntity = queryById(projectId);
        Validate.notNull(projectEntity);
        return combineViewInfo(currentUser,projectEntity);
    }

    public boolean isExist(int id) throws Exception {
        return DaoHelper.sql.count("select count(1) from ldp_stat_project where id = ?", id) == 1;
    }

    public int totalCount() throws Exception {
        return DaoHelper.sql.count("select count(1) from ldp_stat_project");
    }

    public ProjectViewEntity combineViewInfo(UserEntity currentUser, ProjectEntity projectEntity) throws Exception {
        int userId = currentUser.getId();
        ProjectViewEntity projectViewEntity = new ProjectViewEntity(projectEntity);
        boolean isFavorite = relationManager.existRelation(userId,projectEntity.getId(), RelationTypeEnum.FAVORITE_PROJECT);
        projectViewEntity.setFavorite(isFavorite);
        List<Integer> privileges = new ArrayList<>();
        boolean hashUserRole = privilegeManager.hasRole(currentUser,projectEntity.getId(), PrivilegeTypeEnum.STAT_PROJECT_ADMIN.getPrivilegeType());
        if(hashUserRole){
            privileges.add(PrivilegeTypeEnum.STAT_PROJECT_ADMIN.getPrivilegeType());
        }
        boolean hasAdminRole = privilegeManager.hasRole(currentUser,projectEntity.getId(),PrivilegeTypeEnum.STAT_PROJECT_USER.getPrivilegeType());
        if(hasAdminRole){
            privileges.add(PrivilegeTypeEnum.STAT_PROJECT_USER.getPrivilegeType());
        }
        if(CollectionUtils.isEmpty(privileges)){
            String hash = Md5Util.getMD5(currentUser.getId() + "_" + OrderTypeEnum.PROJECT_ACCESS.getType() + "_" + projectEntity.getId());
            boolean isApply = orderManager.isApply(hash);
            projectViewEntity.setApply(isApply);
        }
        List<UserEntity> admins = privilegeManager.queryAdmins(projectEntity.getId(),PrivilegeTypeEnum.STAT_PROJECT_ADMIN.getPrivilegeType());
        projectViewEntity.setAdmins(admins);
        projectViewEntity.setPrivilegeIds(privileges);
        DepartmentViewEntity departmentEntity = departmentManager.queryViewInfoById(projectEntity.getDepartmentId());
        if(departmentEntity != null){
            projectViewEntity.setDepartmentName(departmentEntity.getFullPathName());
        }
        return projectViewEntity;
    }

    public int countByDepartmentId(int departmentId) throws Exception {
        return DaoHelper.sql.count("select count(1) from ldp_stat_project where department_id = ?", departmentId);
    }
}
