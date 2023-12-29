package com.dtstep.lighthouse.insights.service;

import com.dtstep.lighthouse.insights.enums.RoleTypeEnum;
import com.dtstep.lighthouse.insights.modal.Role;

public interface RoleService {

    int create(Role role);

    Role queryRole(RoleTypeEnum roleTypeEnum,Integer projectId);
}
