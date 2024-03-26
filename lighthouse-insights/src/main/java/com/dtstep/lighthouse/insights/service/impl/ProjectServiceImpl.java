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
import com.dtstep.lighthouse.common.constant.SysConst;
import com.dtstep.lighthouse.common.entity.group.GroupExtEntity;
import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.enums.*;
import com.dtstep.lighthouse.common.modal.*;
import com.dtstep.lighthouse.common.entity.ListData;
import com.dtstep.lighthouse.common.entity.ResultCode;
import com.dtstep.lighthouse.core.builtin.BuiltinLoader;
import com.dtstep.lighthouse.insights.dao.*;
import com.dtstep.lighthouse.insights.dto.*;
import com.dtstep.lighthouse.insights.service.*;
import com.dtstep.lighthouse.insights.vo.ProjectVO;
import com.dtstep.lighthouse.common.entity.ServiceResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    private static final Logger logger = LoggerFactory.getLogger(ProjectServiceImpl.class);

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private BaseService baseService;

    @Autowired
    private GroupDao groupDao;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private UserService userService;

    @Autowired
    private StatDao statDao;

    @Autowired
    private RelationService relationService;

    @Autowired
    private RelationDao relationDao;

    @Transactional
    @Override
    public ServiceResult<Integer> create(Project project){
        LocalDateTime localDateTime = LocalDateTime.now();
        project.setUpdateTime(localDateTime);
        project.setCreateTime(localDateTime);
        projectDao.insert(project);
        int id = project.getId();
        Integer departmentId = project.getDepartmentId();
        RolePair rolePair = resourceService.addResourceCallback(ResourceDto.newResource(ResourceTypeEnum.Project,id,ResourceTypeEnum.Department,departmentId));
        Integer manageRoleId = rolePair.getManageRoleId();
        int currentUserId = baseService.getCurrentUserId();
        Permission adminPermission = new Permission(currentUserId,OwnerTypeEnum.USER,manageRoleId);
        permissionService.create(adminPermission);
        return ServiceResult.success(project.getId());
    }

    @Override
    public ResultCode batchGrantPermissions(PermissionGrantParam grantParam) throws Exception{
        Integer resourceId = grantParam.getResourceId();
        Project project = projectDao.queryById(resourceId);
        RoleTypeEnum roleTypeEnum = grantParam.getRoleType();
        Validate.notNull(project);
        Integer roleId;
        HashSet<Integer> adminsSet = new HashSet<>();
        if(roleTypeEnum == RoleTypeEnum.PROJECT_MANAGE_PERMISSION){
            Role role = roleService.queryRole(RoleTypeEnum.PROJECT_MANAGE_PERMISSION,resourceId);
            roleId = role.getId();
            List<Integer> adminIds = permissionService.queryUserPermissionsByRoleId(roleId,5);
            adminsSet.addAll(adminIds);
        }else if(roleTypeEnum == RoleTypeEnum.PROJECT_ACCESS_PERMISSION){
            Role role = roleService.queryRole(RoleTypeEnum.PROJECT_ACCESS_PERMISSION,resourceId);
            roleId = role.getId();
        }else {
            throw new Exception();
        }
        if(roleTypeEnum == RoleTypeEnum.PROJECT_ACCESS_PERMISSION && project.getPrivateType() == PrivateTypeEnum.Public){
            return ResultCode.grantPermissionPublicLimit;
        }
        List<Integer> departmentIdList = grantParam.getDepartmentsPermissions();
        List<Integer> userIdList = grantParam.getUsersPermissions();
        if(CollectionUtils.isNotEmpty(departmentIdList)){
            for(int i=0;i<departmentIdList.size();i++){
                Integer tempDepartmentId = departmentIdList.get(i);
                Validate.isTrue(roleTypeEnum == RoleTypeEnum.PROJECT_ACCESS_PERMISSION);
                permissionService.grantPermission(tempDepartmentId,OwnerTypeEnum.DEPARTMENT,roleId);
            }
        }
        if(CollectionUtils.isNotEmpty(userIdList)){
            if(roleTypeEnum == RoleTypeEnum.PROJECT_MANAGE_PERMISSION){
                adminsSet.addAll(userIdList);
            }
            if(adminsSet.size() > 3){
                return ResultCode.grantPermissionAdminExceedLimit;
            }
            for(int i=0;i<userIdList.size();i++){
                Integer userId = userIdList.get(i);
                permissionService.grantPermission(userId,OwnerTypeEnum.USER,roleId);
            }
        }
        return ResultCode.success;
    }

    @Override
    public ResultCode releasePermission(PermissionReleaseParam releaseParam) throws Exception {
        int currentUserId = baseService.getCurrentUserId();
        Integer resourceId = releaseParam.getResourceId();
        Integer permissionId = releaseParam.getPermissionId();
        Permission permission = permissionService.queryById(permissionId);
        Integer ownerId = permission.getOwnerId();
        Integer roleId = permission.getRoleId();
        if(releaseParam.getRoleType() == RoleTypeEnum.PROJECT_MANAGE_PERMISSION){
            List<Integer> adminIds = permissionService.queryUserPermissionsByRoleId(roleId,3);
            if(adminIds.size() <= 1){
                return ResultCode.releasePermissionAdminAtLeastOne;
            }
        }
        if(ownerId == currentUserId){
            return ResultCode.releasePermissionCurrentNotAllowed;
        }
        Role role = roleService.queryById(roleId);
        Validate.isTrue(role.getResourceId().intValue() == resourceId.intValue());
        permissionService.releasePermission(permissionId);
        return ResultCode.success;
    }

    @Transactional
    @Override
    public int update(Project project) {
        resourceService.updateResourcePidCallback(ResourceDto.newResource(ResourceTypeEnum.Project,project.getId(),ResourceTypeEnum.Department,project.getDepartmentId()));
        return projectDao.update(project);
    }

    @Override
    public ProjectVO queryById(Integer id) {
        Project project;
        if(BuiltinLoader.isBuiltinProject(id)){
            project = BuiltinLoader.getBuiltinProject();
        }else{
            project = projectDao.queryById(id);
        }
        if(project == null){
            return null;
        }
        return translate(project);
    }

    @Override
    @Cacheable(value = "NormalPeriod",key = "#targetClass + '_' + 'queryById' + '_' + #id",cacheManager = "caffeineCacheManager",unless = "#result == null")
    public ProjectVO cacheQueryById(Integer id) {
        return queryById(id);
    }

    private ProjectVO translate(Project project){
        ProjectVO projectVO = new ProjectVO(project);
        if(BuiltinLoader.isBuiltinProject(project.getId())){
            projectVO.addPermission(PermissionEnum.AccessAble);
        }else{
            int currentUserId = baseService.getCurrentUserId();
            Role manageRole = roleService.cacheQueryRole(RoleTypeEnum.PROJECT_MANAGE_PERMISSION, project.getId());
            Role accessRole = roleService.cacheQueryRole(RoleTypeEnum.PROJECT_ACCESS_PERMISSION, project.getId());
            List<Integer> adminIds = permissionService.queryUserPermissionsByRoleId(manageRole.getId(),3);
            if(CollectionUtils.isNotEmpty(adminIds)){
                List<User> admins = adminIds.stream().map(z -> userService.cacheQueryById(z)).collect(Collectors.toList());
                projectVO.setAdmins(admins);
            }
            if(permissionService.checkUserPermission(currentUserId, manageRole.getId())){
                projectVO.addPermission(PermissionEnum.ManageAble);
                projectVO.addPermission(PermissionEnum.AccessAble);
            }else if(project.getPrivateType() == PrivateTypeEnum.Public
                    || permissionService.checkUserPermission(currentUserId, accessRole.getId())){
                projectVO.addPermission(PermissionEnum.AccessAble);
            }
        }
        return projectVO;
    }

    @Override
    public List<ProjectVO> queryByIds(List<Integer> ids) {
        ProjectQueryParam queryParam = new ProjectQueryParam();
        queryParam.setIds(ids);
        List<Project> projectList = projectDao.queryList(queryParam);
        List<ProjectVO> voList = new ArrayList<>();
        for(Project project : projectList){
            ProjectVO projectVO = translate(project);
            voList.add(projectVO);
        }
        return voList;
    }

    @Override
    public ListData<ProjectVO> queryList(ProjectQueryParam queryParam, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<ProjectVO> dtoList = new ArrayList<>();
        PageInfo<Project> pageInfo;
        try{
            List<Project> projectList = projectDao.queryList(queryParam);
            pageInfo = new PageInfo<>(projectList);
        }finally {
            PageHelper.clearPage();
        }
        for(Project project : pageInfo.getList()){
            ProjectVO projectVO;
            try{
                projectVO = translate(project);
                dtoList.add(projectVO);
            }catch (Exception ex){
                logger.error("translate item info error,itemId:{}!",project.getId(),ex);
            }
        }
        return ListData.newInstance(dtoList,pageInfo.getTotal(),pageNum,pageSize);
    }

    @Override
    public int count(ProjectQueryParam queryParam) {
        return projectDao.count(queryParam);
    }

    @Override
    public ServiceResult<Integer> deleteById(Integer id) {
        Project project = projectDao.queryById(id);
        GroupQueryParam queryParam = new GroupQueryParam();
        queryParam.setProjectId(id);
        int groupCount = groupDao.count(queryParam);
        if(groupCount > 0){
            return ServiceResult.result(ResultCode.projectDelErrorGroupExist);
        }
        int result = projectDao.deleteById(id);
        resourceService.deleteResourceCallback(ResourceDto.newResource(ResourceTypeEnum.Project,project.getId(),ResourceTypeEnum.Department,project.getDepartmentId()));
        return ServiceResult.success(result);
    }

    @Override
    public TreeNode getStructure(Project project) throws Exception{
        Integer id = project.getId();
        TreeNode rootNode = new TreeNode(project.getTitle(),project.getId(),"project");
        HashMap<String,TreeNode> nodeMap = new HashMap<>();
        nodeMap.put("project_"+project.getId(),rootNode);
        List<Group> groupList;
        if(BuiltinLoader.isBuiltinProject(project.getId())){
            List<GroupExtEntity> groupExtEntities = BuiltinLoader.getAllGroups();
            groupList = groupExtEntities.stream().map(z -> (Group)z).collect(Collectors.toList());
        }else{
            groupList = groupDao.queryByProjectId(id);
        }
        for(Group group : groupList){
            TreeNode groupNode = new TreeNode(group.getToken(),group.getId(),"group");
            nodeMap.put("group_"+group.getId(),groupNode);
            rootNode.addChild(groupNode);
        }
        List<Stat> statList;
        if(BuiltinLoader.isBuiltinProject(project.getId())){
            List<StatExtEntity> statExtEntities = BuiltinLoader.getAllStats();
            statList = statExtEntities.stream().map(z -> (Stat)z).collect(Collectors.toList());
        }else{
            statList = statDao.queryByProjectId(id);
        }
        for(Stat stat : statList){
            TreeNode statNode = new TreeNode(stat.getTitle(),stat.getId(),"stat");
            TreeNode parentNode = nodeMap.get("group_"+stat.getGroupId());
            parentNode.addChild(statNode);
        }
        return rootNode;
    }

    @Override
    public ResultCode star(Project project) {
        int currentUserId = baseService.getCurrentUserId();
        RelationQueryParam countParam = new RelationQueryParam();
        countParam.setSubjectId(currentUserId);
        countParam.setRelationType(RelationTypeEnum.UserStarProjectRelation);
        int count = relationService.count(countParam);
        if(count > SysConst.USER_STAR_PROJECT_LIMIT){
            return ResultCode.userStarProjectLimitExceed;
        }
        Relation relation = new Relation();
        relation.setSubjectId(currentUserId);
        relation.setRelationType(RelationTypeEnum.UserStarProjectRelation);
        relation.setResourceId(project.getId());
        relation.setResourceType(ResourceTypeEnum.Project);
        return relationService.create(relation);
    }

    @Override
    public ResultCode unStar(Project project) {
        int currentUserId = baseService.getCurrentUserId();
        RelationDeleteParam relationDeleteParam = new RelationDeleteParam();
        relationDeleteParam.setSubjectId(currentUserId);
        relationDeleteParam.setRelationType(RelationTypeEnum.UserStarProjectRelation);
        relationDeleteParam.setResourceId(project.getId());
        relationDeleteParam.setResourceType(ResourceTypeEnum.Project);
        relationService.delete(relationDeleteParam);
        return ResultCode.success;
    }

    @Override
    public List<ProjectVO> queryStarList() {
        int currentUserId = baseService.getCurrentUserId();
        List<Relation> relationList = relationDao.queryList(currentUserId,RelationTypeEnum.UserStarProjectRelation);
        List<Integer> ids = relationList.stream().map(Relation::getResourceId).collect(Collectors.toList());
        List<ProjectVO> voList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(ids)){
            ProjectQueryParam queryParam = new ProjectQueryParam();
            queryParam.setIds(ids);
            List<Project> projectList = projectDao.queryList(queryParam);
            for(Project project : projectList){
                try{
                    ProjectVO projectVO = translate(project);
                    voList.add(projectVO);
                }catch (Exception ex){
                    logger.error("translate item info error,id:{}",project.getId(),ex);
                }
            }
        }
        voList.sort(Comparator.comparingInt(e -> ids.indexOf(e.getId())));
        return voList;
    }


}
