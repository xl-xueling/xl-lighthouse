package com.dtstep.lighthouse.insights.service.impl;

import com.dtstep.lighthouse.insights.dao.RoleDao;
import com.dtstep.lighthouse.insights.enums.RoleTypeEnum;
import com.dtstep.lighthouse.insights.modal.Role;
import com.dtstep.lighthouse.insights.service.RoleService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;

    @Override
    public void initRole() {
        if(!isRoleExist(RoleTypeEnum.DEPARTMENT_MANAGE_PERMISSION,0)){
            Role fullManageRole = new Role(RoleTypeEnum.DEPARTMENT_MANAGE_PERMISSION,0,0);
            create(fullManageRole);
        }
        if(!isRoleExist(RoleTypeEnum.DEPARTMENT_ACCESS_PERMISSION,0)){
            Role fullAccessRole = new Role(RoleTypeEnum.DEPARTMENT_ACCESS_PERMISSION,0,0);
            create(fullAccessRole);
        }
        if(!isRoleExist(RoleTypeEnum.OPT_MANAGE_PERMISSION,0)){
            Role parentRole = roleDao.queryRole(RoleTypeEnum.DEPARTMENT_MANAGE_PERMISSION,0);
            Role optManageRole = new Role(RoleTypeEnum.OPT_MANAGE_PERMISSION,0, parentRole.getId());
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
    public void batchCreate(List<Role> list) {
        if(CollectionUtils.isEmpty(list)){
            return;
        }
        LocalDateTime localDateTime = LocalDateTime.now();
        list.forEach(z -> {
            z.setCreateTime(localDateTime);
            z.setUpdateTime(localDateTime);
        });
        roleDao.batchInsert(list);
    }

    @Override
    public Role queryRole(RoleTypeEnum roleTypeEnum, Integer projectId) {
        return roleDao.queryRole(roleTypeEnum,projectId);
    }

    @Override
    public int update(Role role) {
        return roleDao.update(role);
    }

    @Override
    public boolean isChildRoleExist(Integer pid) {
        return roleDao.isChildRoleExist(pid);
    }

    @Override
    public int deleteById(Integer id) {
        return roleDao.deleteById(id);
    }
}
