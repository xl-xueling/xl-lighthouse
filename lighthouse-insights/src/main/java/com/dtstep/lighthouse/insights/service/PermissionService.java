package com.dtstep.lighthouse.insights.service;

import com.dtstep.lighthouse.insights.enums.OwnerTypeEnum;
import com.dtstep.lighthouse.insights.modal.Permission;

public interface PermissionService {

    int create(Permission permission);

    boolean hasPermission(Integer ownerId, OwnerTypeEnum ownerType, Integer roleId);
}
