package com.dtstep.lighthouse.insights.service.impl;

import com.dtstep.lighthouse.common.enums.user.UserStateEnum;
import com.dtstep.lighthouse.common.util.Md5Util;
import com.dtstep.lighthouse.insights.dao.PermissionDao;
import com.dtstep.lighthouse.insights.enums.OwnerTypeEnum;
import com.dtstep.lighthouse.insights.enums.RoleTypeEnum;
import com.dtstep.lighthouse.insights.modal.Department;
import com.dtstep.lighthouse.insights.modal.Permission;
import com.dtstep.lighthouse.insights.modal.User;
import com.dtstep.lighthouse.insights.service.DepartmentService;
import com.dtstep.lighthouse.insights.service.PermissionService;
import com.dtstep.lighthouse.insights.service.RoleService;
import com.dtstep.lighthouse.insights.service.UserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionDao permissionDao;

    @Autowired
    private UserService userService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private RoleService roleService;

    @Override
    public int create(Permission permission) {
        LocalDateTime localDateTime = LocalDateTime.now();
        permission.setCreateTime(localDateTime);
        permission.setUpdateTime(localDateTime);
        permissionDao.insert(permission);
        return 0;
    }

    @Override
    public void batchCreate(List<Permission> permissionList) {
        if(CollectionUtils.isEmpty(permissionList)){
            return;
        }
        LocalDateTime localDateTime = LocalDateTime.now();
        permissionList.forEach(z -> {
            z.setCreateTime(localDateTime);
            z.setUpdateTime(localDateTime);
        });
        permissionDao.batchInsert(permissionList);
    }

    @Override
    public boolean hasPermission(Integer ownerId, OwnerTypeEnum ownerType, Integer roleId) {
        return permissionDao.hasPermission(ownerId,ownerType,roleId);
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
            Validate.isTrue(user.getState() == UserStateEnum.USR_NORMAL);
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

    @Override
    public int releasePermission(Integer ownerId, OwnerTypeEnum ownerTypeEnum, Integer roleId) {
        Validate.notNull(ownerId);
        Validate.notNull(ownerTypeEnum);
        Validate.notNull(roleId);
        return permissionDao.delete(ownerId, ownerTypeEnum, roleId);
    }
}
