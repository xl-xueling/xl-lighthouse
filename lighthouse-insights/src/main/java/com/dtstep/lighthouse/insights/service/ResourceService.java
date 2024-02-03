package com.dtstep.lighthouse.insights.service;

import com.dtstep.lighthouse.commonv2.insights.ResultCode;
import com.dtstep.lighthouse.common.modal.RolePair;
import com.dtstep.lighthouse.common.enums.OwnerTypeEnum;
import com.dtstep.lighthouse.common.enums.RoleTypeEnum;
import com.dtstep.lighthouse.common.modal.ResourceDto;

public interface ResourceService {

    RolePair addResourceCallback(ResourceDto resource);

    void updateResourcePidCallback(ResourceDto resource);

    ResultCode deleteResourceCallback(ResourceDto resource);

    int grantPermission(Integer ownerId, OwnerTypeEnum ownerTypeEnum, Integer resourceId,RoleTypeEnum roleTypeEnum);

    int releasePermission(Integer ownerId, OwnerTypeEnum ownerTypeEnum, Integer resourceId,RoleTypeEnum roleTypeEnum);

    ResourceDto queryByRoleId(Integer roleId);

}
