package com.dtstep.lighthouse.insights.service;

import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.insights.dto.PermissionDto;
import com.dtstep.lighthouse.insights.dto.PermissionListQueryParam;
import com.dtstep.lighthouse.insights.dto.PermissionQueryParam;
import com.dtstep.lighthouse.insights.enums.OwnerTypeEnum;
import com.dtstep.lighthouse.insights.enums.ResourceTypeEnum;
import com.dtstep.lighthouse.insights.enums.RoleTypeEnum;
import com.dtstep.lighthouse.insights.modal.Permission;

import java.util.List;

public interface PermissionService {

    int create(Permission permission);

    int delete(PermissionQueryParam queryParam);

    int batchCreate(List<Permission> permissionList);

    boolean checkUserPermission(Integer userId, Integer roleId);

    List<Integer> queryUserPermissionsByRoleId(Integer roleId, Integer limit);

    List<Permission> queryUserManagePermission(Integer userId,Integer limit);

    ListData<PermissionDto> queryList(PermissionQueryParam queryParam,Integer pageNum,Integer pageSize);

    boolean existPermission(Integer ownerId, OwnerTypeEnum ownerType, Integer roleId);

    int grantPermission(Integer ownerId, OwnerTypeEnum ownerTypeEnum, Integer roleId);

    int releasePermission(Integer ownerId, OwnerTypeEnum ownerTypeEnum, Integer roleId);
}
