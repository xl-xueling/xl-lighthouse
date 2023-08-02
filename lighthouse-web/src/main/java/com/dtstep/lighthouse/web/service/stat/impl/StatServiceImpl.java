package com.dtstep.lighthouse.web.service.stat.impl;
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
import com.dtstep.lighthouse.common.entity.group.GroupEntity;
import com.dtstep.lighthouse.common.util.StringUtil;
import com.dtstep.lighthouse.core.wrapper.GroupDBWrapper;
import com.dtstep.lighthouse.web.manager.department.DepartmentManager;
import com.dtstep.lighthouse.web.manager.group.GroupManager;
import com.dtstep.lighthouse.web.manager.order.OrderManager;
import com.dtstep.lighthouse.web.manager.privilege.PrivilegeManager;
import com.dtstep.lighthouse.web.manager.project.ProjectManager;
import com.dtstep.lighthouse.web.manager.relations.RelationManager;
import com.google.common.collect.Sets;
import com.dtstep.lighthouse.common.constant.SysConst;
import com.dtstep.lighthouse.common.entity.department.DepartmentEntity;
import com.dtstep.lighthouse.common.entity.department.DepartmentViewEntity;
import com.dtstep.lighthouse.common.entity.paging.PageEntity;
import com.dtstep.lighthouse.common.entity.project.ProjectEntity;
import com.dtstep.lighthouse.common.entity.list.ListViewDataObject;
import com.dtstep.lighthouse.common.entity.stat.StatEntity;
import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.entity.stat.StatViewEntity;
import com.dtstep.lighthouse.common.entity.user.UserEntity;
import com.dtstep.lighthouse.common.enums.display.DisplayTypeEnum;
import com.dtstep.lighthouse.common.enums.relations.RelationTypeEnum;
import com.dtstep.lighthouse.common.enums.order.OrderTypeEnum;
import com.dtstep.lighthouse.common.enums.role.PrivilegeTypeEnum;
import com.dtstep.lighthouse.common.enums.stat.StatStateEnum;
import com.dtstep.lighthouse.common.util.Md5Util;
import com.dtstep.lighthouse.core.dao.ConnectionManager;
import com.dtstep.lighthouse.core.dao.DBConnection;
import com.dtstep.lighthouse.core.dao.DaoHelper;
import com.dtstep.lighthouse.core.sql.SqlBinder;
import com.dtstep.lighthouse.web.dao.StatDao;
import com.dtstep.lighthouse.web.manager.stat.StatManager;
import com.dtstep.lighthouse.web.param.ParamWrapper;
import com.dtstep.lighthouse.web.service.stat.StatService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.SetUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class StatServiceImpl implements StatService {

    private static final Logger logger = LoggerFactory.getLogger(StatServiceImpl.class);

    @Autowired
    private PrivilegeManager privilegeManager;

    @Autowired
    private DepartmentManager departmentManager;

    @Autowired
    private ProjectManager projectManager;

    @Autowired
    private RelationManager relationManager;

    @Autowired
    private OrderManager orderManager;

    @Autowired
    private StatDao statDao;

    @Autowired
    private StatManager statManager;

    @Autowired
    private GroupManager groupManager;

    @Override
    public ListViewDataObject queryListByPage(UserEntity currentUser, int page, int departmentId, int projectId, String search) throws Exception {
        int totalSize = countByParam(departmentId,projectId,search);
        HashMap<String,Object> urlMap = new HashMap<>();
        urlMap.put("departmentId",departmentId);
        urlMap.put("projectId",projectId);
        urlMap.put("search",search);
        ListViewDataObject listObject = new ListViewDataObject();
        String baseUrl = ParamWrapper.getBaseLink("/stat/list.shtml",urlMap);
        PageEntity pageEntity = new PageEntity(baseUrl, page,totalSize, SysConst._LIST_PAGE_SIZE);
        listObject.setPageEntity(pageEntity);
        List<StatViewEntity> viewEntityList = queryListByPage(currentUser,departmentId,projectId,search,pageEntity);
        listObject.setDataList(viewEntityList);
        return listObject;
    }

    protected List<StatViewEntity> queryListByPage(UserEntity currentUser, int departmentId, int projectId, String search, PageEntity pageEntity) throws Exception {
        List<Integer> departmentIdList = null;
        if(departmentId != -1){
            List<DepartmentViewEntity> departmentList = departmentManager.queryListByPid(departmentId);
            departmentIdList = departmentList.stream().map(DepartmentEntity::getId).collect(Collectors.toList());
        }
        SqlBinder dataBinder = new SqlBinder.Builder()
                .appendSegment(" select * from (SELECT a.id,a.title,a.group_id,a.project_id,a.time_param,a.state,a.create_time,c.id as department_id FROM (ldp_stat_item a left join ldp_stat_project b on a.project_id = b.id)\n" +
                        " left join ldp_department c on b.department_id = c.id where b.id != 'NULL' and c.id != 'NULL') d")
                .appendInExceptNull("state", Sets.newHashSet(StatStateEnum.RUNNING.getState(),StatStateEnum.LIMITING.getState()))
                .appendInExceptNull("department_id",departmentIdList)
                .appendWhere("project_id",projectId)
                .appendSegmentIf(StringUtil.isNotEmpty(search)," and (title like '%"+search+"%' or group_id = '"+search+"' or project_id = '"+search+"' or id = '"+search+"')")
                .appendSegment("order by a.create_time desc limit ?,?")
                .create();
        List<StatEntity> list = DaoHelper.sql.getList(StatEntity.class,dataBinder.toString(),pageEntity.getStartIndex() - 1, pageEntity.getPageSize());
        if(CollectionUtils.isEmpty(list)){
            return null;
        }
        List<StatViewEntity> viewList = new ArrayList<>();
        boolean isSystemAdmin = privilegeManager.isSysAdmin(currentUser);
        List<Integer> favoriteIdList = relationManager.actualQueryListByUserId(currentUser.getId(), RelationTypeEnum.FAVORITE_ITEM);
        List<Integer> hasProjectAdminIdList = privilegeManager.queryRelationIdsByUserId(currentUser.getId(), PrivilegeTypeEnum.STAT_PROJECT_ADMIN);
        List<Integer> hasProjectUserPrivilegeIds = privilegeManager.queryRelationIdsByUserId(currentUser.getId(),PrivilegeTypeEnum.STAT_PROJECT_USER);
        List<Integer> hasStatUserPrivilegeIds = privilegeManager.queryRelationIdsByUserId(currentUser.getId(), PrivilegeTypeEnum.STAT_ITEM_USER);
        for(StatEntity statEntity : list) {
            try{
                StatViewEntity statViewEntity = new StatViewEntity(statEntity);
                GroupEntity groupEntity = GroupDBWrapper.queryById(statEntity.getGroupId());
                statViewEntity.setToken(groupEntity.getToken());
                List<UserEntity> admins = privilegeManager.queryAdmins(statViewEntity.getProjectId(),PrivilegeTypeEnum.STAT_PROJECT_ADMIN.getPrivilegeType());
                Validate.isTrue(CollectionUtils.isNotEmpty(admins));
                statViewEntity.setAdmins(admins);
                ProjectEntity projectEntity = projectManager.queryById(statEntity.getProjectId());
                Validate.notNull(projectEntity);
                statViewEntity.setProjectName(projectEntity.getName());
                statViewEntity.setPrivateFlag(projectEntity.getPrivateFlag());
                List<Integer> privileges = new ArrayList<>();
                boolean hasUserRole = isSystemAdmin
                        || projectEntity.getPrivateFlag() == 0
                        || (CollectionUtils.isNotEmpty(hasProjectAdminIdList) && hasProjectAdminIdList.contains(statEntity.getProjectId()))
                        || (CollectionUtils.isNotEmpty(hasProjectUserPrivilegeIds) && hasProjectUserPrivilegeIds.contains(statEntity.getProjectId()))
                        || (CollectionUtils.isNotEmpty(hasStatUserPrivilegeIds) && hasStatUserPrivilegeIds.contains(statEntity.getId()));
                if(hasUserRole){
                    privileges.add(PrivilegeTypeEnum.STAT_ITEM_USER.getPrivilegeType());
                }else{
                    String hash = Md5Util.getMD5(currentUser.getId() + "_" + OrderTypeEnum.STAT_ACCESS.getType() + "_" + statEntity.getId());
                    boolean isApply = orderManager.isApply(hash);
                    statViewEntity.setApply(isApply);
                }
                DepartmentViewEntity departmentEntity = departmentManager.queryViewInfoById(projectEntity.getDepartmentId());
                if(departmentEntity != null){
                    statViewEntity.setDepartmentName(departmentEntity.getName());
                    statViewEntity.setFullDepartmentName(departmentEntity.getFullPathName());
                }
                boolean isFavorite = CollectionUtils.isNotEmpty(favoriteIdList) && favoriteIdList.contains(statEntity.getId());
                statViewEntity.setFavorite(isFavorite);
                statViewEntity.setPrivilegeIds(privileges);
                viewList.add(statViewEntity);
            }catch (Exception ex){
                logger.error("combine stat view info error!",ex);
            }
        }
        return viewList;
    }

    protected int countByParam(int departmentId, int projectId, String search) throws Exception {
        List<Integer> departmentIdList = null;
        if(departmentId != -1){
            List<DepartmentViewEntity> departmentList = departmentManager.queryListByPid(departmentId);
            departmentIdList = departmentList.stream().map(DepartmentEntity::getId).collect(Collectors.toList());
        }
        SqlBinder sqlBinder = new SqlBinder.Builder()
                .appendSegment(" select count(1) from (SELECT a.*,c.id as department_id FROM (ldp_stat_item a left join ldp_stat_project b on a.project_id = b.id)\n" +
                        " left join ldp_department c on b.department_id = c.id where b.id != 'NULL' and c.id != 'NULL') d")
                .appendInExceptNull("state", Sets.newHashSet(StatStateEnum.RUNNING.getState(),StatStateEnum.LIMITING.getState()))
                .appendInExceptNull("department_id",departmentIdList)
                .appendWhere("project_id",projectId)
                .appendSegmentIf(StringUtil.isNotEmpty(search)," and (title like '%"+search+"%' or group_id = '"+search+"' or project_id = '"+search+"' or id = '"+search+"')")
                .create();
        return DaoHelper.sql.count(sqlBinder.toString());
    }

    @Override
    public List<StatExtEntity> queryListByGroupId(int groupId) throws Exception{
        return statDao.queryListByGroupId(groupId);
    }

    @Override
    public StatExtEntity queryById(int statId){
        return statDao.queryById(statId);
    }

    @Override
    public void changeState(int statId, StatStateEnum stateEnum) throws Exception {
        DBConnection dbConnection = ConnectionManager.getConnection();
        ConnectionManager.beginTransaction(dbConnection);
        try{
            StatExtEntity statExtEntity = statManager.queryById(statId);
            statManager.changeState(statExtEntity,stateEnum);
            ConnectionManager.commitTransaction(dbConnection);
            logger.info("change stat state success,statId:{}!",statId);
        }catch (Exception ex){
            logger.error("change stat state error,statId:{}!",statId,ex);
            ConnectionManager.rollbackTransaction(dbConnection);
            throw ex;
        }finally {
            ConnectionManager.close(dbConnection);
        }
    }

    @Override
    public boolean isExist(int statId) throws Exception{
        return statManager.isExist(statId);
    }

    @Override
    public void deleteById(int statId) throws Exception {
        DBConnection connection = ConnectionManager.getConnection();
        ConnectionManager.beginTransaction(connection);
        try{
            StatExtEntity statExtEntity = statManager.queryById(statId);
            statManager.delete(statExtEntity);
            privilegeManager.deleteByRelationId(statId, PrivilegeTypeEnum.STAT_ITEM_USER.getPrivilegeType());
            relationManager.delete(statId,RelationTypeEnum.FAVORITE_ITEM);
            ConnectionManager.commitTransaction(connection);
        }catch (Exception ex){
            ConnectionManager.rollbackTransaction(connection);
            throw ex;
        }finally {
            ConnectionManager.close(connection);
        }
    }

    @Override
    public void changeDisplayType(int statId, DisplayTypeEnum displayTypeEnum) throws Exception {
        statDao.changeDisplayType(statId,displayTypeEnum);
    }

    @Override
    public StatExtEntity queryMatchStat(UserEntity userEntity, StatExtEntity statExtEntity, Set<String> filterDimensSet) throws Exception {
        String[] dimensArr = statExtEntity.getTemplateEntity().getDimensArr();
        if(dimensArr != null){
            Set<String> curDimensSet = new HashSet<>(Arrays.asList(dimensArr));
            if(SetUtils.isEqualSet(curDimensSet,filterDimensSet)){
                return statExtEntity;
            }
        }
        int groupId = statExtEntity.getGroupId();
        List<StatExtEntity> statExtEntityList = queryListByGroupId(groupId);
        if(statExtEntityList == null){
            return null;
        }
        for(StatExtEntity compareEntity: statExtEntityList){
            String statA = compareEntity.getTemplateEntity().getStat();
            String statB = statExtEntity.getTemplateEntity().getStat();
            if(!statA.equals(statB)){
                continue;
            }
            String timeParamA = compareEntity.getTimeParam();
            String timeParamB = statExtEntity.getTimeParam();
            if(!timeParamA.equals(timeParamB)){
                continue;
            }
            String[] compareDimensArr = compareEntity.getTemplateEntity().getDimensArr();
            if(compareDimensArr == null){
                continue;
            }
            Set<String> compareDimensSet = new HashSet<>(Arrays.asList(compareDimensArr));
            if(!SetUtils.isEqualSet(compareDimensSet,filterDimensSet)){
                continue;
            }
            boolean hasRole;
            try {
                hasRole = privilegeManager.hasRole(userEntity,compareEntity.getId(), PrivilegeTypeEnum.STAT_ITEM_USER.getPrivilegeType());
                if(!hasRole){
                    continue;
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
            return compareEntity;
        }
        return null;
    }

    @Override
    public void update(StatExtEntity statExtEntity) throws Exception {
        statManager.update(statExtEntity);
    }
}
