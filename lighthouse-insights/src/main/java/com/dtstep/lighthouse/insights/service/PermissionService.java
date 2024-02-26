package com.dtstep.lighthouse.insights.service;

import com.dtstep.lighthouse.common.entity.ListData;
import com.dtstep.lighthouse.insights.vo.PermissionVO;
import com.dtstep.lighthouse.insights.dto.PermissionQueryParam;
import com.dtstep.lighthouse.common.enums.OwnerTypeEnum;
import com.dtstep.lighthouse.common.modal.Permission;

import java.util.List;

public interface PermissionService {

    int create(Permission permission);

    int delete(PermissionQueryParam queryParam);

    int batchCreate(List<Permission> permissionList);

    Permission queryById(Integer id);

    boolean checkUserPermission(Integer userId, Integer roleId);

    List<Integer> queryUserPermissionsByRoleId(Integer roleId, Integer limit);

    List<Permission> queryUserManagePermission(Integer userId,Integer limit);

    ListData<PermissionVO> queryList(PermissionQueryParam queryParam, Integer pageNum, Integer pageSize);

    boolean existPermission(Integer ownerId, OwnerTypeEnum ownerType, Integer roleId);

    int grantPermission(Integer ownerId, OwnerTypeEnum ownerTypeEnum, Integer roleId);

    int releasePermission(Integer ownerId, OwnerTypeEnum ownerTypeEnum, Integer roleId);

    int releasePermission(Integer id);
}
