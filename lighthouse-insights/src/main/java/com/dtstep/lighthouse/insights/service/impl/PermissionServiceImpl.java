package com.dtstep.lighthouse.insights.service.impl;

import com.dtstep.lighthouse.insights.dao.PermissionDao;
import com.dtstep.lighthouse.insights.modal.Permission;
import com.dtstep.lighthouse.insights.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
}
