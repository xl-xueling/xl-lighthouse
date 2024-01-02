package com.dtstep.lighthouse.insights.service.impl;

import com.dtstep.lighthouse.insights.dao.PermissionDao;
import com.dtstep.lighthouse.insights.enums.OwnerTypeEnum;
import com.dtstep.lighthouse.insights.modal.Permission;
import com.dtstep.lighthouse.insights.service.PermissionService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionDao permissionDao;

    @Override
    public int create(Permission permission) {
        LocalDateTime localDateTime = LocalDateTime.now();
        permission.setCreateTime(localDateTime);
        permission.setUpdateTime(localDateTime);
        permissionDao.insert(permission);
        return 0;
    }

    @Override
    public void batchCreate(List<Permission> permissionList) {
        if(CollectionUtils.isEmpty(permissionList)){
            return;
        }
        LocalDateTime localDateTime = LocalDateTime.now();
        permissionList.forEach(z -> {
            z.setCreateTime(localDateTime);
            z.setUpdateTime(localDateTime);
        });
        permissionDao.batchInsert(permissionList);
    }

    @Override
    public boolean hasPermission(Integer ownerId, OwnerTypeEnum ownerType, Integer roleId) {
        return permissionDao.hasPermission(ownerId,ownerType,roleId);
    }
}
