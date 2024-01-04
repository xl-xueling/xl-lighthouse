package com.dtstep.lighthouse.insights.service;

import com.dtstep.lighthouse.insights.enums.OwnerTypeEnum;
import com.dtstep.lighthouse.insights.enums.RoleTypeEnum;
import com.dtstep.lighthouse.insights.modal.Permission;

import java.util.List;

public interface PermissionService {

    int create(Permission permission);

    void batchCreate(List<Permission> permissionList);

    boolean hasPermission(Integer ownerId, OwnerTypeEnum ownerType, Integer roleId);

    boolean existPermission(Integer ownerId, OwnerTypeEnum ownerType, Integer roleId);

    void grantPermission(Integer ownerId, OwnerTypeEnum ownerTypeEnum, Integer roleId);

    void releasePermission(Integer ownerId, OwnerTypeEnum ownerTypeEnum, Integer roleId);
}
