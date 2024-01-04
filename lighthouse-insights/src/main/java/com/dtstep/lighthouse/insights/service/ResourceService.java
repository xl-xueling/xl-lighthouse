package com.dtstep.lighthouse.insights.service;

import com.dtstep.lighthouse.insights.dto.RolePair;
import com.dtstep.lighthouse.insights.enums.OwnerTypeEnum;
import com.dtstep.lighthouse.insights.enums.RoleTypeEnum;
import com.dtstep.lighthouse.insights.modal.Resource;
import org.javatuples.Pair;

public interface ResourceService {

    RolePair addResourceCallback(Resource resource);

    void updateResourcePidCallback(Resource resource);

    void deleteResourceCallback(Resource resource);

    int grantPermission(Integer ownerId, OwnerTypeEnum ownerTypeEnum, Integer resourceId,RoleTypeEnum roleTypeEnum);

    int releasePermission(Integer ownerId, OwnerTypeEnum ownerTypeEnum, Integer resourceId,RoleTypeEnum roleTypeEnum);
}
