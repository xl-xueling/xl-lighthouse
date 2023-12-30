package com.dtstep.lighthouse.insights.service.impl;

import com.dtstep.lighthouse.insights.dao.RoleDao;
import com.dtstep.lighthouse.insights.enums.RoleTypeEnum;
import com.dtstep.lighthouse.insights.modal.Role;
import com.dtstep.lighthouse.insights.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;

    @Override
    public void initRole() {
        if(!isRoleExist(RoleTypeEnum.FULL_MANAGE_PERMISSION,0)){
            Role fullManageRole = new Role(RoleTypeEnum.FULL_MANAGE_PERMISSION,0);
            create(fullManageRole);
        }
        if(!isRoleExist(RoleTypeEnum.FULL_ACCESS_PERMISSION,0)){
            Role fullAccessRole = new Role(RoleTypeEnum.FULL_ACCESS_PERMISSION,0);
            create(fullAccessRole);
        }
        if(!isRoleExist(RoleTypeEnum.OPT_MANAGE_PERMISSION,0)){
            Role optManageRole = new Role(RoleTypeEnum.OPT_MANAGE_PERMISSION,0);
            create(optManageRole);
        }
    }

    @Override
    public boolean isRoleExist(RoleTypeEnum roleTypeEnum, Integer resourceId) {
        return roleDao.isRoleExist(roleTypeEnum,resourceId);
    }

    @Override
    public int create(Role role) {
        LocalDateTime localDateTime = LocalDateTime.now();
        role.setCreateTime(localDateTime);
        role.setUpdateTime(localDateTime);
        return roleDao.insert(role);
    }

    @Override
    public Role queryRole(RoleTypeEnum roleTypeEnum, Integer projectId) {
        return roleDao.queryRole(roleTypeEnum,projectId);
    }
}
