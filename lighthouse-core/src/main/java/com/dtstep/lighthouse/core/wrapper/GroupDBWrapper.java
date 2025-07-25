package com.dtstep.lighthouse.core.wrapper;
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
import com.dtstep.lighthouse.common.entity.ServiceResult;
import com.dtstep.lighthouse.common.entity.stat.TemplateEntity;
import com.dtstep.lighthouse.common.enums.*;
import com.dtstep.lighthouse.common.modal.*;
import com.dtstep.lighthouse.common.util.*;
import com.dtstep.lighthouse.core.builtin.BuiltinLoader;
import com.dtstep.lighthouse.core.config.LDPConfig;
import com.dtstep.lighthouse.core.formula.FormulaTranslate;
import com.dtstep.lighthouse.common.schedule.ScheduledThreadPoolBuilder;
import com.dtstep.lighthouse.core.storage.cmdb.CMDBStorageEngine;
import com.dtstep.lighthouse.core.storage.cmdb.CMDBStorageEngineProxy;
import com.dtstep.lighthouse.core.template.TemplateContext;
import com.dtstep.lighthouse.core.template.TemplateParser;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.dtstep.lighthouse.common.constant.StatConst;
import com.dtstep.lighthouse.common.entity.group.GroupExtEntity;
import com.dtstep.lighthouse.common.entity.stat.TimeParam;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


public final class GroupDBWrapper {

    private static final Logger logger = LoggerFactory.getLogger(GroupDBWrapper.class);

    private static final Integer _CacheExpireMinutes = 3;

    private static final CMDBStorageEngine<Connection> storageEngine = CMDBStorageEngineProxy.getInstance();

