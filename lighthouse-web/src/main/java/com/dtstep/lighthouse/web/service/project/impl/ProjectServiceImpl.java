package com.dtstep.lighthouse.web.service.project.impl;
/*
 * Copyright (C) 2022-2023 XueLing.雪灵
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
import com.dtstep.lighthouse.common.entity.department.DepartmentEntity;
import com.dtstep.lighthouse.common.entity.department.DepartmentViewEntity;
import com.dtstep.lighthouse.common.entity.group.GroupExtEntity;
import com.dtstep.lighthouse.common.entity.list.ListViewDataObject;
import com.dtstep.lighthouse.common.entity.paging.PageEntity;
import com.dtstep.lighthouse.common.entity.project.ProjectEntity;
import com.dtstep.lighthouse.common.entity.project.ProjectViewEntity;
import com.dtstep.lighthouse.common.entity.tree.ZTreeViewNode;
import com.dtstep.lighthouse.common.entity.user.UserEntity;
import com.dtstep.lighthouse.common.enums.order.OrderTypeEnum;
import com.dtstep.lighthouse.common.enums.relations.RelationTypeEnum;
import com.dtstep.lighthouse.common.enums.role.PrivilegeTypeEnum;
import com.dtstep.lighthouse.common.util.Md5Util;
import com.dtstep.lighthouse.core.dao.ConnectionManager;
import com.dtstep.lighthouse.core.dao.DBConnection;
import com.dtstep.lighthouse.core.dao.DaoHelper;
import com.dtstep.lighthouse.core.sql.SqlBinder;
import com.dtstep.lighthouse.web.dao.ProjectDao;
import com.dtstep.lighthouse.web.manager.department.DepartmentManager;
import com.dtstep.lighthouse.web.manager.group.GroupManager;
import com.dtstep.lighthouse.web.manager.order.OrderManager;
import com.dtstep.lighthouse.web.manager.privilege.PrivilegeManager;
import com.dtstep.lighthouse.web.manager.relations.RelationManager;
import com.dtstep.lighthouse.web.param.ParamWrapper;
import com.dtstep.lighthouse.web.service.project.ProjectService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class ProjectServiceImpl implements ProjectService {

    private static final Logger logger = LoggerFactory.getLogger(ProjectServiceImpl.class);

    @Autowired
    private PrivilegeManager privilegeManager;

    @Autowired
    private GroupManager groupManager;

    @Autowired
    private DepartmentManager departmentManager;

    @Autowired
    private RelationManager relationManager;

    @Autowired
    private OrderManager orderManager;

    @Autowired
    private ProjectDao projectDao;

    @Override
    public ListViewDataObject queryListByPage(UserEntity currentUser, int page, boolean isOwner, int departmentId, String search) throws Exception {
        int totalSize = countByParam(currentUser,isOwner,departmentId,search);
        HashMap<String,Object> urlMap = new HashMap<>();
        urlMap.put("departmentId",departmentId);
        urlMap.put("owner",isOwner?1:0);
        urlMap.put("search",search);
        ListViewDataObject listObject = new ListViewDataObject();
        String baseUrl = ParamWrapper.getBaseLink("/project/list.shtml",urlMap);
        PageEntity pageEntity = new PageEntity(baseUrl, page,totalSize, SysConst._LIST_PAGE_SIZE);
        listObject.setPageEntity(pageEntity);
        List<ProjectViewEntity> viewEntityList = queryListByPage(currentUser,isOwner,departmentId,search,pageEntity);
        listObject.setDataList(viewEntityList);
        return listObject;
    }

    protected int countByParam(UserEntity currentUser,boolean isOwner, int departmentId, String search) throws Exception {
        List<Integer> departmentIdList = null;
        if(departmentId != -1){
            List<DepartmentViewEntity> departmentList = departmentManager.queryListByPid(departmentId);
            departmentIdList = departmentList.stream().map(DepartmentEntity::getId).collect(Collectors.toList());
        }
        SqlBinder.Builder builder;
        if(isOwner && !privilegeManager.isSysAdmin(currentUser)){
            int userId = currentUser.getId();
            builder = new SqlBinder.Builder()
                    .appendSegment("select count(1) from ldp_stat_project")
                    .appendWhere("1",1)
                    .appendWhereSegment("(private_flag = '0' or (id in (select relation_b from ldp_privilege where privilege_type in ('5','6') and relation_a = " + userId + ")))")
                    .appendLike("name",search)
                    .appendInExceptNull("department_id",departmentIdList);
        }else{
            builder = new SqlBinder.Builder()
                    .appendSegment("select count(1) from ldp_stat_project")
                    .appendWhere("1","1")
                    .appendLike("name",search)
                    .appendInExceptNull("department_id",departmentIdList);
        }
        SqlBinder binder = builder.create();
        return DaoHelper.sql.count(binder.toString());
    }

    protected List<ProjectViewEntity> queryListByPage(UserEntity currentUser,boolean isOwner, int departmentId, String search, PageEntity pageEntity) throws Exception {
        int userId = currentUser.getId();
        List<Integer> departmentIdList = null;
        if(departmentId != -1){
            List<DepartmentViewEntity> departmentList = departmentManager.queryListByPid(departmentId);
            departmentIdList = departmentList.stream().map(DepartmentEntity::getId).collect(Collectors.toList());
        }
        SqlBinder.Builder builder;
        if(isOwner && !privilegeManager.isSysAdmin(currentUser)){
            builder = new SqlBinder.Builder()
                    .appendSegment("select * from ldp_stat_project")
                    .appendWhere("1",1)
                    .appendWhereSegment("(private_flag = '0' or (id in (select relation_b from ldp_privilege where privilege_type in ('5','6') and relation_a = "+userId+")))")
                    .appendLike("name",search)
                    .appendInExceptNull("department_id",departmentIdList)
                    .appendSegment("order by create_time desc limit ?,?");
        }else{
            builder = new SqlBinder.Builder()
                    .appendSegment("select * from ldp_stat_project")
                    .appendWhere("1","1")
                    .appendLike("name",search)
                    .appendInExceptNull("department_id",departmentIdList)
                    .appendSegment("order by create_time desc limit ?,?");
        }
        SqlBinder binder = builder.create();
        List<ProjectEntity> entityList = DaoHelper.sql.getList(ProjectEntity.class,binder.toString(),pageEntity.getStartIndex() - 1, pageEntity.getPageSize());
        if(CollectionUtils.isEmpty(entityList)){
            return null;
        }
        List<ProjectViewEntity> viewEntities = new ArrayList<>();
        boolean isSystemAdmin = privilegeManager.isSysAdmin(currentUser);
        List<Integer> favoriteIdList = relationManager.actualQueryListByUserId(userId,RelationTypeEnum.FAVORITE_PROJECT);
        List<Integer> hasProjectAdminPrivilegeIds = privilegeManager.queryRelationIdsByUserId(userId,PrivilegeTypeEnum.STAT_PROJECT_ADMIN);
        List<Integer> hasProjectUserPrivilegeIds = privilegeManager.queryRelationIdsByUserId(userId,PrivilegeTypeEnum.STAT_PROJECT_USER);
        entityList.forEach(projectEntity -> {
            try{
                int projectId = projectEntity.getId();
                ProjectViewEntity projectViewEntity = new ProjectViewEntity(projectEntity);
                boolean isFavorite = CollectionUtils.isNotEmpty(favoriteIdList) && favoriteIdList.contains(projectId);
                projectViewEntity.setFavorite(isFavorite);
                List<Integer> privileges = new ArrayList<>();
                if(isSystemAdmin || (CollectionUtils.isNotEmpty(hasProjectAdminPrivilegeIds) && hasProjectAdminPrivilegeIds.contains(projectId))){
                    privileges.add(PrivilegeTypeEnum.STAT_PROJECT_ADMIN.getPrivilegeType());
                }
                if(isSystemAdmin || projectViewEntity.getPrivateFlag() == 0 || (CollectionUtils.isNotEmpty(hasProjectUserPrivilegeIds) && hasProjectUserPrivilegeIds.contains(projectId))){
                    privileges.add(PrivilegeTypeEnum.STAT_PROJECT_USER.getPrivilegeType());
                }
                if(CollectionUtils.isEmpty(privileges)){
                    String hash = Md5Util.getMD5(currentUser.getId() + "_" + OrderTypeEnum.PROJECT_ACCESS.getType() + "_" + projectEntity.getId());
                    boolean isApply = orderManager.isApply(hash);
                    projectViewEntity.setApply(isApply);
                }
                List<UserEntity> admins = privilegeManager.queryAdmins(projectEntity.getId(),PrivilegeTypeEnum.STAT_PROJECT_ADMIN.getPrivilegeType());
                projectViewEntity.setAdmins(admins);
                projectViewEntity.setPrivilegeIds(privileges);
                DepartmentViewEntity departmentEntity = departmentManager.queryViewInfoById(projectEntity.getDepartmentId());
                if(departmentEntity != null){
                    projectViewEntity.setDepartmentName(departmentEntity.getName());
                    projectViewEntity.setFullDepartmentName(departmentEntity.getFullPathName());
                }
                viewEntities.add(projectViewEntity);
            }catch (Exception ex){
                logger.error("combine project view entity error!",ex);
            }
        });
        return viewEntities;
    }


    @Override
    public ProjectEntity queryById(int projectId) throws Exception {
        return projectDao.queryById(projectId);
    }

    @Override
    public int save(ProjectEntity projectEntity, Set<Integer> adminList) throws Exception {
        DBConnection connection = ConnectionManager.getConnection();
        ConnectionManager.beginTransaction(connection);
        int id;
        try{
            id = projectDao.save(projectEntity);
            privilegeManager.updatePrivilege(PrivilegeTypeEnum.STAT_PROJECT_ADMIN,id,adminList);
            ConnectionManager.commitTransaction(connection);
        }catch (Exception ex){
            ConnectionManager.rollbackTransaction(connection);
            throw ex;
        }finally {
            ConnectionManager.close(connection);
        }
        return id;
    }

    @Override
    public void update(ProjectEntity projectEntity,Set<Integer> adminList) throws Exception {
        DBConnection connection = ConnectionManager.getConnection();
        ConnectionManager.beginTransaction(connection);
        try{
            projectDao.update(projectEntity);
            privilegeManager.updatePrivilege(PrivilegeTypeEnum.STAT_PROJECT_ADMIN,projectEntity.getId(),adminList);
            ConnectionManager.commitTransaction(connection);
        }catch (Exception ex){
            ConnectionManager.rollbackTransaction(connection);
            throw ex;
        }finally {
            ConnectionManager.close(connection);
        }
    }

    @Override
    public boolean isExist(String name) throws Exception {
        return DaoHelper.sql.count("select count(1) from ldp_stat_project where name = ?", name) == 1;
    }

    @Override
    public boolean isExist(int id) throws Exception {
        return DaoHelper.sql.count("select count(1) from ldp_stat_project where id = ?", id) == 1;
    }

    @Override
    public void delete(int id) throws Exception {
        DBConnection connection = ConnectionManager.getConnection();
        ConnectionManager.beginTransaction(connection);
        try{
            projectDao.delete(id);
            privilegeManager.deleteByRelationId(id, PrivilegeTypeEnum.STAT_PROJECT_USER.getPrivilegeType());
            privilegeManager.deleteByRelationId(id, PrivilegeTypeEnum.STAT_PROJECT_ADMIN.getPrivilegeType());
            relationManager.delete(id,RelationTypeEnum.FAVORITE_PROJECT);
            ConnectionManager.commitTransaction(connection);
        }catch (Exception ex){
            ConnectionManager.rollbackTransaction(connection);
            throw ex;
        }finally {
            ConnectionManager.close(connection);
        }
    }


    @Override
    public List<ZTreeViewNode> queryZTreeInfo(int projectId) throws Exception {
        ProjectEntity projectEntity = queryById(projectId);
        assert projectEntity != null;
        List<GroupExtEntity> groupList = groupManager.queryListByProjectId(projectId);
        List<ZTreeViewNode> nodeList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(groupList)){
            for(GroupExtEntity groupExtEntity : groupList) {
                ZTreeViewNode node = new ZTreeViewNode();
                node.setId(String.valueOf(groupExtEntity.getId()));
                node.setpId(String.valueOf(projectEntity.getId()));
                node.setName("i18n(ldp_i18n_project_manage_1013)" + groupExtEntity.getToken());
                node.setType(2);
                node.setIcon("/static/extend/png/plugin.png");
                nodeList.add(node);
            }
        }
        ZTreeViewNode node = new ZTreeViewNode();
        node.setId(String.valueOf(projectEntity.getId()));
        node.setpId(SysConst.TREE_ROOT_NODE_NAME);
        node.setType(1);
        node.setName("i18n(ldp_i18n_project_manage_1012)" + projectEntity.getName());
        node.setIcon("/static/extend/png/root.png");
        node.setOpen(true);
        nodeList.add(node);
        return nodeList;
    }
}
