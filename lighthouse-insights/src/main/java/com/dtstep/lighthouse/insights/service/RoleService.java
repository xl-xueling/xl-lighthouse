package com.dtstep.lighthouse.insights.service;

import com.dtstep.lighthouse.common.enums.RoleTypeEnum;
import com.dtstep.lighthouse.insights.modal.Role;

import java.util.List;

public interface RoleService {

    int create(Role role);

    Role queryById(Integer id);

    Role queryRole(RoleTypeEnum roleTypeEnum, Integer resourceId);

    int deleteById(Integer id);

    int update(Role role);

    void batchCreate(List<Role> list);

    Role cacheQueryRole(RoleTypeEnum roleTypeEnum, Integer resourceId);

    boolean isRoleExist(RoleTypeEnum roleTypeEnum,Integer resourceId);

    boolean isChildRoleExist(Integer pid);

    List<Role> queryListByPid(Integer pid,Integer pageNum,Integer pageSize);
}
