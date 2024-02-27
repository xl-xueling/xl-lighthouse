package com.dtstep.lighthouse.core.wrapper;
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
import com.dtstep.lighthouse.common.enums.ColumnTypeEnum;
import com.dtstep.lighthouse.common.enums.StatStateEnum;
import com.dtstep.lighthouse.common.modal.Column;
import com.dtstep.lighthouse.common.modal.Group;
import com.dtstep.lighthouse.common.modal.Stat;
import com.dtstep.lighthouse.common.util.*;
import com.dtstep.lighthouse.core.builtin.BuiltinLoader;
import com.dtstep.lighthouse.core.config.LDPConfig;
import com.dtstep.lighthouse.core.dao.ConnectionManager;
import com.dtstep.lighthouse.core.dao.DBConnection;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.dtstep.lighthouse.common.constant.StatConst;
import com.dtstep.lighthouse.common.entity.group.GroupExtEntity;
import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.entity.stat.TimeParam;
import com.dtstep.lighthouse.common.enums.limiting.LimitingStrategyEnum;
import com.dtstep.lighthouse.common.enums.GroupStateEnum;
import com.dtstep.lighthouse.core.dao.DaoHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


public final class GroupDBWrapper {

    private static final Logger logger = LoggerFactory.getLogger(GroupDBWrapper.class);

    private static final Cache<Object, Optional<GroupExtEntity>> groupCache = Caffeine.newBuilder()
            .expireAfterWrite(3, TimeUnit.MINUTES)
            .maximumSize(100000)
            .softValues()
            .build();

    public static GroupExtEntity queryByToken(String token){
        Optional<GroupExtEntity> optional = groupCache.get(token, k -> actualQueryGroupByToken(token));
        assert optional != null;
        return optional.orElse(null);
    }


    public static GroupExtEntity queryById(int groupId){
        Optional<GroupExtEntity> optional = groupCache.get(groupId, (k) -> actualQueryGroupById(groupId));
        assert optional != null;
        return optional.orElse(null);
    }

    static {
        ScheduledExecutorService service = Executors.newScheduledThreadPool(1,
                new BasicThreadFactory.Builder().namingPattern("group-cache-refresh-schedule-pool-%d").daemon(true).build());
        service.scheduleWithFixedDelay(new RefreshThread(),0,20, TimeUnit.SECONDS);
    }

    public static Optional<GroupExtEntity> actualQueryGroupByToken(String token){
        if(BuiltinLoader.isBuiltinGroup(token)){
            return Optional.ofNullable(BuiltinLoader.getBuiltinGroup(token));
        }
        GroupExtEntity groupExtEntity = null;
        try{
            Group groupEntity = DaoHelper.sql.getItem(Group.class, "select * from ldp_groups where token = ?", token);
            if(groupEntity != null){
                groupExtEntity = combineExtInfo(groupEntity);
            }
            return Optional.ofNullable(groupExtEntity);
        }catch (Exception ex){
            logger.error("query group info error!",ex);
            return Optional.empty();
        }
    }

    public static Optional<GroupExtEntity> actualQueryGroupById(int groupId){
        if(BuiltinLoader.isBuiltinGroup(groupId)){
            return Optional.of(BuiltinLoader.getBuiltinGroup(groupId));
        }
        GroupExtEntity groupExtEntity = null;
        try{
            Group groupEntity = DaoHelper.sql.getItem(Group.class, "select * from ldp_groups where id = ?", groupId);
            if(groupEntity != null){
                groupExtEntity = combineExtInfo(groupEntity);
            }
            return Optional.ofNullable(groupExtEntity);
        }catch (Exception ex){
            logger.error("query group info error!",ex);
            return Optional.empty();
        }
    }

    private static class GroupResultSetHandler implements ResultSetHandler<Group> {
        @Override
        public Group handle(ResultSet rs) throws SQLException {
            Group group = null;
            if(rs.next()){
                group = new Group();
                Integer id = rs.getInt("id");
                String token = rs.getString("token");
                Integer projectId = rs.getInt("project_id");
                Integer debugMode = rs.getInt("debug_mode");
                String columns = rs.getString("columns");
                String desc = rs.getString("desc");
                String secretKey = rs.getString("secret_key");
                Long createTime = rs.getTimestamp("create_time").getTime();
                Long updateTime = rs.getTimestamp("update_time").getTime();
                Integer state = rs.getInt("state");
                Long refreshTime = rs.getTimestamp("refresh_time").getTime();
                group.setId(id);
                group.setToken(token);
                group.setProjectId(projectId);
                group.setDebugMode(debugMode);
                group.setDesc(desc);
                group.setSecretKey(secretKey);
                group.setCreateTime(DateUtil.timestampToLocalDateTime(createTime));
                group.setUpdateTime(DateUtil.timestampToLocalDateTime(updateTime));
                GroupStateEnum statStateEnum = GroupStateEnum.forValue(state);
                group.setState(statStateEnum);
                List<Column> columnList = JsonUtil.toJavaObjectList(columns,Column.class);
                group.setColumns(columnList);
                group.setRefreshTime(DateUtil.timestampToLocalDateTime(refreshTime));
            }
            return group;
        }
    }

