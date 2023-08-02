package com.dtstep.lighthouse.core.wrapper;
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
import com.dtstep.lighthouse.core.batch.BatchAdapter;
import com.dtstep.lighthouse.core.builtin.BuiltinLoader;
import com.dtstep.lighthouse.core.redis.RedisHandler;
import com.dtstep.lighthouse.core.template.TemplateContext;
import com.dtstep.lighthouse.core.template.TemplateParser;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.dtstep.lighthouse.common.entity.meta.MetaColumn;
import com.dtstep.lighthouse.common.entity.stat.StatEntity;
import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.entity.stat.TemplateEntity;
import com.dtstep.lighthouse.common.entity.state.StatState;
import com.dtstep.lighthouse.common.entity.state.StatUnit;
import com.dtstep.lighthouse.common.enums.stat.GroupStateEnum;
import com.dtstep.lighthouse.common.enums.stat.StatStateEnum;
import com.dtstep.lighthouse.common.exception.TemplateParseException;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.common.util.StringUtil;
import com.dtstep.lighthouse.core.dao.DaoHelper;
import com.dtstep.lighthouse.core.formula.FormulaTranslate;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public final class StatDBWrapper {

    private static final Logger logger = LoggerFactory.getLogger(StatDBWrapper.class);

    private final static LoadingCache<Integer, Optional<List<StatExtEntity>>> groupStatListCache = Caffeine.newBuilder()
            .expireAfterWrite(3, TimeUnit.MINUTES)
            .maximumSize(100000)
            .softValues()
            .build(StatDBWrapper::actualQueryListByGroupId);

    private static final LoadingCache<Integer, Optional<StatExtEntity>> statCache = Caffeine.newBuilder()
            .expireAfterWrite(3, TimeUnit.MINUTES)
            .softValues()
            .maximumSize(100000)
            .build(StatDBWrapper::actualQueryById);

    public static StatExtEntity queryById(int statId) {
        return Objects.requireNonNull(statCache.get(statId)).orElse(null);
    }

    public static List<StatExtEntity> queryListByGroupId(int groupId) {
        return Objects.requireNonNull(groupStatListCache.get(groupId)).orElse(null);
    }

    public static List<StatExtEntity> queryRunningListByGroupId(int groupId){
        List<StatExtEntity> entityList = queryListByGroupId(groupId);
        if(CollectionUtils.isEmpty(entityList)){
            return null;
        }
        return entityList.stream().filter(x -> x.getState() == StatStateEnum.RUNNING.getState()).collect(Collectors.toList());
    }

    public static Optional<StatExtEntity> actualQueryById(int statId) {
        if(BuiltinLoader.isBuiltinStat(statId)){
            return Optional.ofNullable(BuiltinLoader.getBuiltinStat(statId));
        }
        StatExtEntity statExtEntity = null;
        try{
            StatEntity statEntity = queryStatByIdFromDB(statId);
            if(statEntity != null){
                statExtEntity = combineExtInfo(statEntity,false);
            }
            return Optional.ofNullable(statExtEntity);
        }catch (Exception ex){
            logger.error("query stat info error,id:{}",statId,ex);
            return Optional.empty();
        }
    }

    public static StatEntity queryStatByIdFromDB(int statId) throws Exception {
        return DaoHelper.sql.getItem(StatEntity.class, "SELECT a.*,b.columns as group_columns,b.token as token,b.state as group_state FROM ldp_stat_item a left join ldp_stat_group b on a.group_id = b.id where a.id = ?", statId);
    }

    public static Optional<List<StatExtEntity>> actualQueryListByGroupId(int groupId) throws Exception {
        if(BuiltinLoader.isBuiltinGroup(groupId)){
            return Optional.ofNullable(BuiltinLoader.getBuiltinStatByGroupId(groupId));
        }
        List<StatEntity> entityList = queryStatByGroupIDFromDB(groupId);
        if(CollectionUtils.isEmpty(entityList)){
            return Optional.empty();
        }
        List<StatExtEntity> extEntityList = new ArrayList<>();
        entityList.forEach(z -> {
            try{
                StatExtEntity statExtEntity = combineExtInfo(z,false);
                extEntityList.add(statExtEntity);
            }catch (Exception ex){
                logger.error("query stat info error,id:{}",z.getId(),ex);
            }
        });
        return Optional.of(extEntityList);
    }

    public static List<StatEntity> queryStatByGroupIDFromDB(int groupId) throws Exception {
        return DaoHelper.sql.getList(StatEntity.class, "SELECT a.*,b.columns as group_columns,b.token as token,b.state as group_state FROM ldp_stat_item a left join ldp_stat_group b on a.group_id = b.id where  a.group_id = ?", groupId);
    }

    public static StatExtEntity combineExtInfo(StatEntity statEntity,boolean isBuiltIn) throws Exception {
        String groupColumns = statEntity.getGroupColumns();
        assert StringUtil.isNotEmpty(groupColumns);
        List<MetaColumn> groupColumnList = JsonUtil.toJavaObjectList(groupColumns,MetaColumn.class);
        StatExtEntity statExtEntity = new StatExtEntity(statEntity);
        String timeParam = statExtEntity.getTimeParam();
        boolean isInterval = BatchAdapter.isIntervalBatchParam(timeParam);
        if(isInterval){
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
        }else{
            throw new TemplateParseException();
        }
        StatStateEnum stateEnum = StatStateEnum.getByState(statExtEntity.getState());
        statExtEntity.setStatStateEnum(stateEnum);
        if(!isBuiltIn){
            GroupStateEnum groupStateEnum = GroupDBWrapper.getState(statEntity.getGroupId());
            if(groupStateEnum == GroupStateEnum.LIMITING && stateEnum == StatStateEnum.RUNNING){
                statExtEntity.setStatStateEnum(StatStateEnum.LIMITING);
            }else if(StatExtEntity.isLimitedExpired(statExtEntity)){
                DaoHelper.sql.execute("update ldp_stat_item set state = ?,update_time = ? where id = ?", StatStateEnum.RUNNING.getState(),new Date(), statExtEntity.getId());
                statExtEntity.setStatStateEnum(StatStateEnum.RUNNING);
            }
        }
        String template = statExtEntity.getTemplate();
        TemplateEntity templateEntity = TemplateParser.parse(new TemplateContext(statEntity.getId(),template,timeParam,groupColumnList));
        statExtEntity.setTemplateEntity(templateEntity);
        List<MetaColumn> relatedColumns = new ArrayList<>();
        String stat = templateEntity.getStat();
        List<MetaColumn> statRelatedColumns = FormulaTranslate.queryRelatedColumns(groupColumnList,stat);
        if(CollectionUtils.isNotEmpty(statRelatedColumns)){
            relatedColumns.addAll(statRelatedColumns);
        }
        for(StatState statState : templateEntity.getStatStateList()){
            List<MetaColumn> stateRelatedColumns = new ArrayList<>();
            List<StatUnit> unitList = statState.getUnitList();
            if(CollectionUtils.isNotEmpty(unitList)){
                int index = StatState.isBitCountState(statState) ? 1 : 0;
                for(int i = index;i<unitList.size(); i++){
                    StatUnit statUnit = unitList.get(i);
                    List<MetaColumn> unitRelatedColumns = FormulaTranslate.queryRelatedColumns(groupColumnList,statUnit.getOrigin());
                    if(CollectionUtils.isNotEmpty(unitRelatedColumns)){
                        stateRelatedColumns.addAll(unitRelatedColumns);
                    }
                }
            }
            statState.setBuiltIn(isBuiltIn);
            statState.setStatId(statExtEntity.getId());
            statState.setGroupId(statExtEntity.getGroupId());
            statState.setRelatedColumnSet(stateRelatedColumns.stream().map(MetaColumn::getColumnName).collect(Collectors.toSet()));
        }
        String dimens = templateEntity.getDimens();
        if(!StringUtil.isEmpty(dimens)){
            List<MetaColumn> dimensRelatedColumns = FormulaTranslate.queryRelatedColumns(groupColumnList,dimens);
            if(!dimensRelatedColumns.isEmpty()){
                relatedColumns.addAll(dimensRelatedColumns);
            }
        }
        statExtEntity.setSequence(statEntity.getSequenceFlag() == 1);
        statExtEntity.setRelatedColumnSet(relatedColumns.stream().map(MetaColumn::getColumnName).collect(Collectors.toSet()));
        statExtEntity.setTemplateOfHtml(StringEscapeUtils.escapeHtml4(statEntity.getTemplate()));
        return statExtEntity;
    }

    public static void clearLocalCache(int statId){
        StatEntity statEntity = queryById(statId);
        statCache.invalidate(statId);
        groupStatListCache.invalidate(statEntity.getGroupId());
    }

    public static void clearLocalCacheByGroupId(int groupId){
        groupStatListCache.invalidate(groupId);
    }

    public static int changeState(StatExtEntity statExtEntity, StatStateEnum stateEnum) throws Exception{
        Validate.notNull(stateEnum);
        Validate.notNull(statExtEntity);
        int statId = statExtEntity.getId();
        statExtEntity.setStatStateEnum(stateEnum);
        int result;
        if(stateEnum == StatStateEnum.LIMITING){
            result = DaoHelper.sql.execute("update ldp_stat_item set state = ?,update_time = ? where id = ? and state = ?",stateEnum.getState(),new Date(), statId,StatStateEnum.RUNNING.getState());
        }else{
            result = DaoHelper.sql.execute("update ldp_stat_item set state = ?,update_time = ? where id = ?",stateEnum.getState(),new Date(), statId);
        }
        try{
            String key = "STAT::queryById_" + statId;
            RedisHandler.getInstance().del(key);
        }catch (Exception ex){
            logger.error("clear stat redis cache error!",ex);
        }
        return result;
    }

}
