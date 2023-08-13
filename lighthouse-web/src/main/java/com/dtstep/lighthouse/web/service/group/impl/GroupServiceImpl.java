package com.dtstep.lighthouse.web.service.group.impl;
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
import com.dtstep.lighthouse.common.entity.group.GroupExtEntity;
import com.dtstep.lighthouse.common.entity.stat.StatEntity;
import com.dtstep.lighthouse.core.dao.ConnectionManager;
import com.dtstep.lighthouse.core.dao.DBConnection;
import com.dtstep.lighthouse.core.dao.DaoHelper;
import com.dtstep.lighthouse.core.wrapper.GroupDBWrapper;
import com.dtstep.lighthouse.web.dao.GroupDao;
import com.dtstep.lighthouse.web.manager.stat.StatManager;
import com.dtstep.lighthouse.web.service.group.GroupService;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;


@Service
public class GroupServiceImpl implements GroupService {

    private static final Logger logger = LoggerFactory.getLogger(GroupServiceImpl.class);

    @Autowired
    private StatManager statManager;

    @Autowired
    private GroupDao groupDao;

    @Override
    public int save(int userId, GroupEntity groupEntity, List<StatEntity> statList) throws Exception {
        DBConnection dbConnection = ConnectionManager.getConnection();
        ConnectionManager.beginTransaction(dbConnection);
        int groupId = -1;
        try{
            groupId = groupDao.save(groupEntity);
            for(StatEntity statEntity : statList){
                statEntity.setGroupId(groupId);
            }
            statManager.updateList(userId,groupId,statList);
            ConnectionManager.commitTransaction(dbConnection);
            logger.info("save group,save group[id:{}] success!",groupId);
        }catch (Exception ex){
            logger.error("save group,save group[id:{}] error!",groupId,ex);
            ConnectionManager.rollbackTransaction(dbConnection);
            throw ex;
        }finally {
            ConnectionManager.close(dbConnection);
        }
        return groupId;
    }

    @Override
    public void update(int userId, GroupEntity groupEntity, List<StatEntity> statList) throws Exception{
        DBConnection dbConnection = ConnectionManager.getConnection();
        ConnectionManager.beginTransaction(dbConnection);
        int groupId = groupEntity.getId();
        try{
            statManager.updateList(userId, groupEntity.getId(), statList);
            groupDao.update(groupEntity);
            ConnectionManager.commitTransaction(dbConnection);
            logger.info("update group,update group info success,groupId:{}",groupId);
        }catch (Exception ex){
            logger.error("update group,update group info error,groupId:{}",groupId,ex);
            ConnectionManager.rollbackTransaction(dbConnection);
            throw ex;
        }finally {
            ConnectionManager.close(dbConnection);
        }
    }

    @Override
    public boolean isExist(String token) throws Exception {
        return DaoHelper.sql.count("select count(1) from ldp_stat_group where token = ?", token) >= 1;
    }

    @Override
    public GroupExtEntity queryById(int groupId) throws Exception {
        return groupDao.queryById(groupId);
    }

    @Override
    public void delete(int groupId) throws Exception {
        GroupExtEntity groupExtEntity = GroupDBWrapper.queryById(groupId);
        Validate.notNull(groupExtEntity);
        groupDao.delete(groupExtEntity);
    }
}
