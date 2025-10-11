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
import com.dtstep.lighthouse.common.entity.ListData;
import com.dtstep.lighthouse.common.enums.PrivateTypeEnum;
import com.dtstep.lighthouse.common.enums.RoleTypeEnum;
import com.dtstep.lighthouse.common.enums.RollbackStateEnum;
import com.dtstep.lighthouse.common.enums.RollbackTypeEnum;
import com.dtstep.lighthouse.common.modal.*;
import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.core.builtin.BuiltinLoader;
import com.dtstep.lighthouse.core.wrapper.UserDBWrapper;
import com.dtstep.lighthouse.insights.dao.RollbackDao;
import com.dtstep.lighthouse.insights.dto.RollbackQueryParam;
import com.dtstep.lighthouse.insights.service.BaseService;
import com.dtstep.lighthouse.insights.service.RollbackService;
import com.dtstep.lighthouse.insights.vo.ProjectVO;
import com.dtstep.lighthouse.insights.vo.RollbackVO;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RollbackServiceImpl implements RollbackService {

    private static final Logger logger = LoggerFactory.getLogger(RollbackServiceImpl.class);

    @Autowired
    private RollbackDao rollbackDao;

    @Autowired
    private BaseService baseService;

    @Transactional
    @Override
    public ObjectNode put(RollbackModal rollbackModal) throws Exception {
        Integer resourceId = rollbackModal.getResourceId();
        RollbackTypeEnum rollbackTypeEnum = rollbackModal.getDataType();
        int version;
        if(rollbackModal.getVersion() == null){
            Integer dbVersion = rollbackDao.getLatestVersion(resourceId,rollbackTypeEnum);
            version = 1;
            if(dbVersion != null){
                version = dbVersion + 1;
            }
            rollbackModal.setVersion(version);
        }else{
            version = rollbackModal.getVersion();
        }
        LocalDateTime localDateTime = LocalDateTime.now();
        int userId = baseService.getCurrentUserId();
        rollbackModal.setCreateTime(localDateTime);
        rollbackModal.setState(RollbackStateEnum.UNPUBLISHED);
        rollbackModal.setUserId(userId);
        rollbackDao.insert(rollbackModal);
        ObjectNode objectNode = JsonUtil.createObjectNode();
        objectNode.put("version",version);
        objectNode.put("state",RollbackStateEnum.UNPUBLISHED.getState());
        objectNode.put("dataType",rollbackTypeEnum.getType());
        objectNode.put("createTime", DateUtil.translateToTimeStamp(localDateTime));
        return objectNode;
    }

    @Override
    public RollbackModal queryByVersion(RollbackQueryParam queryParam) throws Exception {
        return rollbackDao.queryByVersion(queryParam);
    }

    private RollbackVO translate(RollbackModal rollbackModal){
        RollbackVO rollbackVO = new RollbackVO(rollbackModal);
        if(rollbackModal.getUserId() != null){
            User user = UserDBWrapper.queryById(rollbackModal.getUserId());
            rollbackVO.setCreateUser(user);
        }
        return rollbackVO;
    }

    @Override
    public ListData<RollbackVO> queryList(RollbackQueryParam queryParam, Integer pageNum, Integer pageSize) throws Exception {
        PageHelper.startPage(pageNum,pageSize);
        PageInfo<RollbackModal> pageInfo;
        try{
            List<RollbackModal> rollbackList = rollbackDao.queryList(queryParam);
            pageInfo = new PageInfo<>(rollbackList);
        }finally {
            PageHelper.clearPage();
        }
        List<RollbackVO> dtoList = new ArrayList<>();
        for(RollbackModal rollbackModal : pageInfo.getList()){
            RollbackVO rollbackVO;
            try{
                rollbackVO = translate(rollbackModal);
                dtoList.add(rollbackVO);
            }catch (Exception ex){
                logger.error("translate item info error,itemId:{}!",rollbackModal.getId(),ex);
            }
        }
        return ListData.newInstance(dtoList,pageInfo.getTotal(),pageNum,pageSize);
    }

    @Override
    public void deleteByQueryParam(RollbackQueryParam queryParam) throws Exception {
        rollbackDao.deleteByQueryParam(queryParam);
    }
}
