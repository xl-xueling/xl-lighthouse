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
import com.dtstep.lighthouse.common.constant.StatConst;
import com.dtstep.lighthouse.common.entity.ListData;
import com.dtstep.lighthouse.common.entity.ServiceResult;
import com.dtstep.lighthouse.common.entity.group.GroupExtEntity;
import com.dtstep.lighthouse.common.entity.stat.TemplateEntity;
import com.dtstep.lighthouse.common.enums.GroupStateEnum;
import com.dtstep.lighthouse.common.enums.SwitchStateEnum;
import com.dtstep.lighthouse.common.random.RandomID;
import com.dtstep.lighthouse.common.modal.Column;
import com.dtstep.lighthouse.common.modal.Stat;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.common.util.StringUtil;
import com.dtstep.lighthouse.core.builtin.BuiltinLoader;
import com.dtstep.lighthouse.core.formula.FormulaTranslate;
import com.dtstep.lighthouse.core.rowkey.KeyGenerator;
import com.dtstep.lighthouse.core.rowkey.impl.DefaultKeyGenerator;
import com.dtstep.lighthouse.core.storage.dimens.DimensStorageSelector;
import com.dtstep.lighthouse.core.storage.warehouse.WarehouseStorageEngineProxy;
import com.dtstep.lighthouse.core.template.TemplateContext;
import com.dtstep.lighthouse.core.template.TemplateParser;
import com.dtstep.lighthouse.core.wrapper.DimensDBWrapper;
import com.dtstep.lighthouse.core.wrapper.GroupDBWrapper;
import com.dtstep.lighthouse.core.wrapper.StatDBWrapper;
import com.dtstep.lighthouse.insights.dao.GroupDao;
import com.dtstep.lighthouse.insights.dao.ProjectDao;
import com.dtstep.lighthouse.insights.dto.DimensValueDeleteParam;
import com.dtstep.lighthouse.insights.dto.GroupQueryParam;
import com.dtstep.lighthouse.common.enums.ResourceTypeEnum;
import com.dtstep.lighthouse.common.modal.Group;
import com.dtstep.lighthouse.common.modal.ResourceDto;
import com.dtstep.lighthouse.insights.service.GroupService;
import com.dtstep.lighthouse.insights.service.ResourceService;
import com.dtstep.lighthouse.insights.service.StatService;
import com.dtstep.lighthouse.insights.vo.GroupVO;
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

@Service
public class GroupServiceImpl implements GroupService {

    private static final Logger logger = LoggerFactory.getLogger(GroupServiceImpl.class);

    @Autowired
    private GroupDao groupDao;

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private StatService statService;

    @Autowired
    private ResourceService resourceService;

    private static final KeyGenerator keyGenerator = new DefaultKeyGenerator();

    @Override
    public ListData<GroupVO> queryList(GroupQueryParam groupQueryParam, Integer pageNum, Integer pageSize) throws Exception {
        PageHelper.startPage(pageNum,pageSize);
        List<GroupVO> dtoList = new ArrayList<>();
        PageInfo<Group> pageInfo;
        try{
            List<Group> list = groupDao.queryList(groupQueryParam);
            pageInfo = new PageInfo<>(list);
        }finally {
            PageHelper.clearPage();
        }
        for(Group group : pageInfo.getList()){
            try{
                GroupExtEntity groupExtEntity = GroupDBWrapper.combineExtInfo(group);
                GroupVO groupVO = new GroupVO(groupExtEntity);
                dtoList.add(groupVO);
            }catch (Exception ex){
                logger.error("translate item info error,itemId:{}!",group.getId(),ex);
            }
        }
        return ListData.newInstance(dtoList,pageInfo.getTotal(),pageNum,pageSize);
    }

    @Transactional
    @Override
    public int create(Group group) {
        LocalDateTime localDateTime = LocalDateTime.now();
        group.setSecretKey(RandomID.id(40));
        group.setCreateTime(localDateTime);
        group.setUpdateTime(localDateTime);
        group.setRefreshTime(localDateTime);
        group.setRandomId(RandomID.id(32));
        group.setState(GroupStateEnum.RUNNING);
        group.setDebugMode(SwitchStateEnum.CLOSE);
        groupDao.insert(group);
        resourceService.addResourceCallback(ResourceDto.newResource(ResourceTypeEnum.Group,group.getId(),ResourceTypeEnum.Project,group.getProjectId()));
        return group.getId();
    }

    @Transactional
    @Override
    public int update(Group group) {
        LocalDateTime localDateTime = LocalDateTime.now();
        group.setUpdateTime(localDateTime);
        int result = groupDao.update(group);
        resourceService.updateResourcePidCallback(ResourceDto.newResource(ResourceTypeEnum.Group,group.getId(),ResourceTypeEnum.Project,group.getProjectId()));
        return result;
    }

    @Transactional
    @Override
    public int delete(Group group) {
        Validate.notNull(group);
        Integer id = group.getId();
        resourceService.deleteResourceCallback(ResourceDto.newResource(ResourceTypeEnum.Group,group.getId(),ResourceTypeEnum.Project,group.getProjectId()));
        return groupDao.deleteById(id);
    }

