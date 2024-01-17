package com.dtstep.lighthouse.insights.service;

import com.dtstep.lighthouse.insights.dto_bak.RolePair;
import com.dtstep.lighthouse.insights.enums.OwnerTypeEnum;
import com.dtstep.lighthouse.common.enums.RoleTypeEnum;
import com.dtstep.lighthouse.insights.modal.Resource;

public interface ResourceService {

    RolePair addResourceCallback(Resource resource);

    void updateResourcePidCallback(Resource resource);

    void deleteResourceCallback(Resource resource);

    int grantPermission(Integer ownerId, OwnerTypeEnum ownerTypeEnum, Integer resourceId,RoleTypeEnum roleTypeEnum);

    int releasePermission(Integer ownerId, OwnerTypeEnum ownerTypeEnum, Integer resourceId,RoleTypeEnum roleTypeEnum);

    Resource queryByRoleId(Integer roleId);
}
