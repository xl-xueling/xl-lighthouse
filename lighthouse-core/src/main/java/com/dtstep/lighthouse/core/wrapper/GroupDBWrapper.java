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
import com.dtstep.lighthouse.common.util.*;
import com.dtstep.lighthouse.core.builtin.BuiltinLoader;
import com.dtstep.lighthouse.core.config.LDPConfig;
import com.dtstep.lighthouse.core.redis.RedisHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.dtstep.lighthouse.common.constant.StatConst;
import com.dtstep.lighthouse.common.entity.group.GroupEntity;
import com.dtstep.lighthouse.common.entity.group.GroupExtEntity;
import com.dtstep.lighthouse.common.entity.meta.MetaColumn;
import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.entity.stat.TimeParam;
import com.dtstep.lighthouse.common.enums.limiting.LimitingStrategyEnum;
import com.dtstep.lighthouse.common.enums.meta.ColumnTypeEnum;
import com.dtstep.lighthouse.common.enums.stat.GroupStateEnum;
import com.dtstep.lighthouse.core.dao.DaoHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
            GroupEntity groupEntity = DaoHelper.sql.getItem(GroupEntity.class, "select * from ldp_stat_group where token = ?", token);
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
            GroupEntity groupEntity = DaoHelper.sql.getItem(GroupEntity.class, "select * from ldp_stat_group where id = ?", groupId);
            if(groupEntity != null){
                groupExtEntity = combineExtInfo(groupEntity);
            }
            return Optional.ofNullable(groupExtEntity);
        }catch (Exception ex){
            logger.error("query group info error!",ex);
            return Optional.empty();
        }
    }

    private static GroupExtEntity combineExtInfo(GroupEntity groupEntity) throws Exception{
        GroupExtEntity groupExtEntity = new GroupExtEntity(groupEntity);
        assert StringUtil.isNotEmpty(groupEntity.getColumns());
        List<MetaColumn> columnList = JsonUtil.toJavaObjectList(groupExtEntity.getColumns(),MetaColumn.class);
        groupExtEntity.setColumnList(columnList);
        if(GroupExtEntity.isLimitedExpired(groupExtEntity)){
            DaoHelper.sql.execute("update ldp_stat_group set state = ?,update_time = ? where id = ?", GroupStateEnum.RUNNING.getState(),new Date(), groupExtEntity.getId());
            groupExtEntity.setState(GroupStateEnum.RUNNING.getState());
        }
        if(GroupExtEntity.isDebugModeExpired(groupExtEntity)){
            DaoHelper.sql.execute("update ldp_stat_group set debug_mode = ?,update_time = ? where id = ?",0,new Date(), groupExtEntity.getId());
            groupExtEntity.setDebugMode(0);
        }
        int groupId = groupExtEntity.getId();
        List<StatExtEntity> statExtEntityList = StatDBWrapper.queryRunningListByGroupId(groupId);
        if(CollectionUtils.isNotEmpty(statExtEntityList)){
            HashMap<String, ColumnTypeEnum> groupRelatedColumns = new HashMap<>();
            Map<String,ColumnTypeEnum> groupColumnsMap = columnList.stream().collect(Collectors.toMap(MetaColumn::getColumnName, MetaColumn::getColumnTypeEnum));
            long minDuration = 0L;
            long maxDataExpire = 0L;
            for (StatExtEntity statExtEntity : statExtEntityList) {
                long currentDuration = TimeParam.calculateDuration(statExtEntity.getTimeParamInterval(), statExtEntity.getTimeUnit());
                if(minDuration == 0L){
                    minDuration = currentDuration;
                }else{
                    minDuration = CalculateUtil.getMaxDivisor(minDuration,currentDuration);
                }
                if(maxDataExpire < statExtEntity.getDataExpire()){
                    maxDataExpire = statExtEntity.getDataExpire();
                }
                Set<String> statRelatedColumnSet = statExtEntity.getRelatedColumnSet();
                if (CollectionUtils.isNotEmpty(statRelatedColumnSet)) {
                    for(String columnName : statRelatedColumnSet){
                        groupRelatedColumns.put(columnName,groupColumnsMap.get(columnName));
                    }
                }
            }
            groupExtEntity.setRelatedColumns(groupRelatedColumns);
            TimeParam minTimeParam = transToTimeParam(minDuration);
            groupExtEntity.setMinTimeParam(minTimeParam);
            groupExtEntity.setDataExpire(maxDataExpire);
        }
        groupExtEntity.setVerifyKey(Md5Util.getMD5(groupExtEntity.getSecretKey()));
        String limitedThreshold = groupExtEntity.getLimitedThreshold();
        ObjectMapper objectMapper = new ObjectMapper();
        HashMap<String,Integer> limitedThresholdMap = groupExtEntity.getLimitedThresholdMap();
        if(!StringUtil.isEmpty(limitedThreshold)) {
            JsonNode jsonNode = objectMapper.readTree(limitedThreshold);
            Iterator<String> it = jsonNode.fieldNames();
            while (it.hasNext()){
                String key = it.next();
                limitedThresholdMap.put(key,jsonNode.get(key).asInt());
            }
        }
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
        }else{
            throw new Exception();
        }
        return timeParam;
    }

    public static int changeState(GroupExtEntity groupExtEntity, int state) throws Exception {
         GroupStateEnum groupStateEnum = GroupStateEnum.getStateEnum(state);
         groupExtEntity.setGroupStateEnum(groupStateEnum);
         int result;
         if(groupStateEnum == GroupStateEnum.LIMITING){
             result = DaoHelper.sql.execute("update ldp_stat_group set state = ?,update_time = ? where id = ? and state = ?",state,new Date(), groupExtEntity.getId(),GroupStateEnum.RUNNING.getState());
         }else{
             result = DaoHelper.sql.execute("update ldp_stat_group set state = ?,update_time = ? where id = ?",state,new Date(), groupExtEntity.getId());
         }
        try{
            int groupId = groupExtEntity.getId();
            String key = "GROUP::queryById_" + groupId;
            RedisHandler.getInstance().del(key);
            List<StatExtEntity> statExtEntities = StatDBWrapper.queryListByGroupId(groupId);
            if(CollectionUtils.isNotEmpty(statExtEntities)){
                for(StatExtEntity statExtEntity : statExtEntities){
                    String statKey = "STAT::queryById_" + statExtEntity.getId();
                    RedisHandler.getInstance().del(statKey);
                }
            }
        }catch (Exception ex){
            logger.error("clear group redis cache error!",ex);
        }
         return result;
    }

    public static GroupStateEnum getState(int groupId) throws Exception {
        GroupEntity groupEntity = DaoHelper.sql.getItem(GroupEntity.class,"select state,update_time from ldp_stat_group where id = ?",groupId);
        if(groupEntity == null){
            return null;
        }
        boolean isLimited = groupEntity.getState() == GroupStateEnum.LIMITING.getState()
                && (System.currentTimeMillis() - groupEntity.getUpdateTime().getTime() < TimeUnit.MINUTES.toMillis(StatConst.LIMITED_EXPIRE_MINUTES));
        if(isLimited){
            return GroupStateEnum.LIMITING;
        }else{
            return GroupStateEnum.getStateEnum(groupEntity.getState());
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
                List<Integer> ids = DaoHelper.sql.getList(Integer.class,"select id from ldp_stat_group where create_time != refresh_time and refresh_time > ?",new Date(time));
                if(CollectionUtils.isNotEmpty(ids)){
                    for(int groupId:ids){
                        GroupEntity groupEntity = GroupDBWrapper.queryById(groupId);
                        if(groupEntity != null){
                            clearLocalCache(groupId);
                            StatDBWrapper.clearLocalCacheByGroupId(groupId);
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

