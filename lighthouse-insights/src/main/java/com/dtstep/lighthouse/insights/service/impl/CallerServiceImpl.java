package com.dtstep.lighthouse.insights.service.impl;
/*
 * Copyright (C) 2022-2025 XueLing.雪灵
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
import com.dtstep.lighthouse.common.entity.ListData;
import com.dtstep.lighthouse.common.entity.ResultCode;
import com.dtstep.lighthouse.common.entity.ServiceResult;
import com.dtstep.lighthouse.common.enums.*;
import com.dtstep.lighthouse.common.modal.*;
import com.dtstep.lighthouse.common.random.RandomID;
import com.dtstep.lighthouse.insights.dao.CallerDao;
import com.dtstep.lighthouse.insights.dao.PermissionDao;
import com.dtstep.lighthouse.insights.dto.PermissionGrantParam;
import com.dtstep.lighthouse.insights.dto.PermissionReleaseParam;
import com.dtstep.lighthouse.insights.service.*;
import com.dtstep.lighthouse.insights.vo.CallerVO;
import com.dtstep.lighthouse.insights.service.CallerService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CallerServiceImpl implements CallerService {

    private static final Logger logger = LoggerFactory.getLogger(CallerServiceImpl.class);

    @Autowired
    private CallerDao callerDao;

    @Autowired
    private BaseService baseService;

    @Autowired
    private DomainService domainService;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private PermissionDao permissionDao;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @Transactional
    @Override
    public ServiceResult<Integer> create(Caller caller) throws Exception {
        String callerName = caller.getName();
        CallerQueryParam countQueryParam = new CallerQueryParam();
        countQueryParam.setName(callerName);
        int count = callerDao.count(countQueryParam);
        if(count > 0){
            return ServiceResult.result(ResultCode.getExtendResultCode(ResultCode.callerNameAlreadyExist,callerName));
        }
        LocalDateTime localDateTime = LocalDateTime.now();
        String secretKey = RandomID.id(40);
        caller.setSecretKey(secretKey);
        caller.setState(CallerStateEnum.NORMAL);
        caller.setCreateTime(localDateTime);
        caller.setUpdateTime(localDateTime);
        callerDao.insert(caller);
        int id = caller.getId();
        Domain domain = domainService.queryDefault();
        RolePair rolePair = resourceService.addResourceCallback(ResourceDto.newResource(ResourceTypeEnum.Caller,id,ResourceTypeEnum.Domain,domain.getId()));
        Integer manageRoleId = rolePair.getManageRoleId();
        int currentUserId = baseService.getCurrentUserId();
        Permission adminPermission = new Permission(currentUserId, OwnerTypeEnum.USER,manageRoleId);
        permissionService.create(adminPermission);
        if(id > 0){
            return ServiceResult.result(ResultCode.success,id);
        }else{
            return ServiceResult.result(ResultCode.systemError);
        }
    }

    @Transactional
    @Override
    public int update(Caller caller) throws Exception {
        LocalDateTime localDateTime = LocalDateTime.now();
        caller.setUpdateTime(localDateTime);
        Domain domain = domainService.queryDefault();
        resourceService.updateResourcePidCallback(ResourceDto.newResource(ResourceTypeEnum.Caller,caller.getId(),ResourceTypeEnum.Domain,domain.getId()));
        return callerDao.update(caller);
    }

    @Override
    public CallerVO queryById(Integer id) throws Exception {
        Caller caller = callerDao.queryById(id);
        return translate(caller);
    }

    @Transactional
    @Override
    public void deleteById(Integer id) throws Exception {
        callerDao.deleteById(id);
        Domain domain = domainService.queryDefault();
        resourceService.deleteResourceCallback(ResourceDto.newResource(ResourceTypeEnum.Caller,id,ResourceTypeEnum.Domain,domain.getId()));
    }

    private CallerVO translate(Caller caller){
        if(caller == null){
            return null;
        }
        CallerVO callerVO = new CallerVO(caller);
        Role manageRole = roleService.cacheQueryRole(RoleTypeEnum.CALLER_MANAGER_PERMISSION,caller.getId());
        List<Integer> adminIds = permissionService.queryUserPermissionsByRoleId(manageRole.getId(),3);
        if(CollectionUtils.isNotEmpty(adminIds)){
            List<User> admins = adminIds.stream().map(z -> userService.cacheQueryById(z)).collect(Collectors.toList());
            callerVO.setAdmins(admins);
        }
        int currentUserId = baseService.getCurrentUserId();
        if(permissionService.checkUserPermission(currentUserId, manageRole.getId())){
            callerVO.addPermission(PermissionEnum.ManageAble);
            callerVO.addPermission(PermissionEnum.AccessAble);
        }
        return callerVO;
    }

    @Override
    public ListData<CallerVO> queryList(CallerQueryParam queryParam, Integer pageNum, Integer pageSize) throws Exception {
        PageHelper.startPage(pageNum,pageSize);
        PageInfo<Caller> pageInfo;
        List<Caller> callerList;
        try{
            callerList = callerDao.queryList(queryParam);
            pageInfo = new PageInfo<>(callerList);
        }finally {
            PageHelper.clearPage();
        }
        List<CallerVO> voList = new ArrayList<>();
        for(Caller caller : pageInfo.getList()){
            try{
                CallerVO callerVO = translate(caller);
                voList.add(callerVO);
            }catch (Exception ex){
                logger.error("translate item info error,id:{}",caller.getId(),ex);
            }
        }
        return ListData.newInstance(voList,pageInfo.getTotal(),pageNum,pageSize);
    }

    @Override
    public int count(CallerQueryParam queryParam) throws Exception {
        return callerDao.count(queryParam);
    }

    @Override
    public String getSecretKey(Integer id) {
        return callerDao.getSecretKey(id);
    }

    @Transactional
    @Override
    public ResultCode batchGrantPermissions(PermissionGrantParam grantParam) throws Exception {
        Integer resourceId = grantParam.getResourceId();
        Caller caller = callerDao.queryById(resourceId);
        RoleTypeEnum roleTypeEnum = grantParam.getRoleType();
        Validate.notNull(caller);
        Integer roleId;
        HashSet<Integer> adminsSet = new HashSet<>();
        if(roleTypeEnum == RoleTypeEnum.CALLER_MANAGER_PERMISSION){
            Role role = roleService.queryRole(RoleTypeEnum.CALLER_MANAGER_PERMISSION,resourceId);
            roleId = role.getId();
            List<Integer> adminIds = permissionService.queryUserPermissionsByRoleId(roleId,5);
            adminsSet.addAll(adminIds);
        }else if(roleTypeEnum == RoleTypeEnum.CALLER_ACCESS_PERMISSION){
            Role role = roleService.queryRole(RoleTypeEnum.CALLER_ACCESS_PERMISSION,resourceId);
            roleId = role.getId();
        }else {
            throw new Exception();
        }
        List<Integer> departmentIdList = grantParam.getDepartmentsPermissions();
        List<Integer> userIdList = grantParam.getUsersPermissions();
        if(CollectionUtils.isNotEmpty(departmentIdList)){
            for (Integer tempDepartmentId : departmentIdList) {
                Validate.isTrue(roleTypeEnum == RoleTypeEnum.CALLER_ACCESS_PERMISSION);
                permissionService.grantPermission(tempDepartmentId, OwnerTypeEnum.DEPARTMENT, roleId);
            }
        }
        if(CollectionUtils.isNotEmpty(userIdList)){
            if(roleTypeEnum == RoleTypeEnum.CALLER_MANAGER_PERMISSION){
                adminsSet.addAll(userIdList);
            }
            if(adminsSet.size() > 3){
                return ResultCode.grantPermissionAdminExceedLimit;
            }
            for (Integer userId : userIdList) {
                permissionService.grantPermission(userId, OwnerTypeEnum.USER, roleId);
            }
        }
        return ResultCode.success;
    }

    @Override
    public ResultCode releasePermission(PermissionReleaseParam releaseParam) throws Exception {
        int currentUserId = baseService.getCurrentUserId();
        Integer resourceId = releaseParam.getResourceId();
        Integer permissionId = releaseParam.getPermissionId();
        Permission permission = permissionService.queryById(permissionId);
        Integer ownerId = permission.getOwnerId();
        Integer roleId = permission.getRoleId();
        if(releaseParam.getRoleType() == RoleTypeEnum.CALLER_MANAGER_PERMISSION){
            List<Integer> adminIds = permissionDao.queryUserPermissionsByRoleId(roleId,3);
            if(adminIds.size() <= 1){
                return ResultCode.releasePermissionAdminAtLeastOne;
            }
        }
        if(ownerId == currentUserId){
            return ResultCode.releasePermissionCurrentNotAllowed;
        }
        Role role = roleService.queryById(roleId);
        Validate.isTrue(role.getResourceId().intValue() == resourceId.intValue());
        permissionService.releasePermission(permissionId);
        return ResultCode.success;
    }
}
