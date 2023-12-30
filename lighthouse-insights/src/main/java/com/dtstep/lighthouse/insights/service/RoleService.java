package com.dtstep.lighthouse.insights.service;

import com.dtstep.lighthouse.insights.enums.RoleTypeEnum;
import com.dtstep.lighthouse.insights.modal.Role;

public interface RoleService {

    void initRole();

    int create(Role role);

    Role queryRole(RoleTypeEnum roleTypeEnum,Integer resourceId);

    boolean isRoleExist(RoleTypeEnum roleTypeEnum,Integer resourceId);
}
