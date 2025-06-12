package com.dtstep.lighthouse.insights.service.impl;
/*
 * Copyright (C) 2022-2025 XueLing.雪灵
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import com.dtstep.lighthouse.insights.dao.RoleDao;
import com.dtstep.lighthouse.common.enums.RoleTypeEnum;
import com.dtstep.lighthouse.common.modal.Role;
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
    @Cacheable(value = "NormalPeriod",key = "#targetClass + '_' + 'queryById' + '_' + #id",cacheManager = "caffeineCacheManager",unless = "#result == null")
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
    @Cacheable(value = "NormalPeriod",key = "#targetClass + '_' + 'queryRole' + '_' + #roleTypeEnum.roleType + '_' + #resourceId",cacheManager = "caffeineCacheManager",unless = "#result == null")
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
