package com.dtstep.lighthouse.insights.dao;

import com.dtstep.lighthouse.insights.enums.RoleTypeEnum;
import com.dtstep.lighthouse.insights.modal.Role;

public interface RoleDao {

    int insert(Role role);

    Role queryRole(RoleTypeEnum roleTypeEnum, Integer resourceId);

    boolean isRoleExist(RoleTypeEnum roleTypeEnum,Integer resourceId);
}
