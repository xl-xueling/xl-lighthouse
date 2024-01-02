package com.dtstep.lighthouse.insights.service;

import com.dtstep.lighthouse.insights.enums.OwnerTypeEnum;
import com.dtstep.lighthouse.insights.modal.Permission;

import java.util.List;

public interface PermissionService {

    int create(Permission permission);

    void batchCreate(List<Permission> permissionList);

    boolean hasPermission(Integer ownerId, OwnerTypeEnum ownerType, Integer roleId);
}