    private static Group queryGroupByIdFromDB(int groupId) throws Exception {
        DBConnection dbConnection = ConnectionManager.getConnection();
        Connection conn = dbConnection.getConnection();
        QueryRunner queryRunner = new QueryRunner();
        Group group = null;
        try{
            group = queryRunner.query(conn, String.format("select * from ldp_groups where id = '%s'",groupId), new GroupResultSetHandler());
        }finally {
            ConnectionManager.close(dbConnection);
        }
        return group;
    }

    private static Group queryGroupByTokenFromDB(String token) throws Exception {
        DBConnection dbConnection = ConnectionManager.getConnection();
        Connection conn = dbConnection.getConnection();
        QueryRunner queryRunner = new QueryRunner();
        Group group = null;
        try{
            group = queryRunner.query(conn, String.format("select * from ldp_groups where token = '%s'",token), new GroupResultSetHandler());
        }finally {
            ConnectionManager.close(dbConnection);
        }
        return group;
    }

    private static GroupExtEntity combineExtInfo(Group groupEntity) throws Exception{
        GroupExtEntity groupExtEntity = new GroupExtEntity(groupEntity);
        if(GroupExtEntity.isLimitedExpired(groupExtEntity)){
            DaoHelper.sql.execute("update ldp_groups set state = ?,update_time = ? where id = ?", GroupStateEnum.RUNNING.getState(),new Date(), groupExtEntity.getId());
            groupExtEntity.setState(GroupStateEnum.RUNNING);
        }
        List<Column> columnList = groupEntity.getColumns();
        int groupId = groupExtEntity.getId();
        List<StatExtEntity> statExtEntityList = StatDBWrapper.actualQueryListByGroupId(groupId).orElse(null);
        if(CollectionUtils.isNotEmpty(statExtEntityList)){
            HashMap<String, ColumnTypeEnum> groupRunningRelatedColumns = new HashMap<>();
            HashMap<String, ColumnTypeEnum> groupAllRelatedColumns = new HashMap<>();
            Map<String,ColumnTypeEnum> groupColumnsMap = columnList.stream().collect(Collectors.toMap(Column::getName, Column::getType));
            long minDuration = 0L;
            long maxDataExpire = 0L;
            for (StatExtEntity statExtEntity : statExtEntityList) {
                if(statExtEntity.getStatStateEnum() == StatStateEnum.RUNNING){
                    long currentDuration = TimeParam.calculateDuration(statExtEntity.getTimeParamInterval(), statExtEntity.getTimeUnit());
                    if(minDuration == 0L){
                        minDuration = currentDuration;
                    }else{
                        minDuration = CalculateUtil.getMaxDivisor(minDuration,currentDuration);
                    }
                    if(maxDataExpire < statExtEntity.getExpired()){
                        maxDataExpire = statExtEntity.getExpired();
                    }
                    Set<String> statRelatedColumnSet = statExtEntity.getRelatedColumnSet();
                    if (CollectionUtils.isNotEmpty(statRelatedColumnSet)) {
                        for(String columnName : statRelatedColumnSet){
                            groupRunningRelatedColumns.put(columnName,groupColumnsMap.get(columnName));
                            groupAllRelatedColumns.put(columnName,groupColumnsMap.get(columnName));
                        }
                    }
                }else{
                    Set<String> statRelatedColumnSet = statExtEntity.getRelatedColumnSet();
                    if (CollectionUtils.isNotEmpty(statRelatedColumnSet)) {
                        for(String columnName : statRelatedColumnSet){
                            groupAllRelatedColumns.put(columnName,groupColumnsMap.get(columnName));
                        }
                    }
                }
            }
            groupExtEntity.setAllRelatedColumns(groupAllRelatedColumns);
            groupExtEntity.setRunningRelatedColumns(groupRunningRelatedColumns);
            TimeParam minTimeParam = transToTimeParam(minDuration);
            groupExtEntity.setMinTimeParam(minTimeParam);
            groupExtEntity.setDataExpire(maxDataExpire);
        }
        groupExtEntity.setVerifyKey(Md5Util.getMD5(groupExtEntity.getSecretKey()));
        ObjectMapper objectMapper = new ObjectMapper();
        HashMap<String,Integer> limitedThresholdMap = groupExtEntity.getLimitedThresholdMap();
        if(!limitedThresholdMap.containsKey(LimitingStrategyEnum.GROUP_MESSAGE_SIZE_LIMIT.getStrategy())){
            int limit = LDPConfig.getOrDefault(LDPConfig.KEY_LIMITED_GROUP_MESSAGE_SIZE_PER_SEC,-1,Integer.class);
            limitedThresholdMap.put(LimitingStrategyEnum.GROUP_MESSAGE_SIZE_LIMIT.getStrategy(),limit);
        }
        if(!limitedThresholdMap.containsKey(LimitingStrategyEnum.STAT_RESULT_SIZE_LIMIT.getStrategy())){
            int limit = LDPConfig.getOrDefault(LDPConfig.KEY_LIMITED_STAT_RESULT_SIZE_PER_SEC,-1,Integer.class);
            limitedThresholdMap.put(LimitingStrategyEnum.STAT_RESULT_SIZE_LIMIT.getStrategy(),limit);
        }
        return groupExtEntity;
    }