    @Override
    public GroupVO queryById(Integer id) throws Exception {
        GroupExtEntity groupExtEntity;
        if(BuiltinLoader.isBuiltinGroup(id)){
            groupExtEntity = BuiltinLoader.getBuiltinGroup(id);
        }else{
            Group group = groupDao.queryById(id);
            if(group == null){
                return null;
            }
            groupExtEntity = GroupDBWrapper.combineExtInfo(group);
        }
        GroupVO groupVO = new GroupVO(groupExtEntity);
        Set<String> relatedColumns = getRelatedColumns(groupExtEntity);
        groupVO.setRelatedColumns(relatedColumns);
        return groupVO;
    }

    public Set<String> getRelatedColumns(Group group) throws Exception {
        Set<String> relatedColumnSet = new HashSet<>();
        List<Column> columnList = group.getColumns();
        List<Stat> statList = StatDBWrapper.queryStatByGroupIDFromDB(group.getId());
        if (CollectionUtils.isNotEmpty(statList)) {
            for (Stat stat : statList) {
                String template = stat.getTemplate();
                ServiceResult<TemplateEntity> serviceResult = TemplateParser.parseConfig(new TemplateContext(stat.getId(),template,stat.getTimeparam(),columnList));
                if(!serviceResult.isSuccess()){
                    logger.error("load stat error,id:{},template:{}.", stat.getId(),template);
                    continue;
                }
                List<Column> statRelatedColumns = FormulaTranslate.queryRelatedColumns(columnList, template);
                if (CollectionUtils.isNotEmpty(statRelatedColumns)) {
                    for (Column column : statRelatedColumns) {
                        relatedColumnSet.add(column.getName());
                    }
                }
                String dimens = serviceResult.getData().getDimens();
                if(!StringUtil.isEmpty(dimens)){
                    List<Column> dimensRelatedColumns = FormulaTranslate.queryRelatedColumns(columnList,dimens);
                    if (CollectionUtils.isNotEmpty(dimensRelatedColumns)) {
                        for (Column column : dimensRelatedColumns) {
                            relatedColumnSet.add(column.getName());
                        }
                    }
                }
            }
        }
        return relatedColumnSet;
    }

    @Override
    @Cacheable(value = "NormalPeriod",key = "#targetClass + '_' + 'queryById' + '_' + #id",cacheManager = "caffeineCacheManager",unless = "#result == null")
    public Group cacheQueryById(Integer id) {
        return groupDao.queryById(id);
    }

    @Override
    public List<Group> queryByProjectId(Integer projectId) {
        return groupDao.queryByProjectId(projectId);
    }

    @Override
    public int count(GroupQueryParam queryParam) {
        return groupDao.count(queryParam);
    }

    @Override
    public String getSecretKey(Integer id) {
        return groupDao.getSecretKey(id);
    }

    @Override
    public List<String> queryDimensList(Integer id) throws Exception{
        GroupExtEntity groupEntity = queryById(id);
        if(groupEntity == null){
            return null;
        }
        List<Stat> statList = statService.queryByGroupId(id);
        List<String> dimensList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(statList)){
            List<Column> columnList = groupEntity.getColumns();
            for(Stat stat : statList){
                String template = stat.getTemplate();
                ServiceResult<TemplateEntity> serviceResult = TemplateParser.parseConfig(new TemplateContext(stat.getId(),template,stat.getTimeparam(),columnList));
                if(!serviceResult.isSuccess()){
                    logger.error("load stat error,id:{},template:{}.", stat.getId(),template);
                    continue;
                }
                String dimens = serviceResult.getData().getDimens();
                if(!StringUtil.isEmpty(dimens)){
                    List<Column> dimensRelatedColumns = FormulaTranslate.queryRelatedColumns(columnList,dimens);
                    if(CollectionUtils.isNotEmpty(dimensRelatedColumns)){
                        for(Column relatedColumn : dimensRelatedColumns){
                            String columnName = relatedColumn.getName();
                            if(!dimensList.contains(columnName)){
                                dimensList.add(columnName);
                            }
                        }
                    }
                }
            }
        }
        return dimensList;
    }

    @Override
    public List<String> queryDimensValueList(Integer groupId, String dimens) throws Exception {
        GroupExtEntity groupEntity = queryById(groupId);
        if(groupEntity == null){
            return null;
        }
        return DimensStorageSelector.query(groupEntity,dimens, null, StatConst.DIMENS_THRESHOLD_LIMIT_COUNT);
    }

    @Override
    public void deleteDimensValue(List<DimensValueDeleteParam> deleteParams) throws Exception {
        List<String> keyList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(deleteParams)){
            for(DimensValueDeleteParam deleteParam : deleteParams){
                int groupId = deleteParam.getGroupId();
                Group group = GroupDBWrapper.queryById(groupId);
                if(group == null){
                    continue;
                }
                String dimens = deleteParam.getDimens();
                String dimensValue = deleteParam.getDimensValue();
                String rowKey = keyGenerator.dimensKey(group,dimens,dimensValue);
                keyList.add(rowKey+";v");
            }
        }
        WarehouseStorageEngineProxy.getInstance().deletes(StatConst.DIMENS_STORAGE_TABLE,keyList);
    }

    @Override
    public void clearDimensValue(Integer groupId) throws Exception {
        Group dbGroup = groupDao.queryById(groupId);
        if(dbGroup == null){
            return;
        }
        dbGroup.setDataVersion(dbGroup.getDataVersion() + 1);
        update(dbGroup);
    }
}
