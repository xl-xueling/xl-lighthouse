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
import com.dtstep.lighthouse.common.constant.StatConst;
import com.dtstep.lighthouse.common.entity.AlarmExtEntity;
import com.dtstep.lighthouse.common.entity.ServiceResult;
import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.entity.stat.TemplateEntity;
import com.dtstep.lighthouse.common.entity.state.StatState;
import com.dtstep.lighthouse.common.entity.state.StatUnit;
import com.dtstep.lighthouse.common.enums.GroupStateEnum;
import com.dtstep.lighthouse.common.enums.PrivateTypeEnum;
import com.dtstep.lighthouse.common.enums.StatStateEnum;
import com.dtstep.lighthouse.common.modal.Column;
import com.dtstep.lighthouse.common.modal.LimitingParam;
import com.dtstep.lighthouse.common.modal.RenderConfig;
import com.dtstep.lighthouse.common.modal.Stat;
import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.common.util.StringUtil;
import com.dtstep.lighthouse.core.builtin.BuiltinLoader;
import com.dtstep.lighthouse.core.formula.FormulaTranslate;
import com.dtstep.lighthouse.common.schedule.ScheduledThreadPoolBuilder;
import com.dtstep.lighthouse.core.plugins.PluginManager;
import com.dtstep.lighthouse.core.storage.cmdb.CMDBStorageEngine;
import com.dtstep.lighthouse.core.storage.cmdb.CMDBStorageEngineProxy;
import com.dtstep.lighthouse.core.template.TemplateContext;
import com.dtstep.lighthouse.core.template.TemplateParser;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
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

public class StatDBWrapper {

    private static final Logger logger = LoggerFactory.getLogger(StatDBWrapper.class);

    private static final Integer _CacheExpireMinutes = 3;

    private static final CMDBStorageEngine<Connection> storageEngine = CMDBStorageEngineProxy.getInstance();

    private static final LoadingCache<Integer, Optional<StatExtEntity>> statCache = Caffeine.newBuilder()
            .expireAfterWrite(_CacheExpireMinutes, TimeUnit.MINUTES)
            .softValues()
            .maximumSize(500000)
            .build(StatDBWrapper::actualQueryById);

    private final static LoadingCache<Integer, Optional<List<StatExtEntity>>> groupStatListCache = Caffeine.newBuilder()
            .expireAfterWrite(_CacheExpireMinutes, TimeUnit.MINUTES)
            .maximumSize(500000)
            .softValues()
            .build(StatDBWrapper::actualQueryListByGroupId);

