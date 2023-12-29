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
