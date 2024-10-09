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
import com.dtstep.lighthouse.common.enums.ResourceTypeEnum;
import com.dtstep.lighthouse.common.enums.RoleTypeEnum;
import com.dtstep.lighthouse.common.enums.UserStateEnum;
import com.dtstep.lighthouse.common.entity.ListData;
import com.dtstep.lighthouse.common.modal.*;
import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.insights.dao.PermissionDao;
import com.dtstep.lighthouse.insights.service.*;
import com.dtstep.lighthouse.insights.vo.*;
import com.dtstep.lighthouse.insights.dto.PermissionQueryParam;
import com.dtstep.lighthouse.common.enums.OwnerTypeEnum;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class PermissionServiceImpl implements PermissionService {

    private static final Logger logger = LoggerFactory.getLogger(PermissionServiceImpl.class);

    @Autowired
    private PermissionDao permissionDao;

    @Autowired
    private UserService userService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private CallerService callerService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private StatService statService;

    @Autowired
    private ViewService viewService;

    @Autowired
    private BaseService baseService;

    @Override
    public int create(Permission permission) {
        LocalDateTime localDateTime = LocalDateTime.now();
        permission.setCreateTime(localDateTime);
        permission.setUpdateTime(localDateTime);
        permissionDao.insert(permission);
        return permission.getId();
    }

    @Transactional
    @Override
    public int batchCreate(List<Permission> permissionList) {
        int result = 0;
        if(CollectionUtils.isEmpty(permissionList)){
            return result;
        }
        LocalDateTime localDateTime = LocalDateTime.now();
        List<Permission> permissions = permissionList.stream().filter(z -> !existPermission(z.getOwnerId(),z.getOwnerType(),z.getRoleId())).map(z -> {
            z.setCreateTime(localDateTime);
            z.setUpdateTime(localDateTime);
            return z;
        }).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(permissions)){
            result = permissionDao.batchInsert(permissions);
        }
        return result;
    }

    @Override
    public boolean checkUserPermission(Integer userId, Integer roleId) {
        return permissionDao.checkUserPermission(userId,roleId);
    }

    @Override
    @Cacheable(value = "NormalPeriod",key = "#targetClass + '_' + 'queryUserPermissionsByRoleId' + '_' + #roleId + '_' + #limit",cacheManager = "caffeineCacheManager",unless = "#result == null")
    public List<Integer> queryUserPermissionsByRoleId(Integer roleId, Integer limit) {
        return permissionDao.queryUserPermissionsByRoleId(roleId,limit);
    }

    @Override
    public boolean existPermission(Integer ownerId, OwnerTypeEnum ownerType, Integer roleId) {
        return permissionDao.existPermission(ownerId, ownerType, roleId);
    }

    @Transactional
    @Override
    public int grantPermission(Integer ownerId, OwnerTypeEnum ownerTypeEnum, Integer roleId) {
        Validate.notNull(ownerId);
        Validate.notNull(ownerTypeEnum);
        Validate.notNull(roleId);
        if(existPermission(ownerId,ownerTypeEnum,roleId)){
            return 0;
        }
        if(ownerTypeEnum == OwnerTypeEnum.USER){
            User user = userService.queryById(ownerId);
            Validate.notNull(user);
            Validate.isTrue(user.getState() == UserStateEnum.USER_NORMAL);
        }else if (ownerTypeEnum == OwnerTypeEnum.DEPARTMENT){
            Department department = departmentService.queryById(ownerId);
            Validate.notNull(department);
        }
        Permission permission = new Permission();
        permission.setOwnerId(ownerId);
        permission.setOwnerType(ownerTypeEnum);
        permission.setRoleId(roleId);
        LocalDateTime localDateTime = LocalDateTime.now();
        permission.setCreateTime(localDateTime);
        permission.setUpdateTime(localDateTime);
        return permissionDao.insert(permission);
    }

    @Transactional
    @Override
    public int grantPermission(Integer ownerId, OwnerTypeEnum ownerTypeEnum, Integer roleId,int extension) throws Exception{
        Validate.notNull(ownerId);
        Validate.notNull(ownerTypeEnum);
        Validate.notNull(roleId);
        PermissionQueryParam permissionQueryParam = new PermissionQueryParam();
        permissionQueryParam.setRoleId(roleId);
        permissionQueryParam.setOwnerId(ownerId);
        permissionQueryParam.setOwnerType(ownerTypeEnum);
        delete(permissionQueryParam);
        if(ownerTypeEnum == OwnerTypeEnum.USER){
            User user = userService.queryById(ownerId);
            Validate.notNull(user);
            Validate.isTrue(user.getState() == UserStateEnum.USER_NORMAL);
        }else if (ownerTypeEnum == OwnerTypeEnum.DEPARTMENT){
            Department department = departmentService.queryById(ownerId);
            Validate.notNull(department);
        }else if(ownerTypeEnum == OwnerTypeEnum.CALLER){
            Caller caller = callerService.queryById(ownerId);
            Validate.notNull(caller);
        }
        long current = System.currentTimeMillis();
        long extensionTime = current + TimeUnit.SECONDS.toMillis(extension);
        LocalDateTime localDateTime = DateUtil.timestampToLocalDateTime(current);
        LocalDateTime extensionDateTime = DateUtil.timestampToLocalDateTime(extensionTime);
        Permission permission = new Permission();
        permission.setOwnerId(ownerId);
        permission.setOwnerType(ownerTypeEnum);
        permission.setRoleId(roleId);
        permission.setExpireTime(extensionDateTime);
        permission.setCreateTime(localDateTime);
        permission.setUpdateTime(localDateTime);
        return permissionDao.insert(permission);
    }

    @Override
    public int releasePermission(Integer ownerId, OwnerTypeEnum ownerTypeEnum, Integer roleId) {
        Validate.notNull(ownerId);
        Validate.notNull(ownerTypeEnum);
        Validate.notNull(roleId);
        return permissionDao.delete(ownerId, ownerTypeEnum, roleId);
    }

    @Override
    public int releasePermission(Integer id) {
        PermissionQueryParam queryParam = new PermissionQueryParam();
        queryParam.setId(id);
        return permissionDao.delete(queryParam);
    }

    @Override
    public Permission queryById(Integer id) {
        return permissionDao.queryById(id);
    }

    @Override
    public List<Permission> queryUserManagePermission(Integer userId,Integer limit) {
        return permissionDao.queryUserManagePermission(userId,limit);
    }

    @Override
    public int delete(PermissionQueryParam queryParam) {
        return permissionDao.delete(queryParam);
    }

    private PermissionVO translate(Permission permission) throws Exception {
        PermissionVO permissionVO = new PermissionVO(permission);
        if(permissionVO.getOwnerType() == OwnerTypeEnum.USER){
            User user = userService.cacheQueryById(permissionVO.getOwnerId());
            permissionVO.setExtend(user);
        }else if(permissionVO.getOwnerType() == OwnerTypeEnum.DEPARTMENT){
            Department department = departmentService.queryById(permissionVO.getOwnerId());
            permissionVO.setExtend(department);
        }else if(permissionVO.getOwnerType() == OwnerTypeEnum.CALLER){
            Caller caller = callerService.queryById(permissionVO.getOwnerId());
            permissionVO.setExtend(caller);
        }
        int roleId = permission.getRoleId();
        Role role = roleService.cacheQueryById(roleId);
        permissionVO.setRoleType(role.getRoleType());
        return permissionVO;
    }


    @Override
    public ListData<PermissionVO> queryList(PermissionQueryParam queryParam, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<PermissionVO> dtoList = new ArrayList<>();
        PageInfo<PermissionVO> pageInfo;
        try{
            List<Permission> permissionList = permissionDao.queryList(queryParam);
            pageInfo = new PageInfo(permissionList);
        }finally {
            PageHelper.clearPage();
        }
        for(Permission permission : pageInfo.getList()){
            try{
                PermissionVO dto = translate(permission);
                dtoList.add(dto);
            }catch (Exception ex){
                logger.error("translate item info error,itemId:{}",permission.getId(),ex);
            }
        }
        return ListData.newInstance(dtoList,pageInfo.getTotal(),pageNum,pageSize);
    }

    @Override
    public ListData<AuthRecord> queryOwnerAuthList(PermissionQueryParam queryParam, Integer pageNum, Integer pageSize) throws Exception {
        PageHelper.startPage(pageNum,pageSize);
        PageInfo<Permission> pageInfo;
        try{
            List<Permission> permissionList = permissionDao.queryAuthList(queryParam);
            pageInfo = new PageInfo<>(permissionList);
        }finally {
            PageHelper.clearPage();
        }
        List<AuthRecord> voList = new ArrayList<>();
        List<Integer> statIdList = new ArrayList<>();
        List<Integer> projectIdList = new ArrayList<>();
        List<Integer> viewIdList = new ArrayList<>();
        HashMap<Integer,Role> roleMap = new HashMap<>();
        for(Permission permission : pageInfo.getList()){
            int roleId = permission.getRoleId();
            Role role = roleService.queryById(roleId);
            if(role == null){
                continue;
            }
            roleMap.put(roleId,role);
            RoleTypeEnum roleTypeEnum = role.getRoleType();
            if(roleTypeEnum == RoleTypeEnum.PROJECT_MANAGE_PERMISSION || roleTypeEnum == RoleTypeEnum.PROJECT_ACCESS_PERMISSION){
                int projectId = role.getResourceId();
                projectIdList.add(projectId);
            }else if(roleTypeEnum == RoleTypeEnum.STAT_MANAGE_PERMISSION || roleTypeEnum == RoleTypeEnum.STAT_ACCESS_PERMISSION){
                int statId = role.getResourceId();
                statIdList.add(statId);
            }else if(roleTypeEnum == RoleTypeEnum.VIEW_MANAGE_PERMISSION || roleTypeEnum == RoleTypeEnum.VIEW_ACCESS_PERMISSION){
                int viewId = role.getResourceId();
                viewIdList.add(viewId);
            }
        }
        List<StatVO> statList = statService.queryByIds(statIdList);
        List<ProjectVO> projectList = projectService.queryByIds(projectIdList);
        List<ViewVO> viewList = viewService.queryByIds(viewIdList);
        for(Permission permission : pageInfo.getList()){
            int roleId = permission.getRoleId();
            Role role = roleMap.get(roleId);
            if(role == null){
                continue;
            }
            AuthRecord authRecord = new AuthRecord(permission);
            authRecord.setId(permission.getId());
            RoleTypeEnum roleTypeEnum = role.getRoleType();
            if(roleTypeEnum == RoleTypeEnum.PROJECT_MANAGE_PERMISSION || roleTypeEnum == RoleTypeEnum.PROJECT_ACCESS_PERMISSION){
                ProjectVO project = projectList.stream().filter(x -> x.getId().intValue() == role.getResourceId().intValue()).findFirst().orElse(null);
                authRecord.setExtend(project);
                authRecord.setResourceId(role.getResourceId());
                authRecord.setResourceType(ResourceTypeEnum.Project);
            }else if(roleTypeEnum == RoleTypeEnum.STAT_MANAGE_PERMISSION || roleTypeEnum == RoleTypeEnum.STAT_ACCESS_PERMISSION){
                StatVO statVO = statList.stream().filter(x -> x.getId().intValue() == role.getResourceId().intValue()).findFirst().orElse(null);
                authRecord.setExtend(statVO);
                authRecord.setResourceId(role.getResourceId());
                authRecord.setResourceType(ResourceTypeEnum.Stat);
            }else if(roleTypeEnum == RoleTypeEnum.VIEW_MANAGE_PERMISSION || roleTypeEnum == RoleTypeEnum.VIEW_ACCESS_PERMISSION){
                ViewVO viewVO = viewList.stream().filter(x -> x.getId().intValue() == role.getResourceId().intValue()).findFirst().orElse(null);
                authRecord.setExtend(viewVO);
                authRecord.setResourceId(role.getResourceId());
                authRecord.setResourceType(ResourceTypeEnum.View);
            }
            voList.add(authRecord);
        }
        return ListData.newInstance(voList,pageInfo.getTotal(),pageNum,pageSize);
    }

    @Override
    public int extensionPermission(Integer id, int expire) throws Exception {
        return permissionDao.extensionPermission(id,expire);
    }
}
