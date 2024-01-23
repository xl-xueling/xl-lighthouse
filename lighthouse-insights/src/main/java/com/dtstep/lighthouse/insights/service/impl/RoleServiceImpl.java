package com.dtstep.lighthouse.insights.service.impl;

import com.dtstep.lighthouse.insights.dao.RoleDao;
import com.dtstep.lighthouse.common.enums.RoleTypeEnum;
import com.dtstep.lighthouse.insights.modal.Role;
import com.dtstep.lighthouse.insights.service.RoleService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;

    @Override
    public Role queryById(Integer id) {
        return roleDao.queryById(id);
    }

    @Override
    @Cacheable(value = "LongPeriod",key = "#targetClass + '_' + 'queryById' + '_' + #id",cacheManager = "caffeineCacheManager",unless = "#result == null")
    public Role cacheQueryById(Integer id) {
        return roleDao.queryById(id);
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
    @Cacheable(value = "LongPeriod",key = "#targetClass + '_' + 'queryRole' + '_' + #roleTypeEnum.roleType + '_' + #resourceId",cacheManager = "caffeineCacheManager",unless = "#result == null")
    public Role cacheQueryRole(RoleTypeEnum roleTypeEnum, Integer resourceId) {
        return roleDao.queryRole(roleTypeEnum,resourceId);
    }

    @Override
    public int update(Role role) {
        role.setUpdateTime(LocalDateTime.now());
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

    @Override
    public List<Role> queryListByPid(Integer pid, Integer pageNum, Integer pageSize) {
        return roleDao.queryListByPid(pid,pageNum,pageSize);
    }

    @Override
    public Role queryRole(RoleTypeEnum roleTypeEnum, Integer resourceId) {
        return roleDao.queryRole(roleTypeEnum,resourceId);
    }
}
