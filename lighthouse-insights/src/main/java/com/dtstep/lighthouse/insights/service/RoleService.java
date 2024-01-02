package com.dtstep.lighthouse.insights.service;

import com.dtstep.lighthouse.insights.enums.RoleTypeEnum;
import com.dtstep.lighthouse.insights.modal.Role;

import java.util.List;

public interface RoleService {

    void initRole();

    int create(Role role);

    int deleteById(Integer id);

    int update(Role role);

    void batchCreate(List<Role> list);

    Role queryRole(RoleTypeEnum roleTypeEnum,Integer resourceId);

    boolean isRoleExist(RoleTypeEnum roleTypeEnum,Integer resourceId);

    boolean isChildRoleExist(Integer pid);
}
