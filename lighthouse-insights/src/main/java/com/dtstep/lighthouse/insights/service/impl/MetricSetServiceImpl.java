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
import com.dtstep.lighthouse.common.constant.SysConst;
import com.dtstep.lighthouse.common.enums.*;
import com.dtstep.lighthouse.common.random.RandomID;
import com.dtstep.lighthouse.common.modal.*;
import com.dtstep.lighthouse.common.util.Md5Util;
import com.dtstep.lighthouse.common.entity.ListData;
import com.dtstep.lighthouse.common.entity.ResultCode;
import com.dtstep.lighthouse.common.util.StringUtil;
import com.dtstep.lighthouse.insights.config.SoftEdition;
import com.dtstep.lighthouse.insights.dao.*;
import com.dtstep.lighthouse.insights.dto.*;
import com.dtstep.lighthouse.insights.service.*;
import com.dtstep.lighthouse.insights.vo.MetricSetVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MetricSetServiceImpl implements MetricSetService {

    private static final Logger logger = LoggerFactory.getLogger(MetricSetServiceImpl.class);

    @Autowired
    private MetricSetDao metricSetDao;

    @Autowired
    private BaseService baseService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private PermissionDao permissionDao;

    @Autowired
    private UserService userService;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private StatService statService;

    @Autowired
    private RelationService relationService;

    @Autowired
    private DomainService domainService;

    @Autowired
    private RelationDao relationDao;

    @Autowired
    private GroupDao groupDao;

    @Autowired
    private StatDao statDao;

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private ViewDao viewDao;

    @Autowired
    private SoftEdition softEdition;

    @Transactional
    @Override
    public int create(MetricSet metricSet) {
        LocalDateTime localDateTime = LocalDateTime.now();
        metricSet.setCreateTime(localDateTime);
        metricSet.setUpdateTime(localDateTime);
        metricSet.setCreateUserId(baseService.getCurrentUserId());
        metricSetDao.insert(metricSet);
        int id = metricSet.getId();
        Domain domain = domainService.queryDefault();
        RolePair rolePair = resourceService.addResourceCallback(ResourceDto.newResource(ResourceTypeEnum.MetricSet,id,ResourceTypeEnum.Domain,domain.getId()));
        Integer manageRoleId = rolePair.getManageRoleId();
        int currentUserId = baseService.getCurrentUserId();
        Permission adminPermission = new Permission(currentUserId, OwnerTypeEnum.USER,manageRoleId);
        permissionService.create(adminPermission);
        return id;
    }


    @Transactional
    @Override
    public ResultCode batchGrantPermissions(PermissionGrantParam grantParam) throws Exception {
        Integer resourceId = grantParam.getResourceId();
        MetricSet metricSet = metricSetDao.queryById(resourceId);
        RoleTypeEnum roleTypeEnum = grantParam.getRoleType();
        Validate.notNull(metricSet);
        Integer roleId;
        HashSet<Integer> adminsSet = new HashSet<>();
        if(roleTypeEnum == RoleTypeEnum.METRIC_MANAGE_PERMISSION){
            Role role = roleService.queryRole(RoleTypeEnum.METRIC_MANAGE_PERMISSION,resourceId);
            roleId = role.getId();
            List<Integer> adminIds = permissionService.queryUserPermissionsByRoleId(roleId,5);
            adminsSet.addAll(adminIds);
        }else if(roleTypeEnum == RoleTypeEnum.METRIC_ACCESS_PERMISSION){
            Role role = roleService.queryRole(RoleTypeEnum.METRIC_ACCESS_PERMISSION,resourceId);
            roleId = role.getId();
        }else {
            throw new Exception();
        }
        if(roleTypeEnum == RoleTypeEnum.METRIC_ACCESS_PERMISSION && metricSet.getPrivateType() == PrivateTypeEnum.Public){
            return ResultCode.grantPermissionPublicLimit;
        }
        List<Integer> departmentIdList = grantParam.getDepartmentsPermissions();
        List<Integer> userIdList = grantParam.getUsersPermissions();
        if(CollectionUtils.isNotEmpty(departmentIdList)){
            for (Integer tempDepartmentId : departmentIdList) {
                Validate.isTrue(roleTypeEnum == RoleTypeEnum.METRIC_ACCESS_PERMISSION);
                permissionService.grantPermission(tempDepartmentId, OwnerTypeEnum.DEPARTMENT, roleId);
            }
        }
        if(CollectionUtils.isNotEmpty(userIdList)){
            if(roleTypeEnum == RoleTypeEnum.METRIC_MANAGE_PERMISSION){
                adminsSet.addAll(userIdList);
            }
            if(adminsSet.size() > 3){
                return ResultCode.grantPermissionAdminExceedLimit;
            }
            for (Integer userId : userIdList) {
                permissionService.grantPermission(userId, OwnerTypeEnum.USER, roleId);
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
        if(releaseParam.getRoleType() == RoleTypeEnum.METRIC_MANAGE_PERMISSION){
            List<Integer> adminIds = permissionDao.queryUserPermissionsByRoleId(roleId,3);
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

    @Override
    public int update(MetricSet metricSet) {
        LocalDateTime localDateTime = LocalDateTime.now();
        metricSet.setUpdateTime(localDateTime);
        int result = metricSetDao.update(metricSet);
        Domain domain = domainService.queryDefault();
        resourceService.updateResourcePidCallback(ResourceDto.newResource(ResourceTypeEnum.MetricSet,metricSet.getId(),ResourceTypeEnum.Domain,domain.getId()));
        return result;
    }

    private MetricSetVO translate(MetricSet metricSet){
        if(metricSet == null){
            return null;
        }
        MetricSetVO metricSetVO = new MetricSetVO(metricSet);
        Role manageRole = roleService.cacheQueryRole(RoleTypeEnum.METRIC_MANAGE_PERMISSION,metricSet.getId());
        Role accessRole = roleService.queryRole(RoleTypeEnum.METRIC_ACCESS_PERMISSION,metricSet.getId());
        int currentUserId = baseService.getCurrentUserId();
        if(permissionService.checkUserPermission(currentUserId, manageRole.getId())){
            metricSetVO.addPermission(PermissionEnum.ManageAble);
            metricSetVO.addPermission(PermissionEnum.AccessAble);
        }else if(softEdition.isOpenSource() || metricSetVO.getPrivateType() == PrivateTypeEnum.Public || permissionService.checkUserPermission(currentUserId,accessRole.getId())){
            metricSetVO.addPermission(PermissionEnum.AccessAble);
        }
        Integer userId = metricSet.getCreateUserId();
        if(userId != null){
            User user = userService.cacheQueryById(userId);
            metricSetVO.setCreateUser(user);
        }
        return metricSetVO;
    }

    @Override
    public MetricSetVO queryById(Integer id) {
        MetricSet metricSet = metricSetDao.queryById(id);
        if(metricSet == null){
            return null;
        }
        MetricSetVO metricSetVO = translate(metricSet);
        Role manageRole = roleService.cacheQueryRole(RoleTypeEnum.METRIC_MANAGE_PERMISSION,metricSet.getId());
        List<Integer> adminIds = permissionService.queryUserPermissionsByRoleId(manageRole.getId(),3);
        if(CollectionUtils.isNotEmpty(adminIds)){
            List<User> admins = adminIds.stream().map(z -> userService.cacheQueryById(z)).collect(Collectors.toList());
            metricSetVO.setAdmins(admins);
        }
        List<Relation> relationList = relationDao.queryList(metricSetVO.getId(), RelationTypeEnum.MetricSetBindRelation);
        List<MetricBindElement> elements = new ArrayList<>();
        for(Relation relation : relationList){
            MetricBindElement bindElement = new MetricBindElement();
            bindElement.setResourceId(relation.getResourceId());
            bindElement.setResourceType(relation.getResourceType());
            elements.add(bindElement);
        }
        metricSetVO.setBindElements(elements);
        return metricSetVO;
    }

    @Override
    public int binded(MetricBindParam bindParam) throws Exception {
        List<Integer> metricIds = bindParam.getMetricIds();
        List<MetricBindElement> bindElements = bindParam.getBindElements();
        List<Relation> relationList = new ArrayList<>();
        LocalDateTime localDateTime = LocalDateTime.now();
        for(Integer metricId : metricIds){
            List<Integer> projectIds = bindElements.stream().filter(x -> x.getResourceType() == ResourceTypeEnum.Project).map(MetricBindElement::getResourceId).collect(Collectors.toList());
            for(Integer projectId : projectIds){
                Project project = projectService.queryById(projectId);
                if(project == null){
                    continue;
                }
                Relation relation = new Relation();
                String hash = Md5Util.getMD5(metricId + "_" + RelationTypeEnum.MetricSetBindRelation.getRelationType() + "_" + projectId + "_" + ResourceTypeEnum.Project.getResourceType());
                boolean isExist = relationService.isExist(hash);
                if(!isExist){
                    relation.setSubjectId(metricId);
                    relation.setRelationType(RelationTypeEnum.MetricSetBindRelation);
                    relation.setResourceId(projectId);
                    relation.setResourceType(ResourceTypeEnum.Project);
                    relation.setHash(hash);
                    relation.setCreateTime(localDateTime);
                    relation.setUpdateTime(localDateTime);
                    relationList.add(relation);
                }
            }
            List<Integer> statIds = bindElements.stream().filter(x -> x.getResourceType() == ResourceTypeEnum.Stat).map(MetricBindElement::getResourceId).collect(Collectors.toList());
            for(Integer statId : statIds){
                Stat stat = statService.queryById(statId);
                if(stat == null){
                    continue;
                }
                String hash = Md5Util.getMD5(metricId + "_" + RelationTypeEnum.MetricSetBindRelation.getRelationType() + "_" + statId + "_" + ResourceTypeEnum.Stat.getResourceType());
                boolean isExist = relationService.isExist(hash);
                if(!isExist){
                    Relation relation = new Relation();
                    relation.setSubjectId(metricId);
                    relation.setRelationType(RelationTypeEnum.MetricSetBindRelation);
                    relation.setResourceId(statId);
                    relation.setResourceType(ResourceTypeEnum.Stat);
                    relation.setHash(hash);
                    relation.setCreateTime(localDateTime);
                    relation.setUpdateTime(localDateTime);
                    relationList.add(relation);
                }
            }

            List<Integer> viewIds = bindElements.stream().filter(x -> x.getResourceType() == ResourceTypeEnum.View).map(MetricBindElement::getResourceId).collect(Collectors.toList());
            for(Integer viewId : viewIds){
                View view = viewDao.queryById(viewId);
                if(view == null){
                    continue;
                }
                String hash = Md5Util.getMD5(metricId + "_" + RelationTypeEnum.MetricSetBindRelation.getRelationType() + "_" + viewId + "_" + ResourceTypeEnum.View.getResourceType());
                boolean isExist = relationService.isExist(hash);
                if(!isExist){
                    Relation relation = new Relation();
                    relation.setSubjectId(metricId);
                    relation.setRelationType(RelationTypeEnum.MetricSetBindRelation);
                    relation.setResourceId(viewId);
                    relation.setResourceType(ResourceTypeEnum.View);
                    relation.setHash(hash);
                    relation.setCreateTime(localDateTime);
                    relation.setUpdateTime(localDateTime);
                    relationList.add(relation);
                }
            }
        }
        int result = 0;
        if(CollectionUtils.isNotEmpty(relationList)){
            result = relationService.batchCreate(relationList);
        }
        return result;
    }

    @Override
    public ListData<MetricSetVO> queryList(MetricSetQueryParam queryParam, Integer pageNum, Integer pageSize) {
        queryParam.setOwnerId(baseService.getCurrentUserId());
        PageHelper.startPage(pageNum,pageSize);
        PageInfo<MetricSet> pageInfo = null;
        try{
            List<MetricSet> metricSetList = metricSetDao.queryList(queryParam);
            pageInfo = new PageInfo<>(metricSetList);
        }finally {
            PageHelper.clearPage();
        }
        List<MetricSetVO> voList = new ArrayList<>();
        for(MetricSet metricSet : pageInfo.getList()){
            try{
                MetricSetVO metricSetVO = translate(metricSet);
                voList.add(metricSetVO);
            }catch (Exception ex){
                logger.error("translate item info error,id:{}",metricSet.getId(),ex);
            }
        }
        return ListData.newInstance(voList,pageInfo.getTotal(),pageNum,pageSize);
    }

    @Override
    public TreeNode getStructure(MetricSetVO metricSet) throws Exception {
        Validate.notNull(metricSet);
        TreeNode structure = metricSet.getStructure();
        if(structure == null || CollectionUtils.isEmpty(structure.getChildren())){
            structure = combineDefaultStructure(metricSet);
            metricSet.setCustomStructure(false);
        }else{
            metricSet.setCustomStructure(true);
        }
        return structure;
    }

    private TreeNode combineDefaultStructure(MetricSetVO metricSet){
        List<String> keyList = new ArrayList<>();
        String rootKey = RandomID.id(10,keyList);
        TreeNode rootNode = new TreeNode(rootKey,metricSet.getTitle(),metricSet.getId(),"metric");
        RelationQueryParam queryParam = new RelationQueryParam();
        queryParam.setSubjectId(metricSet.getId());
        queryParam.setRelationType(RelationTypeEnum.MetricSetBindRelation);
        List<Relation> relationList = relationDao.queryJoinList(queryParam);
        List<Integer> projectIdList = relationList.stream().filter(x -> x.getResourceType() == ResourceTypeEnum.Project).map(Relation::getResourceId).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(projectIdList)){
            HashMap<String,TreeNode> nodesMap = new HashMap<>();
            List<FlatTreeNode> flatTreeNodes = projectDao.queryNodeList(projectIdList);
            for(FlatTreeNode flatNode:flatTreeNodes){
                String key = RandomID.id(10,keyList);
                TreeNode treeNode = new TreeNode(key,flatNode.getTitle(), flatNode.getId(),flatNode.getType());
                nodesMap.put(flatNode.getType() + "_" + flatNode.getId(),treeNode);
            }
            for (FlatTreeNode flatNode:flatTreeNodes) {
                TreeNode currentNode = nodesMap.get(flatNode.getType() + "_" + flatNode.getId());
                if(flatNode.getType().equals("group")){
                    TreeNode parentNode = nodesMap.get("project" + "_" + flatNode.getPid());
                    parentNode.addChild(currentNode);
                }else if(flatNode.getType().equals("stat")){
                    TreeNode parentNode = nodesMap.get("group" + "_" + flatNode.getPid());
                    parentNode.addChild(currentNode);
                }
            }
            for(TreeNode treeNode : nodesMap.values()){
                if(treeNode.getType().equals("project")){
                    rootNode.addChild(treeNode);
                }
            }
        }
        for(Relation relation : relationList){
            if(relation.getResourceType() == ResourceTypeEnum.Stat){
                String key = RandomID.id(10,keyList);
                if(StringUtil.isNotEmpty(relation.getResourceTitle())){
                    TreeNode treeNode = new TreeNode(key,relation.getResourceTitle(),relation.getResourceId(),"stat");
                    rootNode.addChild(treeNode);
                }
            }else if(relation.getResourceType() == ResourceTypeEnum.View){
                String key = RandomID.id(10,keyList);
                if(StringUtil.isNotEmpty(relation.getResourceTitle())){
                    TreeNode treeNode = new TreeNode(key,relation.getResourceTitle(),relation.getResourceId(),"view");
                    rootNode.addChild(treeNode);
                }
            }
        }
        return rootNode;
    }

    @Override
    public int bindRemove(MetricBindRemoveParam removeParam) {
        Integer relationId = removeParam.getRelationId();
        Relation relation = relationDao.queryById(relationId);
        Validate.isTrue(relation.getSubjectId().intValue() == removeParam.getId().intValue());
        return relationDao.deleteById(relationId);
    }

    @Override
    @Transactional
    public int delete(MetricSet metricSet) {
        Integer metricId = metricSet.getId();
        RelationDeleteParam userStarRelation = new RelationDeleteParam();
        userStarRelation.setRelationType(RelationTypeEnum.UserStarMetricSetRelation);
        userStarRelation.setResourceId(metricId);
        userStarRelation.setResourceType(ResourceTypeEnum.MetricSet);
        relationService.delete(userStarRelation);

        RelationDeleteParam metricBindRelation = new RelationDeleteParam();
        metricBindRelation.setRelationType(RelationTypeEnum.MetricSetBindRelation);
        metricBindRelation.setSubjectId(metricId);
        relationService.delete(metricBindRelation);
        return metricSetDao.deleteById(metricSet.getId());
    }

    @Override
    public ResultCode star(MetricSet metricSet) {
        int currentUserId = baseService.getCurrentUserId();
        RelationQueryParam countParam = new RelationQueryParam();
        countParam.setSubjectId(currentUserId);
        countParam.setRelationType(RelationTypeEnum.UserStarMetricSetRelation);
        int count = relationService.count(countParam);
        if(count > SysConst.USER_STAR_METRICSET_LIMIT){
            return ResultCode.userStarMetricLimitExceed;
        }
        Relation relation = new Relation();
        relation.setSubjectId(currentUserId);
        relation.setRelationType(RelationTypeEnum.UserStarMetricSetRelation);
        relation.setResourceId(metricSet.getId());
        relation.setResourceType(ResourceTypeEnum.MetricSet);
        return relationService.create(relation);
    }

    @Override
    public ResultCode unStar(MetricSet metricSet) {
        int currentUserId = baseService.getCurrentUserId();
        RelationDeleteParam relationDeleteParam = new RelationDeleteParam();
        relationDeleteParam.setSubjectId(currentUserId);
        relationDeleteParam.setRelationType(RelationTypeEnum.UserStarMetricSetRelation);
        relationDeleteParam.setResourceId(metricSet.getId());
        relationDeleteParam.setResourceType(ResourceTypeEnum.MetricSet);
        relationService.delete(relationDeleteParam);
        return ResultCode.success;
    }

    @Override
    public void updateStructure(MetricUpdateStructureParam updateStructureParam){
        TreeNode treeNode = updateStructureParam.getStructure();
        modifyStructure(List.of(treeNode));
        MetricSet metricSet = new MetricSet();
        metricSet.setId(updateStructureParam.getId());
        metricSet.setStructure(treeNode);
        metricSetDao.update(metricSet);
    }

    private void modifyStructure(List<TreeNode> nodeList){
        for(TreeNode treeNode : nodeList){
            if(treeNode.getType() == null || treeNode.getType().equals("project") || treeNode.getType().equals("group")){
                treeNode.setType("dir");
            }
            if(CollectionUtils.isNotEmpty(treeNode.getChildren())){
                modifyStructure(treeNode.getChildren());
            }
        }
    }

    @Override
    public ListData<Indicator> queryIndicatorList(MetricPendQueryParam queryParam, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        PageInfo<Indicator> pageInfo;
        try{
            List<Indicator> ids = metricSetDao.queryIndicatorList(queryParam.getId());
            pageInfo = new PageInfo<>(ids);
        }finally {
            PageHelper.clearPage();
        }
        return ListData.newInstance(pageInfo.getList(),pageInfo.getTotal(),pageNum,pageSize);
    }

    private static List<Integer> getCurrentStatIds(List<TreeNode> nodes) {
        List<Integer> values = new ArrayList<>();
        for (TreeNode node : nodes) {
            if(node.getType().equals("stat")){
                values.add((Integer) node.getValue());
            }
            if (CollectionUtils.isNotEmpty(node.getChildren())) {
                values.addAll(getCurrentStatIds(node.getChildren()));
            }
        }
        return values;
    }

    @Override
    public List<MetricSetVO> queryStarList() {
        int currentUserId = baseService.getCurrentUserId();
        List<Relation> relationList = relationDao.queryList(currentUserId,RelationTypeEnum.UserStarMetricSetRelation);
        List<Integer> ids = relationList.stream().map(Relation::getResourceId).collect(Collectors.toList());
        List<MetricSetVO> voList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(ids)){
            MetricSetQueryParam queryParam = new MetricSetQueryParam();
            queryParam.setIds(ids);
            List<MetricSet> metricSetList = metricSetDao.queryList(queryParam);
            for(MetricSet metricSet : metricSetList){
                try{
                    MetricSetVO metricSetVO = translate(metricSet);
                    voList.add(metricSetVO);
                }catch (Exception ex){
                    logger.error("translate item info error,id:{}",metricSet.getId(),ex);
                }
            }
        }
        voList.sort(Comparator.comparingInt(e -> ids.indexOf(e.getId())));
        return voList;
    }

    @Override
    public int count(MetricSetQueryParam queryParam) {
        return metricSetDao.count(queryParam);
    }
}