    private static TimeParam transToTimeParam(long duration) throws Exception {
        TimeParam timeParam;
        if(duration % TimeUnit.DAYS.toMillis(1) == 0){
            timeParam = new TimeParam((int)(duration / TimeUnit.DAYS.toMillis(1)),TimeUnit.DAYS);
        }else if(duration % TimeUnit.HOURS.toMillis(1) == 0){
            timeParam = new TimeParam((int)(duration / TimeUnit.HOURS.toMillis(1)),TimeUnit.HOURS);
        }else if(duration % TimeUnit.MINUTES.toMillis(1) == 0){
            timeParam = new TimeParam((int)(duration / TimeUnit.MINUTES.toMillis(1)),TimeUnit.MINUTES);
        }else if(duration % TimeUnit.SECONDS.toMillis(1) == 0){
            timeParam = new TimeParam((int)(duration / TimeUnit.SECONDS.toMillis(1)),TimeUnit.SECONDS);
        }else{
            throw new Exception();
        }
        return timeParam;
    }

    public static int changeState(GroupExtEntity groupExtEntity, int state) throws Exception {
         GroupStateEnum groupStateEnum = GroupStateEnum.forValue(state);
         groupExtEntity.setGroupStateEnum(groupStateEnum);
         int result;
         if(groupStateEnum == GroupStateEnum.LIMITING){
             result = DaoHelper.sql.execute("update ldp_groups set state = ?,update_time = ? where id = ? and state = ?",state,new Date(), groupExtEntity.getId(),GroupStateEnum.RUNNING.getState());
         }else{
             result = DaoHelper.sql.execute("update ldp_groups set state = ?,update_time = ? where id = ?",state,new Date(), groupExtEntity.getId());
         }
         return result;
    }

    public static GroupStateEnum getState(int groupId) throws Exception {
        Group groupEntity = DaoHelper.sql.getItem(Group.class,"select state,update_time from ldp_groups where id = ?",groupId);
        if(groupEntity == null){
            return null;
        }
        boolean isLimited = groupEntity.getState() == GroupStateEnum.LIMITING
                && (System.currentTimeMillis() - groupEntity.getUpdateTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() < TimeUnit.MINUTES.toMillis(StatConst.LIMITED_EXPIRE_MINUTES));
        if(isLimited){
            return GroupStateEnum.LIMITING;
        }else{
            return groupEntity.getState();
        }
    }

    public static void clearLocalCache(int groupId){
        GroupExtEntity groupExtEntity = GroupDBWrapper.queryById(groupId);
        assert groupExtEntity != null;
        groupCache.invalidate(groupId);
        groupCache.invalidate(groupExtEntity.getToken());
    }

    static class RefreshThread implements Runnable {

        @Override
        public void run() {
            long time = DateUtil.getSecondBefore(System.currentTimeMillis(),20);
            try{
                List<Integer> ids = DaoHelper.sql.getList(Integer.class,"select id from ldp_groups where create_time != refresh_time and refresh_time > ?",new Date(time));
                if(CollectionUtils.isNotEmpty(ids)){
                    for(int groupId:ids){
                        GroupExtEntity groupEntity = GroupDBWrapper.queryById(groupId);
                        if(groupEntity != null){
                            clearLocalCache(groupId);
                            logger.info("refresh group cache success,groupId:{}",groupId);
                        }
                    }
                }
            }catch (Exception ex){
                logger.error("statistic group cache refresh error!",ex);
            }
        }
    }

}

