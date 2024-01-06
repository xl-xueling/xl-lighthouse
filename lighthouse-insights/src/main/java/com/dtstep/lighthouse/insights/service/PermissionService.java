package com.dtstep.lighthouse.insights.service;

import com.dtstep.lighthouse.insights.dto.PermissionQueryParam;
import com.dtstep.lighthouse.insights.enums.OwnerTypeEnum;
import com.dtstep.lighthouse.insights.enums.RoleTypeEnum;
import com.dtstep.lighthouse.insights.modal.Permission;

import java.util.List;

public interface PermissionService {

    int create(Permission permission);

    int delete(PermissionQueryParam queryParam);

    void batchCreate(List<Permission> permissionList);

    boolean checkUserPermission(Integer userId, Integer roleId);

    List<Integer> queryUserPermissionsByRoleId(Integer roleId, Integer limit);

    List<Permission> queryUserManagePermission(Integer userId,Integer limit);

    boolean existPermission(Integer ownerId, OwnerTypeEnum ownerType, Integer roleId);

    int grantPermission(Integer ownerId, OwnerTypeEnum ownerTypeEnum, Integer roleId);

    int releasePermission(Integer ownerId, OwnerTypeEnum ownerTypeEnum, Integer roleId);
}
