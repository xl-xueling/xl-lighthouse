package com.dtstep.lighthouse.core.ipc.impl;
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
import com.dtstep.lighthouse.common.constant.StatConst;
import com.dtstep.lighthouse.common.entity.group.GroupExtEntity;
import com.dtstep.lighthouse.common.entity.group.GroupVerifyEntity;
import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.entity.stat.StatVerifyEntity;
import com.dtstep.lighthouse.common.entity.view.LimitValue;
import com.dtstep.lighthouse.common.entity.view.StatValue;
import com.dtstep.lighthouse.common.enums.OwnerTypeEnum;
import com.dtstep.lighthouse.common.enums.ResourceTypeEnum;
import com.dtstep.lighthouse.common.exception.PermissionException;
import com.dtstep.lighthouse.common.modal.Caller;
import com.dtstep.lighthouse.common.modal.Permission;
import com.dtstep.lighthouse.common.modal.Role;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.common.util.SnappyUtil;
import com.dtstep.lighthouse.common.util.StringUtil;
import com.dtstep.lighthouse.core.batch.BatchAdapter;
import com.dtstep.lighthouse.core.ipc.DisruptorEventProducer;
import com.dtstep.lighthouse.core.storage.limit.LimitStorageSelector;
import com.dtstep.lighthouse.core.storage.result.ResultStorageSelector;
import com.dtstep.lighthouse.core.wrapper.*;
import com.dtstep.lighthouse.core.ipc.RPCServer;
import com.google.common.base.Splitter;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class RPCServerImpl implements RPCServer {

    private static final Logger logger = LoggerFactory.getLogger(RPCServerImpl.class);

    private static final DisruptorEventProducer eventProducer = new DisruptorEventProducer();

    @Override
    public void authVerification(String callerName, String callerKey, int resourceId, ResourceTypeEnum resourceTypeEnum) throws Exception {
        if(StringUtil.isEmpty(callerName)){
            throw new IllegalArgumentException("Missing required parameters[callerName]!");
        }
        if(StringUtil.isEmpty(callerKey)){
            throw new IllegalArgumentException("Missing required parameters[callerKey]!");
        }
        Caller caller = CallerDBWrapper.queryByName(callerName);
        if(caller == null){
            throw new PermissionException("Api caller[" + callerName + "] not exist!");
        }
        if(!callerKey.equals(caller.getSecretKey())){
            throw new PermissionException("Api caller[" + callerName + "] secret-key verification failed!");
        }
        Role role = RoleDBWrapper.queryAccessRoleByResource(resourceId,resourceTypeEnum);
        if(role == null){
            throw new PermissionException("Api caller[" + callerName + "] authorization verification failed!");
        }
        boolean hasPermission = PermissionDBWrapper.hasPermission(caller.getId(), OwnerTypeEnum.CALLER,role.getId());
        if(!hasPermission){
            throw new PermissionException("Api caller[" + callerName + "] authorization verification failed!");
        }
    }

    @Override
    public GroupVerifyEntity queryGroupInfo(String token) throws Exception {
        GroupExtEntity groupExtEntity = GroupDBWrapper.queryByToken(token);
        GroupVerifyEntity groupVerifyEntity = null;
        if(groupExtEntity != null){
            groupVerifyEntity = new GroupVerifyEntity();
            groupVerifyEntity.setVerifyKey(groupExtEntity.getVerifyKey());
            groupVerifyEntity.setRelationColumns(groupExtEntity.getRunningRelatedColumns());
            groupVerifyEntity.setState(groupExtEntity.getState());
            groupVerifyEntity.setGroupId(groupExtEntity.getId());
            groupVerifyEntity.setToken(groupExtEntity.getToken());
            groupVerifyEntity.setMinTimeParam(groupExtEntity.getMinTimeParam());
            if(logger.isTraceEnabled()){
                logger.trace("query groupInfo by token,token:{},groupInfo:{}",token, JsonUtil.toJSONString(groupVerifyEntity));
            }
        }
        return groupVerifyEntity;
    }

    @Override
    public StatVerifyEntity queryStatInfo(int id) throws Exception {
        StatExtEntity statExtEntity = StatDBWrapper.queryById(id);
        StatVerifyEntity statVerifyEntity = null;
        if(statExtEntity != null){
            statVerifyEntity = new StatVerifyEntity();
            String token = statExtEntity.getToken();
            GroupVerifyEntity groupVerifyEntity = queryGroupInfo(token);
            statVerifyEntity.setStatId(id);
            statVerifyEntity.setVerifyKey(groupVerifyEntity.getVerifyKey());
        }
        return statVerifyEntity;
    }

    @Override
    public void process(byte[] bytes) throws Exception {
        if(bytes == null){
            return;
        }
        String data;
        if(SnappyUtil.isCompress(bytes)){
            data = SnappyUtil.uncompressByte(bytes);
        }else{
            data = new String(bytes, StandardCharsets.UTF_8);
        }
        if(StringUtil.isEmpty(data)){
            return;
        }
        if(logger.isDebugEnabled()){
            logger.debug("lighthouse debug,ice service receive message:{}",data);
        }
        for (String temp : Splitter.on(StatConst.SEPARATOR_LEVEL_0).split(data)) {
            if (!StringUtil.isEmpty(temp)) {
                int index = temp.lastIndexOf(StatConst.SEPARATOR_LEVEL_1);
                eventProducer.onData(temp.substring(0, index), Integer.parseInt(temp.substring(index + 1)));
            }
        }
    }

    @Override
    public List<StatValue> dataQuery(int statId, String dimensValue, List<Long> batchList) throws Exception{
        StatExtEntity statExtEntity = StatDBWrapper.queryById(statId);
        if(statExtEntity == null){
            throw new IllegalArgumentException("statistic:" + statId + " not exist!");
        }
        return ResultStorageSelector.query(statExtEntity,dimensValue,batchList);
    }

    @Override
    public List<StatValue> dataDurationQuery(int statId, String dimensValue, long startTime, long endTime) throws Exception{
        StatExtEntity statExtEntity = StatDBWrapper.queryById(statId);
        if(statExtEntity == null){
            throw new IllegalArgumentException("statistic:" + statId + " not exist!");
        }
        List<Long> batchList;
        try{
            batchList = BatchAdapter.queryBatchTimeList(statExtEntity.getTimeparam(),startTime,endTime);
        }catch (Exception ex){
            logger.error("query batch list error,statId:{}",statId,ex);
            throw ex;
        }
        return ResultStorageSelector.query(statExtEntity,dimensValue,batchList);
    }

    @Override
    public Map<String, List<StatValue>> dataQueryWithDimensList(int statId, List<String> dimensValueList, List<Long> batchList) throws Exception {
        StatExtEntity statExtEntity = StatDBWrapper.queryById(statId);
        if(statExtEntity == null){
            throw new IllegalArgumentException("statistic:" + statId + " not exist!");
        }
        return ResultStorageSelector.queryWithDimensList(statExtEntity,dimensValueList,batchList);
    }

    @Override
    public Map<String, List<StatValue>> dataDurationQueryWithDimensList(int statId, List<String> dimensValueList, long startTime, long endTime) throws Exception {
        StatExtEntity statExtEntity = StatDBWrapper.queryById(statId);
        if(statExtEntity == null){
            throw new IllegalArgumentException("statistic:" + statId + " not exist!");
        }
        List<Long> batchList;
        try{
            batchList = BatchAdapter.queryBatchTimeList(statExtEntity.getTimeparam(),startTime,endTime);
        }catch (Exception ex){
            logger.error("query batch list error,statId:{}",statId,ex);
            throw ex;
        }
        return ResultStorageSelector.queryWithDimensList(statExtEntity,dimensValueList,batchList);
    }

    @Override
    public List<LimitValue> limitQuery(int statId, long batchTime) throws Exception {
        StatExtEntity statExtEntity = StatDBWrapper.queryById(statId);
        if(statExtEntity == null){
            throw new IllegalArgumentException("statistic:" + statId + " not exist!");
        }
        return LimitStorageSelector.query(statExtEntity,batchTime);
    }
}