    static {
        ScheduledExecutorService service = ScheduledThreadPoolBuilder.newScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("stat-cache-refresh-schedule-pool-%d").daemon(true).build());
        service.scheduleWithFixedDelay(new RefreshThread(),0,20, TimeUnit.SECONDS);
    }

    public static StatExtEntity queryById(int statId) {
        return Objects.requireNonNull(statCache.get(statId)).orElse(null);
    }

    public static Optional<StatExtEntity> actualQueryById(int statId) {
        if(BuiltinLoader.isBuiltinStat(statId)){
            return Optional.ofNullable(BuiltinLoader.getBuiltinStat(statId));
        }
        StatExtEntity statExtEntity = null;
        try{
            Stat statEntity = queryStatByIdFromDB(statId);
            if(statEntity != null){
                statExtEntity = combineExtInfo(statEntity,false);
            }
            return Optional.ofNullable(statExtEntity);
        }catch (Exception ex){
            logger.error("query stat info error,id:{}",statId,ex);
            return Optional.empty();
        }
    }

    private static class StatResultListHandler implements ResultSetHandler<List<Stat>> {

        @Override
        public List<Stat> handle(ResultSet rs) throws SQLException {
            List<Stat> statList = new ArrayList<>();
            while (rs.next()){
                Stat stat = new Stat();
                Integer id = rs.getInt("id");
                String title = rs.getString("title");
                Integer groupId = rs.getInt("group_id");
                Integer projectId = rs.getInt("project_id");
                Integer dataVersion = rs.getInt("data_version");
                String template = rs.getString("template");
                String timeparam = rs.getString("timeparam");
                Long expired = rs.getLong("expired");
                int state = rs.getInt("state");
                String renderConfig = rs.getString("render_config");
                if(StringUtil.isNotEmpty(renderConfig)){
                    stat.setRenderConfig(JsonUtil.toJavaObject(renderConfig, RenderConfig.class));
                }
                String limitingParam = rs.getString("limiting_param");
                if(StringUtil.isNotEmpty(limitingParam)){
                    LimitingParam statLimitingParam = JsonUtil.toJavaObject(limitingParam, LimitingParam.class);
                    stat.setLimitingParam(statLimitingParam);
                }
                Integer metaId = rs.getInt("meta_id");
                long createTime = rs.getTimestamp("create_time").getTime();
                long updateTime = rs.getTimestamp("update_time").getTime();
                long refreshTime = rs.getTimestamp("refresh_time").getTime();
                String randomId = rs.getString("random_id");
                String columns = rs.getString("columns");
                stat.setId(id);
                stat.setTitle(title);
                stat.setGroupId(groupId);
                stat.setProjectId(projectId);
                stat.setTemplate(template);
                stat.setTimeparam(timeparam);
                stat.setDataVersion(dataVersion);
                stat.setExpired(expired);
                StatStateEnum statStateEnum = StatStateEnum.getByState(state);
                stat.setState(statStateEnum);
                stat.setMetaId(metaId);
                stat.setCreateTime(DateUtil.timestampToLocalDateTime(createTime));
                stat.setUpdateTime(DateUtil.timestampToLocalDateTime(updateTime));
                stat.setRefreshTime(DateUtil.timestampToLocalDateTime(refreshTime));
                stat.setRandomId(randomId);
                stat.setGroupColumns(columns);
                statList.add(stat);
            }
            return statList;
        }
    }

    private static class StatResultSetHandler implements ResultSetHandler<Stat> {

        @Override
        public Stat handle(ResultSet rs) throws SQLException {
            Stat stat = null;
            if(rs.next()){
                stat = new Stat();
                Integer id = rs.getInt("id");
                String title = rs.getString("title");
                Integer groupId = rs.getInt("group_id");
                Integer projectId = rs.getInt("project_id");
                String token = rs.getString("token");
                String template = rs.getString("template");
                String timeparam = rs.getString("timeparam");
                Integer dataVersion = rs.getInt("data_version");
                Long expired = rs.getLong("expired");
                int state = rs.getInt("state");
                String renderConfig = rs.getString("render_config");
                if(StringUtil.isNotEmpty(renderConfig)){
                    stat.setRenderConfig(JsonUtil.toJavaObject(renderConfig, RenderConfig.class));
                }
                String limitingParam = rs.getString("limiting_param");
                if(StringUtil.isNotEmpty(limitingParam)){
                    LimitingParam statLimitingParam = JsonUtil.toJavaObject(limitingParam, LimitingParam.class);
                    stat.setLimitingParam(statLimitingParam);
                }
                int privateType = rs.getInt("private_type");
                Integer metaId = rs.getInt("meta_id");
                long createTime = rs.getTimestamp("create_time").getTime();
                long updateTime = rs.getTimestamp("update_time").getTime();
                long refreshTime = rs.getTimestamp("refresh_time").getTime();
                String randomId = rs.getString("random_id");
                String columns = rs.getString("columns");
                stat.setId(id);
                stat.setTitle(title);
                stat.setGroupId(groupId);
                stat.setProjectId(projectId);
                stat.setToken(token);
                stat.setTemplate(template);
                stat.setTimeparam(timeparam);
                stat.setExpired(expired);
                StatStateEnum statStateEnum = StatStateEnum.getByState(state);
                stat.setState(statStateEnum);
                stat.setPrivateType(PrivateTypeEnum.forValue(privateType));
                stat.setMetaId(metaId);
                stat.setCreateTime(DateUtil.timestampToLocalDateTime(createTime));
                stat.setUpdateTime(DateUtil.timestampToLocalDateTime(updateTime));
                stat.setRefreshTime(DateUtil.timestampToLocalDateTime(refreshTime));
                stat.setRandomId(randomId);
                stat.setGroupColumns(columns);
                stat.setDataVersion(dataVersion);
            }
            return stat;
        }
    }

    private static Stat queryStatByIdFromDB(int statId) throws Exception {
        Connection conn = storageEngine.getConnection();
        QueryRunner queryRunner = new QueryRunner();
        Stat stat;
        try{
            stat = queryRunner.query(conn, String.format("select a.*,b.token,b.columns,c.private_type from ldp_stats a left join ldp_groups b on a.group_id = b.id left join ldp_projects c on a.project_id = c.id where a.id = '%s'",statId), new StatResultSetHandler());
        }finally {
            storageEngine.closeConnection();
        }
        return stat;
    }

    public static List<Stat> queryStatByGroupIDFromDB(int groupId) throws Exception {
        Connection conn = storageEngine.getConnection();
        QueryRunner queryRunner = new QueryRunner();
        List<Stat> statList;
        try{
            statList = queryRunner.query(conn, String.format("select a.*,b.token,b.columns from ldp_stats a left join ldp_groups b on a.group_id = b.id where a.group_id = '%s'",groupId), new StatResultListHandler());
        }finally {
            storageEngine.closeConnection();
        }
        return statList;
    }

    public static Optional<List<StatExtEntity>> actualQueryListByGroupId(int groupId) throws Exception {
        List<Stat> entityList = queryStatByGroupIDFromDB(groupId);
        if(CollectionUtils.isEmpty(entityList)){
            return Optional.empty();
        }
        List<StatExtEntity> extEntityList = new ArrayList<>();
        entityList.forEach(z -> {
            try{
                StatExtEntity statExtEntity = combineExtInfo(z,false);
                if(statExtEntity != null){
                    extEntityList.add(statExtEntity);
                }
            }catch (Exception ex){
                logger.error("query stat info error,id:{}",z.getId(),ex);
            }
        });
        return Optional.of(extEntityList);
    }

    public static StatExtEntity combineExtInfo(Stat statEntity,boolean isBuiltIn) throws Exception {
        String groupColumns = statEntity.getGroupColumns();
        assert StringUtil.isNotEmpty(groupColumns);
        List<Column> groupColumnList = JsonUtil.toJavaObjectList(groupColumns,Column.class);
        StatExtEntity statExtEntity = new StatExtEntity(statEntity);
        String timeParam = statExtEntity.getTimeparam();
        String[] timeParamArr = timeParam.split("-");
        statExtEntity.setTimeParamInterval(Integer.parseInt(timeParamArr[0]));
        if("minute".equals(timeParamArr[1])){
            statExtEntity.setTimeUnit(TimeUnit.MINUTES);
        }else if("hour".equals(timeParamArr[1])){
            statExtEntity.setTimeUnit(TimeUnit.HOURS);
        }else if("day".equals(timeParamArr[1])){
            statExtEntity.setTimeUnit(TimeUnit.DAYS);
        }else if("second".equals(timeParamArr[1])){
            statExtEntity.setTimeUnit(TimeUnit.SECONDS);
        }
        StatStateEnum stateEnum = statExtEntity.getState();
        if(!isBuiltIn){
            GroupStateEnum groupStateEnum = GroupDBWrapper.getState(statEntity.getGroupId());
            if(groupStateEnum == GroupStateEnum.LIMITING && stateEnum == StatStateEnum.RUNNING){
                statExtEntity.setState(StatStateEnum.LIMITING);
            }else if(StatExtEntity.isLimitedExpired(statExtEntity)){
                changeState(statEntity.getId(),StatStateEnum.RUNNING,LocalDateTime.now());
            }
        }
        String template = statExtEntity.getTemplate();
        ServiceResult<TemplateEntity> serviceResult = TemplateParser.parseConfig(new TemplateContext(statEntity.getId(),template,timeParam,groupColumnList));
        if(!serviceResult.isSuccess()){
            return null;
        }
        TemplateEntity templateEntity = serviceResult.getData();
        statExtEntity.setTemplateEntity(templateEntity);
        List<Column> relatedColumns = new ArrayList<>();
        String stat = templateEntity.getStat();
        List<Column> statRelatedColumns = FormulaTranslate.queryRelatedColumns(groupColumnList,stat);
        if(CollectionUtils.isNotEmpty(statRelatedColumns)){
            relatedColumns.addAll(statRelatedColumns);
        }
        for(StatState statState : templateEntity.getStatStateList()){
            List<Column> stateRelatedColumns = new ArrayList<>();
            List<StatUnit> unitList = statState.getUnitList();
            if(CollectionUtils.isNotEmpty(unitList)){
                int index = StatState.isBitCountState(statState) ? 1 : 0;
                for(int i = index;i<unitList.size(); i++){
                    StatUnit statUnit = unitList.get(i);
                    List<Column> unitRelatedColumns = FormulaTranslate.queryRelatedColumns(groupColumnList,statUnit.getOrigin());
                    if(CollectionUtils.isNotEmpty(unitRelatedColumns)){
                        stateRelatedColumns.addAll(unitRelatedColumns);
                    }
                }
            }
            statState.setBuiltIn(isBuiltIn);
            statState.setStatId(statExtEntity.getId());
            statState.setGroupId(statExtEntity.getGroupId());
            statState.setRelatedColumnSet(stateRelatedColumns.stream().map(Column::getName).collect(Collectors.toSet()));
        }
        String dimens = templateEntity.getDimens();
        if(!StringUtil.isEmpty(dimens)){
            List<Column> dimensRelatedColumns = FormulaTranslate.queryRelatedColumns(groupColumnList,dimens);
            if(!dimensRelatedColumns.isEmpty()){
                relatedColumns.addAll(dimensRelatedColumns);
            }
        }
        statExtEntity.setRelatedColumnSet(relatedColumns.stream().map(Column::getName).collect(Collectors.toSet()));
        if(PluginManager.getAlarmPlugin().isPresent()){
            List<AlarmExtEntity> alarmList = AlarmDBWrapper.queryByStatId(statEntity.getId());
            statExtEntity.setAlarmList(alarmList);
            statExtEntity.setNeedAlarm(CollectionUtils.isNotEmpty(alarmList) && alarmList.stream().anyMatch(AlarmExtEntity::isState));
        }
        return statExtEntity;
    }

    public static int changeState(int statId, StatStateEnum statStateEnum, LocalDateTime date) throws Exception {
        Connection conn = storageEngine.getConnection();
        QueryRunner queryRunner = new QueryRunner();
        int result;
        try{
            if(statStateEnum == StatStateEnum.LIMITING){
                long startTime = DateUtil.translateToTimeStamp(date);
                long endTime = DateUtil.getMinuteAfter(startTime, StatConst.LIMITING_EXPIRE_MINUTES);
                LimitingParam limitingParam = new LimitingParam();
                limitingParam.setStartTime(startTime);
                limitingParam.setEndTime(endTime);
                result = queryRunner.update(conn, "update ldp_stats set state = ?,refresh_time = ?,limiting_param =? where id = ? and state = ?", statStateEnum.getState(),date,JsonUtil.toJSONString(limitingParam), statId,StatStateEnum.RUNNING.getState());
            }else{
                result = queryRunner.update(conn, "update ldp_stats set state = ?,refresh_time = ? where id = ?", statStateEnum.getState(),date, statId);
            }
        }finally {
            storageEngine.closeConnection();
        }
        return result;
    }

    public static List<StatExtEntity> queryListByGroupId(int groupId) {
        return Objects.requireNonNull(groupStatListCache.get(groupId)).orElse(null);
    }

    public static List<StatExtEntity> queryRunningListByGroupId(int groupId){
        List<StatExtEntity> entityList;
        if(BuiltinLoader.isBuiltinGroup(groupId)){
            entityList = BuiltinLoader.getBuiltinStatByGroupId(groupId);
        }else{
            entityList = queryListByGroupId(groupId);
        }
        if(CollectionUtils.isEmpty(entityList)){
            return Collections.emptyList();
        }
        return entityList.stream().filter(x -> x.getState() == StatStateEnum.RUNNING).collect(Collectors.toList());
    }

    private static class RefreshEntity {

        private Integer id;

        private Integer groupId;

        private String token;

        private Long refreshTime;

        public RefreshEntity(Integer id,Integer groupId,String token,Long refreshTime){
            this.id = id;
            this.token = token;
            this.groupId = groupId;
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

        public Integer getGroupId() {
            return groupId;
        }

        public void setGroupId(Integer groupId) {
            this.groupId = groupId;
        }
    }

    private static class RefreshListSetHandler implements ResultSetHandler<List<RefreshEntity>> {

        @Override
        public List<RefreshEntity> handle(ResultSet resultSet) throws SQLException {
            List<RefreshEntity> list = new ArrayList<>();
            while (resultSet.next()){
                Integer id = resultSet.getInt("id");
                Integer groupId = resultSet.getInt("group_id");
                long refreshTime = resultSet.getTimestamp("refresh_time").getTime();
                String token = resultSet.getString("token");
                list.add(new RefreshEntity(id,groupId,token,refreshTime));
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
            ids = queryRunner.query(conn, "select a.id,a.group_id,a.refresh_time,b.token from ldp_stats a inner join ldp_groups b on a.group_id = b.id where a.refresh_time >= ? limit 10000", new RefreshListSetHandler(),new Date(time));
        }finally {
            storageEngine.closeConnection();
        }
        return ids;
    }

    static class RefreshThread implements Runnable {

        @Override
        public void run() {
            try{
                List<RefreshEntity> entities = queryRefreshIdList();
                if(CollectionUtils.isNotEmpty(entities)){
                    for(RefreshEntity refreshEntity:entities){
                        Optional<StatExtEntity> statCacheById = statCache.getIfPresent(refreshEntity.getId());
                        if(statCacheById != null && statCacheById.get().getRefreshTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() < refreshEntity.getRefreshTime()){
                            if(logger.isTraceEnabled()){
                                logger.trace("clear stat local cache,id:{}",refreshEntity.getId());
                            }
                            statCache.invalidate(refreshEntity.getId());
                        }

                        Optional<List<StatExtEntity>> groupStatCache = groupStatListCache.getIfPresent(refreshEntity.getGroupId());
                        if(groupStatCache != null){
                            List<StatExtEntity> statCacheList = groupStatCache.get();
                            List<Integer> cacheIdList = statCacheList.stream().map(Stat::getId).collect(Collectors.toList());
                            if(!cacheIdList.contains(refreshEntity.getId())){
                                logger.info("discover new statistic item:{}, clear group:{} cache!",refreshEntity.getId(),refreshEntity.getGroupId());
                                groupStatListCache.invalidate(refreshEntity.getGroupId());
                            }else{
                                List<StatExtEntity> entityList = statCacheList.stream()
                                        .filter(x -> x.getId().intValue() == refreshEntity.getId().intValue()
                                                && x.getRefreshTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() < refreshEntity.getRefreshTime()
                                        ).collect(Collectors.toList());
                                if(CollectionUtils.isNotEmpty(entityList)){
                                    if(logger.isTraceEnabled()){
                                        logger.trace("clear group-stats local cache,groupId:{},token:{}",refreshEntity.getGroupId(),refreshEntity.getToken());
                                    }
                                    groupStatListCache.invalidate(refreshEntity.getGroupId());
                                }
                            }
                        }
                    }
                }
            }catch (Exception ex){
                logger.error("statistic group cache refresh error!",ex);
            }
        }
    }
}
