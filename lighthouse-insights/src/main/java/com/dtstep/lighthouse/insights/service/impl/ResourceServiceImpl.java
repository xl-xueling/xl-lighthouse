package com.dtstep.lighthouse.insights.service.impl;
/*
 * Copyright (C) 2022-2024 XueLing.雪灵
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
import com.dtstep.lighthouse.common.modal.*;
import com.dtstep.lighthouse.common.entity.ResultCode;
import com.dtstep.lighthouse.insights.dao.*;
import com.dtstep.lighthouse.insights.dto.PermissionQueryParam;
import com.dtstep.lighthouse.common.modal.RolePair;
import com.dtstep.lighthouse.common.enums.OwnerTypeEnum;
import com.dtstep.lighthouse.common.enums.ResourceTypeEnum;
import com.dtstep.lighthouse.common.enums.RoleTypeEnum;
import com.dtstep.lighthouse.insights.service.*;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ResourceServiceImpl implements ResourceService {

    @Autowired
    private RoleService roleService;

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private GroupDao groupDao;

    @Autowired
    private StatDao statDao;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private MetricSetDao metricSetDao;

    @Autowired
    private ViewDao viewDao;

    @Autowired
    private CallerDao callerDao;

    @Autowired
    private DomainService domainService;

    @Transactional
    @Override
    public RolePair addResourceCallback(ResourceDto resource) {
        Role manageRole = null;
        Role accessRole = null;
        String name = null;
        if(resource.getResourceType() == ResourceTypeEnum.Domain){
            Role parentManageRole = roleService.cacheQueryRole(RoleTypeEnum.FULL_MANAGE_PERMISSION,0);
            Role parentAccessRole = roleService.cacheQueryRole(RoleTypeEnum.FULL_ACCESS_PERMISSION,0);
            Integer manageRolePid = parentManageRole.getId();
            Integer accessRolePid = parentAccessRole.getId();
            manageRole = new Role(RoleTypeEnum.DOMAIN_MANAGE_PERMISSION,resource.getResourceId(),manageRolePid);
            accessRole = new Role(RoleTypeEnum.DOMAIN_ACCESS_PERMISSION,resource.getResourceId(),accessRolePid);
            name = domainService.queryById(resource.getResourceId()).getName();
        }else if(resource.getResourceType() == ResourceTypeEnum.Department){
            RoleTypeEnum parentManageRoleType = resource.getParentResourceType() == ResourceTypeEnum.Domain ? RoleTypeEnum.DOMAIN_MANAGE_PERMISSION : RoleTypeEnum.DEPARTMENT_MANAGE_PERMISSION;
            Role parentManageRole = roleService.cacheQueryRole(parentManageRoleType,resource.getResourcePid());
            Integer manageRolePid = parentManageRole.getId();
            RoleTypeEnum parentAccessRoleType = resource.getParentResourceType() == ResourceTypeEnum.Domain ? RoleTypeEnum.DOMAIN_ACCESS_PERMISSION : RoleTypeEnum.DEPARTMENT_ACCESS_PERMISSION;
            Role parentAccessRole = roleService.cacheQueryRole(parentAccessRoleType,resource.getResourcePid());
            Integer accessRolePid = parentAccessRole.getId();
            manageRole = new Role(RoleTypeEnum.DEPARTMENT_MANAGE_PERMISSION,resource.getResourceId(),manageRolePid);
            accessRole = new Role(RoleTypeEnum.DEPARTMENT_ACCESS_PERMISSION,resource.getResourceId(),accessRolePid);
            name = departmentDao.queryById(resource.getResourceId()).getName();
        }else if(resource.getResourceType() == ResourceTypeEnum.Project){
            Role parentManageRole = roleService.cacheQueryRole(RoleTypeEnum.DEPARTMENT_MANAGE_PERMISSION,resource.getResourcePid());
            Integer manageRolePid = parentManageRole.getId();
            Role parentAccessRole = roleService.cacheQueryRole(RoleTypeEnum.DEPARTMENT_ACCESS_PERMISSION,resource.getResourcePid());
            Integer accessRolePid = parentAccessRole.getId();
            manageRole = new Role(RoleTypeEnum.PROJECT_MANAGE_PERMISSION,resource.getResourceId(),manageRolePid);
            accessRole = new Role(RoleTypeEnum.PROJECT_ACCESS_PERMISSION,resource.getResourceId(),accessRolePid);
            name = projectDao.queryById(resource.getResourceId()).getTitle();
        }else if(resource.getResourceType() == ResourceTypeEnum.Group){
            Role parentManageRole = roleService.cacheQueryRole(RoleTypeEnum.PROJECT_MANAGE_PERMISSION,resource.getResourcePid());
            Integer manageRolePid = parentManageRole.getId();
            Role parentAccessRole = roleService.cacheQueryRole(RoleTypeEnum.PROJECT_ACCESS_PERMISSION,resource.getResourcePid());
            Integer accessRolePid = parentAccessRole.getId();
            manageRole = new Role(RoleTypeEnum.GROUP_MANAGE_PERMISSION,resource.getResourceId(),manageRolePid);
            accessRole = new Role(RoleTypeEnum.GROUP_ACCESS_PERMISSION,resource.getResourceId(),accessRolePid);
            name = groupDao.queryById(resource.getResourceId()).getToken();
        }else if(resource.getResourceType() == ResourceTypeEnum.Stat){
            Role parentManageRole = roleService.cacheQueryRole(RoleTypeEnum.GROUP_MANAGE_PERMISSION,resource.getResourcePid());
            Integer manageRolePid = parentManageRole.getId();
            Role parentAccessRole = roleService.cacheQueryRole(RoleTypeEnum.GROUP_ACCESS_PERMISSION,resource.getResourcePid());
            Integer accessRolePid = parentAccessRole.getId();
            manageRole = new Role(RoleTypeEnum.STAT_MANAGE_PERMISSION,resource.getResourceId(),manageRolePid);
            accessRole = new Role(RoleTypeEnum.STAT_ACCESS_PERMISSION,resource.getResourceId(),accessRolePid);
            name = statDao.queryById(resource.getResourceId()).getTitle();
        }else if(resource.getResourceType() == ResourceTypeEnum.MetricSet){
            Role parentManageRole = roleService.cacheQueryRole(RoleTypeEnum.DOMAIN_MANAGE_PERMISSION,resource.getResourcePid());
            Integer manageRolePid = parentManageRole.getId();
            Role parentAccessRole = roleService.cacheQueryRole(RoleTypeEnum.DOMAIN_ACCESS_PERMISSION,resource.getResourcePid());
            Integer accessRolePid = parentAccessRole.getId();
            manageRole = new Role(RoleTypeEnum.METRIC_MANAGE_PERMISSION,resource.getResourceId(),manageRolePid);
            accessRole = new Role(RoleTypeEnum.METRIC_ACCESS_PERMISSION,resource.getResourceId(),accessRolePid);
            name = metricSetDao.queryById(resource.getResourceId()).getTitle();
        }else if(resource.getResourceType() == ResourceTypeEnum.View){
            Role parentManageRole = roleService.cacheQueryRole(RoleTypeEnum.DOMAIN_MANAGE_PERMISSION,resource.getResourcePid());
            Integer manageRolePid = parentManageRole.getId();
            Role parentAccessRole = roleService.cacheQueryRole(RoleTypeEnum.DOMAIN_ACCESS_PERMISSION,resource.getResourcePid());
            Integer accessRolePid = parentAccessRole.getId();
            manageRole = new Role(RoleTypeEnum.VIEW_MANAGE_PERMISSION,resource.getResourceId(),manageRolePid);
            accessRole = new Role(RoleTypeEnum.VIEW_ACCESS_PERMISSION,resource.getResourceId(),accessRolePid);
            name = viewDao.queryById(resource.getResourceId()).getTitle();
        }else if(resource.getResourceType() == ResourceTypeEnum.Caller){
            Role parentManageRole = roleService.cacheQueryRole(RoleTypeEnum.DOMAIN_MANAGE_PERMISSION,resource.getResourcePid());
            Integer manageRolePid = parentManageRole.getId();
            Role parentAccessRole = roleService.cacheQueryRole(RoleTypeEnum.DOMAIN_ACCESS_PERMISSION,resource.getResourcePid());
            Integer accessRolePid = parentAccessRole.getId();
            manageRole = new Role(RoleTypeEnum.CALLER_MANAGER_PERMISSION,resource.getResourceId(),manageRolePid);
            accessRole = new Role(RoleTypeEnum.CALLER_ACCESS_PERMISSION,resource.getResourceId(),accessRolePid);
            name = callerDao.queryById(resource.getResourceId()).getName();
        }
        Validate.notNull(manageRole);
        Validate.notNull(accessRole);
        manageRole.setDesc(manageRole.getRoleType().name() + "(" + name + ")");
        accessRole.setDesc(accessRole.getRoleType().name() + "(" + name + ")");
        roleService.create(manageRole);
        roleService.create(accessRole);
        return new RolePair(manageRole.getId(),accessRole.getId());
    }

    @Transactional
    @Override
    public void updateResourcePidCallback(ResourceDto resource) {
        Role manageRole = null;
        Role accessRole = null;
        String name = null;
        if(resource.getResourceType() == ResourceTypeEnum.Domain){
            Role parentManageRole = roleService.cacheQueryRole(RoleTypeEnum.FULL_MANAGE_PERMISSION,0);
            Role parentAccessRole = roleService.cacheQueryRole(RoleTypeEnum.FULL_ACCESS_PERMISSION,0);
            Integer manageRolePid = parentManageRole.getId();
            Integer accessRolePid = parentAccessRole.getId();
            manageRole = new Role(RoleTypeEnum.DOMAIN_MANAGE_PERMISSION,resource.getResourceId(),manageRolePid);
            accessRole = new Role(RoleTypeEnum.DOMAIN_ACCESS_PERMISSION,resource.getResourceId(),accessRolePid);
            name = domainService.queryById(resource.getResourceId()).getName();
        }else if(resource.getResourceType() == ResourceTypeEnum.Department){
            RoleTypeEnum parentManageRoleType = resource.getParentResourceType() == ResourceTypeEnum.Domain ? RoleTypeEnum.DOMAIN_MANAGE_PERMISSION : RoleTypeEnum.DEPARTMENT_MANAGE_PERMISSION;
            Role parentManageRole = roleService.cacheQueryRole(parentManageRoleType,resource.getResourcePid());
            Integer manageRolePid = parentManageRole.getId();
            RoleTypeEnum parentAccessRoleType = resource.getParentResourceType() == ResourceTypeEnum.Domain ? RoleTypeEnum.DOMAIN_ACCESS_PERMISSION : RoleTypeEnum.DEPARTMENT_ACCESS_PERMISSION;
            Role parentAccessRole = roleService.cacheQueryRole(parentAccessRoleType,resource.getResourcePid());
            Integer accessRolePid = parentAccessRole.getId();
            manageRole = new Role(RoleTypeEnum.DEPARTMENT_MANAGE_PERMISSION,resource.getResourceId(),manageRolePid);
            accessRole = new Role(RoleTypeEnum.DEPARTMENT_ACCESS_PERMISSION,resource.getResourceId(),accessRolePid);
            name = departmentDao.queryById(resource.getResourceId()).getName();
        }else if(resource.getResourceType() == ResourceTypeEnum.Project){
            Role parentManageRole = roleService.cacheQueryRole(RoleTypeEnum.DEPARTMENT_MANAGE_PERMISSION,resource.getResourcePid());
            Integer manageRolePid = parentManageRole.getId();
            Role parentAccessRole = roleService.cacheQueryRole(RoleTypeEnum.DEPARTMENT_ACCESS_PERMISSION,resource.getResourcePid());
            Integer accessRolePid = parentAccessRole.getId();
            manageRole = new Role(RoleTypeEnum.PROJECT_MANAGE_PERMISSION,resource.getResourceId(),manageRolePid);
            accessRole = new Role(RoleTypeEnum.PROJECT_ACCESS_PERMISSION,resource.getResourceId(),accessRolePid);
            name = projectDao.queryById(resource.getResourceId()).getTitle();
        }else if(resource.getResourceType() == ResourceTypeEnum.Group){
            Role parentManageRole = roleService.cacheQueryRole(RoleTypeEnum.PROJECT_MANAGE_PERMISSION,resource.getResourcePid());
            Integer manageRolePid = parentManageRole.getId();
            Role parentAccessRole = roleService.cacheQueryRole(RoleTypeEnum.PROJECT_ACCESS_PERMISSION,resource.getResourcePid());
            Integer accessRolePid = parentAccessRole.getId();
            manageRole = new Role(RoleTypeEnum.GROUP_MANAGE_PERMISSION,resource.getResourceId(),manageRolePid);
            accessRole = new Role(RoleTypeEnum.GROUP_ACCESS_PERMISSION,resource.getResourceId(),accessRolePid);
            name = groupDao.queryById(resource.getResourceId()).getToken();
        }else if(resource.getResourceType() == ResourceTypeEnum.Stat){
            Role parentManageRole = roleService.cacheQueryRole(RoleTypeEnum.GROUP_MANAGE_PERMISSION,resource.getResourcePid());
            Integer manageRolePid = parentManageRole.getId();
            Role parentAccessRole = roleService.cacheQueryRole(RoleTypeEnum.GROUP_ACCESS_PERMISSION,resource.getResourcePid());
            Integer accessRolePid = parentAccessRole.getId();
            manageRole = new Role(RoleTypeEnum.STAT_MANAGE_PERMISSION,resource.getResourceId(),manageRolePid);
            accessRole = new Role(RoleTypeEnum.STAT_ACCESS_PERMISSION,resource.getResourceId(),accessRolePid);
            name = statDao.queryById(resource.getResourceId()).getTitle();
        }else if(resource.getResourceType() == ResourceTypeEnum.MetricSet){
            Role parentManageRole = roleService.cacheQueryRole(RoleTypeEnum.DOMAIN_MANAGE_PERMISSION,resource.getResourcePid());
            Integer manageRolePid = parentManageRole.getId();
            Role parentAccessRole = roleService.cacheQueryRole(RoleTypeEnum.DOMAIN_ACCESS_PERMISSION,resource.getResourcePid());
            Integer accessRolePid = parentAccessRole.getId();
            manageRole = new Role(RoleTypeEnum.METRIC_MANAGE_PERMISSION,resource.getResourceId(),manageRolePid);
            accessRole = new Role(RoleTypeEnum.METRIC_ACCESS_PERMISSION,resource.getResourceId(),accessRolePid);
            name = metricSetDao.queryById(resource.getResourceId()).getTitle();
        }else if(resource.getResourceType() == ResourceTypeEnum.View){
            Role parentManageRole = roleService.cacheQueryRole(RoleTypeEnum.DOMAIN_MANAGE_PERMISSION,resource.getResourcePid());
            Integer manageRolePid = parentManageRole.getId();
            Role parentAccessRole = roleService.cacheQueryRole(RoleTypeEnum.DOMAIN_ACCESS_PERMISSION,resource.getResourcePid());
            Integer accessRolePid = parentAccessRole.getId();
            manageRole = new Role(RoleTypeEnum.VIEW_MANAGE_PERMISSION,resource.getResourceId(),manageRolePid);
            accessRole = new Role(RoleTypeEnum.VIEW_ACCESS_PERMISSION,resource.getResourceId(),accessRolePid);
            name = viewDao.queryById(resource.getResourceId()).getTitle();
        }else if(resource.getResourceType() == ResourceTypeEnum.Caller){
            Role parentManageRole = roleService.cacheQueryRole(RoleTypeEnum.DOMAIN_MANAGE_PERMISSION,resource.getResourcePid());
            Integer manageRolePid = parentManageRole.getId();
            Role parentAccessRole = roleService.cacheQueryRole(RoleTypeEnum.DOMAIN_ACCESS_PERMISSION,resource.getResourcePid());
            Integer accessRolePid = parentAccessRole.getId();
            manageRole = new Role(RoleTypeEnum.CALLER_MANAGER_PERMISSION,resource.getResourceId(),manageRolePid);
            accessRole = new Role(RoleTypeEnum.CALLER_ACCESS_PERMISSION,resource.getResourceId(),accessRolePid);
            name = callerDao.queryById(resource.getResourceId()).getName();
        }
        Validate.notNull(manageRole);
        Validate.notNull(accessRole);
        manageRole.setDesc(manageRole.getRoleType().name() + "(" + name + ")");
        accessRole.setDesc(accessRole.getRoleType().name() + "(" + name + ")");
        roleService.update(manageRole);
        roleService.update(accessRole);
    }

    @Transactional
    @Override
    public ResultCode deleteResourceCallback(ResourceDto resource){
        Role manageRole = null;
        Role accessRole = null;
        if(resource.getResourceType() == ResourceTypeEnum.Domain){
            manageRole = roleService.cacheQueryRole(RoleTypeEnum.DOMAIN_MANAGE_PERMISSION,resource.getResourceId());
            accessRole = roleService.cacheQueryRole(RoleTypeEnum.DOMAIN_ACCESS_PERMISSION,resource.getResourceId());
        }else if(resource.getResourceType() == ResourceTypeEnum.Department){
            manageRole = roleService.cacheQueryRole(RoleTypeEnum.DEPARTMENT_MANAGE_PERMISSION,resource.getResourceId());
            accessRole = roleService.cacheQueryRole(RoleTypeEnum.DEPARTMENT_ACCESS_PERMISSION,resource.getResourceId());
        }else if(resource.getResourceType() == ResourceTypeEnum.Project){
            manageRole = roleService.cacheQueryRole(RoleTypeEnum.PROJECT_MANAGE_PERMISSION,resource.getResourceId());
            accessRole = roleService.cacheQueryRole(RoleTypeEnum.PROJECT_ACCESS_PERMISSION,resource.getResourceId());
        }else if(resource.getResourceType() == ResourceTypeEnum.Group){
            manageRole = roleService.cacheQueryRole(RoleTypeEnum.GROUP_MANAGE_PERMISSION,resource.getResourceId());
            accessRole = roleService.cacheQueryRole(RoleTypeEnum.GROUP_ACCESS_PERMISSION,resource.getResourceId());
        }else if(resource.getResourceType() == ResourceTypeEnum.Stat){
            manageRole = roleService.cacheQueryRole(RoleTypeEnum.STAT_MANAGE_PERMISSION,resource.getResourceId());
            accessRole = roleService.cacheQueryRole(RoleTypeEnum.STAT_ACCESS_PERMISSION,resource.getResourceId());
        }else if(resource.getResourceType() == ResourceTypeEnum.MetricSet){
            manageRole = roleService.cacheQueryRole(RoleTypeEnum.METRIC_MANAGE_PERMISSION,resource.getResourceId());
            accessRole = roleService.cacheQueryRole(RoleTypeEnum.METRIC_ACCESS_PERMISSION,resource.getResourceId());
        }else if(resource.getResourceType() == ResourceTypeEnum.View){
            manageRole = roleService.cacheQueryRole(RoleTypeEnum.VIEW_MANAGE_PERMISSION,resource.getResourceId());
            accessRole = roleService.cacheQueryRole(RoleTypeEnum.VIEW_ACCESS_PERMISSION,resource.getResourceId());
        }else if(resource.getResourceType() == ResourceTypeEnum.Caller){
            manageRole = roleService.cacheQueryRole(RoleTypeEnum.CALLER_MANAGER_PERMISSION,resource.getResourceId());
            accessRole = roleService.cacheQueryRole(RoleTypeEnum.CALLER_ACCESS_PERMISSION,resource.getResourceId());
        }
        Validate.notNull(manageRole);
        Validate.notNull(accessRole);
        PermissionQueryParam manageQueryParam = new PermissionQueryParam();
        manageQueryParam.setRoleId(manageRole.getId());
        permissionService.delete(manageQueryParam);
        PermissionQueryParam accessQueryParam = new PermissionQueryParam();
        accessQueryParam.setRoleId(accessRole.getId());
        permissionService.delete(accessQueryParam);
        roleService.deleteById(manageRole.getId());
        roleService.deleteById(accessRole.getId());
        return ResultCode.success;
    }

    @Override
    public int grantPermission(Integer ownerId, OwnerTypeEnum ownerTypeEnum, Integer resourceId, RoleTypeEnum roleTypeEnum) {
        Role role = roleService.cacheQueryRole(roleTypeEnum,resourceId);
        Validate.notNull(role);
        return permissionService.grantPermission(ownerId,ownerTypeEnum,role.getId());
    }

    @Override
    public int releasePermission(Integer ownerId, OwnerTypeEnum ownerTypeEnum, Integer resourceId, RoleTypeEnum roleTypeEnum) {
        Role role = roleService.cacheQueryRole(roleTypeEnum,resourceId);
        return permissionService.releasePermission(ownerId,ownerTypeEnum,role.getId());
    }

    @Override
    public ResourceDto queryByRoleId(Integer roleId) {
        Role role = roleService.queryById(roleId);
        if(role == null){
            return null;
        }
        ResourceDto resource = null;
        RoleTypeEnum roleTypeEnum = role.getRoleType();
        Integer resourceId = role.getResourceId();
        if(roleTypeEnum == RoleTypeEnum.DOMAIN_MANAGE_PERMISSION || roleTypeEnum == RoleTypeEnum.DOMAIN_ACCESS_PERMISSION){
            Domain domain = domainService.queryById(resourceId);
            resource = new ResourceDto(ResourceTypeEnum.Domain,resourceId,domain);
        }else if(roleTypeEnum == RoleTypeEnum.DEPARTMENT_MANAGE_PERMISSION || roleTypeEnum == RoleTypeEnum.DEPARTMENT_ACCESS_PERMISSION){
            Department department = departmentDao.queryById(resourceId);
            resource = new ResourceDto(ResourceTypeEnum.Department,resourceId,department);
        }else if(roleTypeEnum == RoleTypeEnum.PROJECT_MANAGE_PERMISSION || roleTypeEnum == RoleTypeEnum.PROJECT_ACCESS_PERMISSION){
            Project project = projectDao.queryById(resourceId);
            resource = new ResourceDto(ResourceTypeEnum.Project,resourceId,project);
        }else if(roleTypeEnum == RoleTypeEnum.GROUP_MANAGE_PERMISSION || roleTypeEnum == RoleTypeEnum.GROUP_ACCESS_PERMISSION){
            Group group = groupDao.queryById(resourceId);
            resource = new ResourceDto(ResourceTypeEnum.Group,resourceId,group);
        }else if(roleTypeEnum == RoleTypeEnum.STAT_MANAGE_PERMISSION || roleTypeEnum == RoleTypeEnum.STAT_ACCESS_PERMISSION){
            Stat stat = statDao.queryById(resourceId);
            resource = new ResourceDto(ResourceTypeEnum.Stat,resourceId,stat);
        }else if(roleTypeEnum == RoleTypeEnum.METRIC_MANAGE_PERMISSION || roleTypeEnum == RoleTypeEnum.METRIC_ACCESS_PERMISSION){
            MetricSet metricSet = metricSetDao.queryById(resourceId);
            resource = new ResourceDto(ResourceTypeEnum.MetricSet,resourceId,metricSet);
        }else if(roleTypeEnum == RoleTypeEnum.VIEW_MANAGE_PERMISSION || roleTypeEnum == RoleTypeEnum.VIEW_ACCESS_PERMISSION){
            View view = viewDao.queryById(resourceId);
            resource = new ResourceDto(ResourceTypeEnum.View,resourceId,view);
        }else if(roleTypeEnum == RoleTypeEnum.CALLER_MANAGER_PERMISSION || roleTypeEnum == RoleTypeEnum.CALLER_ACCESS_PERMISSION){
            Caller caller = callerDao.queryById(resourceId);
            resource = new ResourceDto(ResourceTypeEnum.Caller,resourceId,caller);
        }else if(roleTypeEnum == RoleTypeEnum.FULL_MANAGE_PERMISSION || roleTypeEnum == RoleTypeEnum.FULL_ACCESS_PERMISSION){
            resource = new ResourceDto(ResourceTypeEnum.System,0);
        }
        return resource;
    }
}