    private static final Cache<Object, Optional<GroupExtEntity>> groupCache = Caffeine.newBuilder()
            .expireAfterWrite(_CacheExpireMinutes, TimeUnit.MINUTES)
            .maximumSize(500000)
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
        ScheduledExecutorService service = ScheduledThreadPoolBuilder.newScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("group-cache-refresh-schedule-pool-%d").daemon(true).build());
        service.scheduleWithFixedDelay(new RefreshThread(),0,20, TimeUnit.SECONDS);
    }

    public static Optional<GroupExtEntity> actualQueryGroupByToken(String token){
        if(BuiltinLoader.isBuiltinGroup(token)){
            return Optional.ofNullable(BuiltinLoader.getBuiltinGroup(token));
        }
        GroupExtEntity groupExtEntity = null;
        try{
            Group groupEntity = queryGroupByTokenFromDB(token);
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
            return Optional.ofNullable(BuiltinLoader.getBuiltinGroup(groupId));
        }
        GroupExtEntity groupExtEntity = null;
        try{
            Group groupEntity = queryGroupByIdFromDB(groupId);
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
                int debugMode = rs.getInt("debug_mode");
                String columns = rs.getString("columns");
                Integer dataVersion = rs.getInt("data_version");
                String randomId = rs.getString("random_id");
                String desc = rs.getString("desc");
                String extendConfig = rs.getString("extend_config");
                if(StringUtil.isNotEmpty(extendConfig)){
                    GroupExtendConfig groupExtendConfig = JsonUtil.toJavaObject(extendConfig,GroupExtendConfig.class);
                    group.setExtendConfig(groupExtendConfig);
                }
                String limitingParam = rs.getString("limiting_param");
                if(StringUtil.isNotEmpty(limitingParam)){
                    LimitingParam groupLimitingParam = JsonUtil.toJavaObject(limitingParam, LimitingParam.class);
                    group.setLimitingParam(groupLimitingParam);
                }
                String debugParam = rs.getString("debug_param");
                if(StringUtil.isNotEmpty(debugParam)){
                    DebugParam groupDebugParam = JsonUtil.toJavaObject(debugParam, DebugParam.class);
                    group.setDebugParam(groupDebugParam);
                }
                String secretKey = rs.getString("secret_key");
                long createTime = rs.getTimestamp("create_time").getTime();
                long updateTime = rs.getTimestamp("update_time").getTime();
                int state = rs.getInt("state");
                long refreshTime = rs.getTimestamp("refresh_time").getTime();
                group.setId(id);
                group.setToken(token);
                group.setProjectId(projectId);
                SwitchStateEnum debugModeState = SwitchStateEnum.forValue(debugMode);
                group.setDebugMode(debugModeState);
                group.setDesc(desc);
                group.setDataVersion(dataVersion);
                group.setSecretKey(secretKey);
                group.setRandomId(randomId);
                group.setCreateTime(DateUtil.timestampToLocalDateTime(createTime));
                group.setUpdateTime(DateUtil.timestampToLocalDateTime(updateTime));
                group.setRefreshTime(DateUtil.timestampToLocalDateTime(refreshTime));
                GroupStateEnum statStateEnum = GroupStateEnum.forValue(state);
                group.setState(statStateEnum);
                List<Column> columnList = JsonUtil.toJavaObjectList(columns,Column.class);
                group.setColumns(columnList);
            }
            return group;
        }
    }

    private static Group queryGroupByIdFromDB(int groupId) throws Exception {
        Connection conn = storageEngine.getConnection();
        QueryRunner queryRunner = new QueryRunner();
        Group group;
        try{
            group = queryRunner.query(conn, String.format("select * from ldp_groups where id = '%s'",groupId), new GroupResultSetHandler());
        }finally {
            storageEngine.closeConnection();
        }
        return group;
    }

    private static Group queryGroupByTokenFromDB(String token) throws Exception {
        Connection conn = storageEngine.getConnection();
        QueryRunner queryRunner = new QueryRunner();
        Group group;
        try{
            group = queryRunner.query(conn, String.format("select * from ldp_groups where token = '%s'",token), new GroupResultSetHandler());
        }finally {
            storageEngine.closeConnection();
        }
        return group;
    }

    public static int changeState(int groupId, GroupStateEnum groupStateEnum, LocalDateTime date) throws Exception {
        Connection conn = storageEngine.getConnection();
        QueryRunner queryRunner = new QueryRunner();
        int result;
        try{
            if(groupStateEnum == GroupStateEnum.LIMITING){
                long startTime = DateUtil.translateToTimeStamp(date);
                long endTime = DateUtil.getMinuteAfter(startTime,StatConst.LIMITING_EXPIRE_MINUTES);
                LimitingParam limitingParam = new LimitingParam();
                limitingParam.setStartTime(startTime);
                limitingParam.setEndTime(endTime);
                result = queryRunner.update(conn, "update ldp_groups set state = ?,refresh_time = ?,limiting_param =? where id = ? and state = ?", groupStateEnum.getState(),date, JsonUtil.toJSONString(limitingParam),groupId,GroupStateEnum.RUNNING.getState());
            }else{
                result = queryRunner.update(conn, "update ldp_groups set state = ?,refresh_time = ? where id = ?", groupStateEnum.getState(),date, groupId);
            }
        }finally {
            storageEngine.closeConnection();
        }
        return result;
    }

    public static int changeDebugMode(int groupId,SwitchStateEnum switchStateEnum,LocalDateTime date) throws Exception {
        Connection conn = storageEngine.getConnection();
        QueryRunner queryRunner = new QueryRunner();
        int result;
        try{
            if(switchStateEnum == SwitchStateEnum.OPEN){
                long startTime = DateUtil.translateToTimeStamp(date);
                long endTime = DateUtil.getMinuteAfter(startTime,StatConst.LIMITING_EXPIRE_MINUTES);
                DebugParam debugParam = new DebugParam();
                debugParam.setStartTime(startTime);
                debugParam.setEndTime(endTime);
                result = queryRunner.update(conn, "update ldp_groups set debug_mode = ?,refresh_time = ?,debug_param =? where id = ? and debug_mode = ?", switchStateEnum.getState(),date,JsonUtil.toJSONString(debugParam), groupId,SwitchStateEnum.CLOSE.getState());
            }else{
                result = queryRunner.update(conn, "update ldp_groups set debug_mode = ?,refresh_time = ? where id = ? and debug_mode = ?", switchStateEnum.getState(),date, groupId,SwitchStateEnum.OPEN.getState());
            }
        }finally {
            storageEngine.closeConnection();
        }
        return result;
    }

    public static GroupExtEntity combineExtInfo(Group groupEntity) throws Exception{
        GroupExtEntity groupExtEntity = new GroupExtEntity(groupEntity);
        if(GroupExtEntity.isLimitedExpired(groupExtEntity)){
            changeState(groupEntity.getId(),GroupStateEnum.RUNNING,LocalDateTime.now());
            groupExtEntity.setState(GroupStateEnum.RUNNING);
        }
        if(GroupExtEntity.isDebugModeExpired(groupExtEntity)){
            changeDebugMode(groupEntity.getId(),SwitchStateEnum.CLOSE,LocalDateTime.now());
            groupExtEntity.setDebugMode(SwitchStateEnum.CLOSE);
        }
        List<Column> columnList = groupEntity.getColumns();
        int groupId = groupExtEntity.getId();
        List<Stat> statList = StatDBWrapper.queryStatByGroupIDFromDB(groupId);
        if(CollectionUtils.isNotEmpty(statList)){
            HashMap<String, ColumnTypeEnum> groupRunningRelatedColumns = new HashMap<>();
            HashMap<String, ColumnTypeEnum> groupAllRelatedColumns = new HashMap<>();
            Map<String,ColumnTypeEnum> groupColumnsMap = columnList.stream().collect(Collectors.toMap(Column::getName, Column::getType));
            long minDuration = 0L;
            long maxDataExpire = 0L;
            for (Stat stat : statList) {
                String template = stat.getTemplate();
                List<Column> statRelatedColumns = FormulaTranslate.queryRelatedColumns(columnList,template);
                ServiceResult<TemplateEntity> serviceResult = TemplateParser.parseConfig(new TemplateContext(stat.getId(),template,stat.getTimeparam(),columnList));
                if(!serviceResult.isSuccess()){
                    logger.error("load stat error,id:{},template:{}.", stat.getId(),template);
                    continue;
                }
                String dimens = serviceResult.getData().getDimens();
                List<Column> dimensRelatedColumns = null;
                if(!StringUtil.isEmpty(dimens)){
                    dimensRelatedColumns = FormulaTranslate.queryRelatedColumns(columnList,dimens);
                }
                if(stat.getState() == StatStateEnum.RUNNING || stat.getState() == StatStateEnum.LIMITING){
                    long currentDuration = TimeParam.calculateDuration(stat.getTimeparam());
                    if(minDuration == 0L){
                        minDuration = currentDuration;
                    }else{
                        minDuration = CalculateUtil.getMaxDivisor(minDuration,currentDuration);
                    }
                    if(maxDataExpire < stat.getExpired()){
                        maxDataExpire = stat.getExpired();
                    }
                    if (CollectionUtils.isNotEmpty(statRelatedColumns)) {
                        for(Column column : statRelatedColumns){
                            groupRunningRelatedColumns.put(column.getName(),groupColumnsMap.get(column.getName()));
                            groupAllRelatedColumns.put(column.getName(),groupColumnsMap.get(column.getName()));
                        }
                    }
                    if(CollectionUtils.isNotEmpty(dimensRelatedColumns)){
                        for(Column column : dimensRelatedColumns){
                            groupRunningRelatedColumns.put(column.getName(),groupColumnsMap.get(column.getName()));
                            groupAllRelatedColumns.put(column.getName(),groupColumnsMap.get(column.getName()));
                        }
                    }
                }else{
                    if (CollectionUtils.isNotEmpty(statRelatedColumns)) {
                        for(Column column : statRelatedColumns){
                            groupAllRelatedColumns.put(column.getName(),groupColumnsMap.get(column.getName()));
                        }
                    }
                    if(CollectionUtils.isNotEmpty(dimensRelatedColumns)){
                        for(Column column : dimensRelatedColumns){
                            groupAllRelatedColumns.put(column.getName(),groupColumnsMap.get(column.getName()));
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
        if(StringUtil.isNotEmpty(groupExtEntity.getSecretKey())){
            groupExtEntity.setVerifyKey(Md5Util.getMD5(groupExtEntity.getSecretKey()));
        }
        GroupExtendConfig groupExtendConfig = groupExtEntity.getExtendConfig();
        HashMap<LimitingStrategyEnum, Integer> limitingMap = groupExtendConfig.getLimitingConfig();
        if(!limitingMap.containsKey(LimitingStrategyEnum.GROUP_MESSAGE_SIZE_LIMITING)){
            int limit = LDPConfig.getOrDefault(LDPConfig.KEY_LIMITING_GROUP_MESSAGE_SIZE_PER_SEC,-1,Integer.class);
            limitingMap.put(LimitingStrategyEnum.GROUP_MESSAGE_SIZE_LIMITING,limit);
        }
        if(!limitingMap.containsKey(LimitingStrategyEnum.STAT_RESULT_SIZE_LIMITING)){
            int limit = LDPConfig.getOrDefault(LDPConfig.KEY_LIMITING_STAT_RESULT_SIZE_PER_SEC,-1,Integer.class);
            limitingMap.put(LimitingStrategyEnum.STAT_RESULT_SIZE_LIMITING,limit);
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

    public static GroupStateEnum getState(int groupId) throws Exception {
        Group groupEntity = queryGroupByIdFromDB(groupId);
        if(groupEntity == null){
            return null;
        }
        boolean isLimited = groupEntity.getState() == GroupStateEnum.LIMITING
                && (System.currentTimeMillis() - groupEntity.getRefreshTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() < TimeUnit.MINUTES.toMillis(StatConst.LIMITING_EXPIRE_MINUTES));
        if(isLimited){
            return GroupStateEnum.LIMITING;
        }else if(groupEntity.getState() == GroupStateEnum.LIMITING){
            return GroupStateEnum.RUNNING;
        }else {
            return groupEntity.getState();
        }
    }

    private static class RefreshListSetHandler implements ResultSetHandler<List<RefreshEntity>> {

        @Override
        public List<RefreshEntity> handle(ResultSet resultSet) throws SQLException {
            List<RefreshEntity> list = new ArrayList<>();
            while (resultSet.next()){
                Integer id = resultSet.getInt("id");
                String token = resultSet.getString("token");
                long refreshTime = resultSet.getTimestamp("refresh_time").getTime();
                list.add(new RefreshEntity(id,token,refreshTime));
            }
            return list;
        }
    }

    private static List<RefreshEntity> queryRefreshIdList() throws Exception {
        Connection conn = storageEngine.getConnection();
        QueryRunner queryRunner = new QueryRunner();
        List<RefreshEntity> ids;
        try{
            long time = DateUtil.getMinuteBefore(System.currentTimeMillis(),_CacheExpireMinutes);
            ids = queryRunner.query(conn, "select id,token,refresh_time from ldp_groups where create_time != refresh_time and refresh_time >= ? limit 10000", new RefreshListSetHandler(),new Date(time));
        }finally {
            storageEngine.closeConnection();
        }
        return ids;
    }

    private static class RefreshEntity {

        private Integer id;

        private String token;

        private Long refreshTime;

        public RefreshEntity(Integer id,String token,Long refreshTime){
            this.id = id;
            this.token = token;
            this.refreshTime = refreshTime;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public Long getRefreshTime() {
            return refreshTime;
        }

        public void setRefreshTime(Long refreshTime) {
            this.refreshTime = refreshTime;
        }
    }

    static class RefreshThread implements Runnable {

        @Override
        public void run() {
            try{
                List<RefreshEntity> entities = queryRefreshIdList();
                if(CollectionUtils.isNotEmpty(entities)){
                    for(RefreshEntity refreshEntity:entities){
                        Optional<GroupExtEntity> cacheById = groupCache.getIfPresent(refreshEntity.getId());
                        if(cacheById != null && cacheById.get().getRefreshTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() < refreshEntity.getRefreshTime()){
                            if(logger.isTraceEnabled()){
                                logger.trace("clear group local cache,id:{},token:{}",refreshEntity.getId(),refreshEntity.getToken());
                            }
                            groupCache.invalidate(refreshEntity.getId());
                            groupCache.invalidate(refreshEntity.getToken());
                        }
                        Optional<GroupExtEntity> cacheByToken = groupCache.getIfPresent(refreshEntity.getToken());
                        if(cacheByToken != null && cacheByToken.get().getRefreshTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() < refreshEntity.getRefreshTime()){
                            if(logger.isTraceEnabled()){
                                logger.trace("clear group local cache,id:{},token:{}",refreshEntity.getId(),refreshEntity.getToken());
                            }
                            groupCache.invalidate(refreshEntity.getId());
                            groupCache.invalidate(refreshEntity.getToken());
                        }
                    }
                }
            }catch (Exception ex){
                logger.error("statistic group cache refresh error!",ex);
            }
        }
    }
}

