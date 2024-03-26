package com.dtstep.lighthouse.insights.service.impl;
/*
 * Copyright (C) 2022-2024 XueLing.雪灵
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
import com.dtstep.lighthouse.insights.dao.DepartmentDao;
import com.dtstep.lighthouse.common.modal.TreeNode;
import com.dtstep.lighthouse.common.enums.ResourceTypeEnum;
import com.dtstep.lighthouse.common.modal.Department;
import com.dtstep.lighthouse.common.modal.ResourceDto;
import com.dtstep.lighthouse.common.modal.Role;
import com.dtstep.lighthouse.common.modal.Domain;
import com.dtstep.lighthouse.insights.service.*;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private RoleService roleService;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private DomainService domainService;

    @Autowired
    private PermissionService permissionService;

    @Transactional
    @Override
    public int create(Department department) {
        Date date = new Date();
        department.setUpdateTime(date);
        department.setCreateTime(date);
        departmentDao.insert(department);
        int departmentId = department.getId();
        List<Role> roleList = new ArrayList<>();
        Integer resourcePid;
        if(department.getPid() == 0){
            resourcePid = domainService.queryDefault().getId();
            resourceService.addResourceCallback(ResourceDto.newResource(ResourceTypeEnum.Department,departmentId,ResourceTypeEnum.Domain,resourcePid));
        }else{
            resourcePid = department.getPid();
            resourceService.addResourceCallback(ResourceDto.newResource(ResourceTypeEnum.Department,departmentId,ResourceTypeEnum.Department,resourcePid));
        }
        return department.getId();
    }

    @Transactional
    @Override
    public int update(Department department) {
        int result = departmentDao.update(department);
        Integer resourcePid;
        if(department.getPid() == 0){
            resourcePid = domainService.queryDefault().getId();
            resourceService.updateResourcePidCallback(ResourceDto.newResource(ResourceTypeEnum.Department,department.getId(),ResourceTypeEnum.Domain,resourcePid));
        }else{
            resourcePid = department.getPid();
            resourceService.updateResourcePidCallback(ResourceDto.newResource(ResourceTypeEnum.Department,department.getId(),ResourceTypeEnum.Department,resourcePid));
        }
        return result;
    }

    @Transactional
    @Override
    public int delete(Department department) {
        Validate.notNull(department);
        int result = departmentDao.deleteById(department.getId());

        Integer resourcePid;
        if(department.getPid() == 0){
            Domain domain = domainService.queryDefault();
            resourcePid = domain.getId();
            resourceService.deleteResourceCallback(ResourceDto.newResource(ResourceTypeEnum.Department,department.getId(),ResourceTypeEnum.Domain,resourcePid));
        }else{
            resourcePid = department.getPid();
            resourceService.deleteResourceCallback(ResourceDto.newResource(ResourceTypeEnum.Department,department.getId(),ResourceTypeEnum.Department,resourcePid));
        }
        return result;
    }

    @Override
    public Department queryById(Integer id) {
        return departmentDao.queryById(id);
    }

    @Override
    public List<TreeNode> getStructure() {
        List<Department> departmentList = queryAll();
        HashMap<Integer,TreeNode> departmentMap = new HashMap<>();
        for (Department department : departmentList) {
            TreeNode treeNode = new TreeNode(department.getName(),department.getId());
            departmentMap.put(department.getId(),treeNode);
        }
        List<TreeNode> nodeList = new ArrayList<>();
        for(Department department:departmentList){
            TreeNode currentNode = departmentMap.get(department.getId());
            int pid = department.getPid();
            if(pid == 0){
                nodeList.add(currentNode);
            }else{
                TreeNode parentNode = departmentMap.get(pid);
                if(parentNode != null){
                    List<TreeNode> children = (parentNode.getChildren() == null ? new ArrayList<>() : parentNode.getChildren());
                    children.add(currentNode);
                    parentNode.setChildren(children);
                }else{
                    nodeList.add(currentNode);
                }
            }
        }
        return nodeList;
    }

    @Override
    public List<Department> queryAll() {
        return departmentDao.queryAll();
    }

    @Override
    public int getLevel(Integer id) {
        return departmentDao.getLevel(id);
    }

    @Override
    public int getChildLevel(Integer pid) {
        return departmentDao.getChildLevel(pid);
    }

    @Override
    public int countChildByPid(Integer pid) {
        return departmentDao.countChildByPid(pid);
    }

    @Override
    public String getFullPath(Integer id) {
        return departmentDao.getFullPath(id);
    }
}
