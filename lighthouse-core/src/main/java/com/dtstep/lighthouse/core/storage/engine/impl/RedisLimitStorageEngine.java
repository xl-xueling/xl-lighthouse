package com.dtstep.lighthouse.core.storage.engine.impl;
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
import com.dtstep.lighthouse.core.lock.RedLock;
import com.dtstep.lighthouse.core.redis.RedisHandler;
import com.dtstep.lighthouse.core.storage.proxy.ResultStorageProxy;
import com.dtstep.lighthouse.core.wrapper.GroupDBWrapper;
import com.dtstep.lighthouse.core.wrapper.StatDBWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.dtstep.lighthouse.common.constant.RedisConst;
import com.dtstep.lighthouse.common.constant.StatConst;
import com.dtstep.lighthouse.common.entity.event.LimitBucket;
import com.dtstep.lighthouse.common.entity.group.GroupExtEntity;
import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.entity.stat.TemplateEntity;
import com.dtstep.lighthouse.common.entity.state.StatState;
import com.dtstep.lighthouse.common.entity.view.LimitValue;
import com.dtstep.lighthouse.common.entity.view.StatValue;
import com.dtstep.lighthouse.common.enums.function.FunctionEnum;
import com.dtstep.lighthouse.common.enums.stat.LimitTypeEnum;
import com.dtstep.lighthouse.common.hash.HashUtil;
import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.core.storage.engine.LimitStorageEngine;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Tuple;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RedisLimitStorageEngine extends LimitStorageEngine<LimitBucket, LimitValue> {

    private static final Logger logger = LoggerFactory.getLogger(RedisLimitStorageEngine.class);

    @Override
    public void limit(List<LimitBucket> limitBuckets) throws Exception {
        Map<String,List<LimitBucket>> limitMap = limitBuckets.stream().collect(Collectors.groupingBy(x -> (x.getStatId() + "_" + x.getBatchTime() + "_" + HashUtil.BKDRHash(x.getDimensValue()) % StatConst.LIMIT_SALT)));
        for(String ignored : limitMap.keySet()){
            List<LimitBucket> events = limitMap.get(ignored);
            int statId = events.get(0).getStatId();
            long batchTime = events.get(0).getBatchTime();
            int salt = Integer.parseInt(ignored.substring(ignored.lastIndexOf("_") + 1));
            Set<String> dimensSet = events.stream().map(LimitBucket::getDimensValue).collect(Collectors.toSet());
            StatExtEntity statExtEntity = StatDBWrapper.queryById(statId);
            if(statExtEntity == null){
                continue;
            }
            try{
                process(statExtEntity,dimensSet,batchTime,salt);
            }catch (Exception ex){
                logger.error("lighthouse limit process error!",ex);
            }
        }
    }

    private void process(StatExtEntity statExtEntity, Set<String> dimensSet, long batchTime, int salt) throws Exception {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        TemplateEntity templateEntity = statExtEntity.getTemplateEntity();
        LimitTypeEnum limitTypeEnum = templateEntity.getLimitTypeEnum();
        GroupExtEntity groupExtEntity = GroupDBWrapper.queryById(statExtEntity.getGroupId());
        if(CollectionUtils.isEmpty(dimensSet) || groupExtEntity == null){
            return;
        }
        String baseKey = BatchAdapter.generateBatchBaseKey(groupExtEntity.getToken(), statExtEntity.getId(), statExtEntity.getDataVersion(),null,batchTime,0);
        String lockKey = RedisConst.LOCK_LIMIT_PREFIX + "_" + baseKey + "_" + salt + "_" + DateUtil.formatTimeStamp(batchTime,"yyyyMMddHHmmss");
        String redisKey = RedisConst.LIMIT_N_PREFIX + "_" + baseKey + "_" + salt;
        boolean isLock = RedLock.tryLock(lockKey,5,3, TimeUnit.MINUTES);
        int expireSeconds = (int) statExtEntity.getTimeUnit().toSeconds(statExtEntity.getTimeParamInterval());
        int cacheSize = getLimitCacheSize(statExtEntity,templateEntity.getLimitSize());
        if(isLock){
            try{
                long t1 = System.currentTimeMillis();
                LinkedHashMap<String, StatValue> dbMap = ResultStorageProxy.queryWithDimensList(statExtEntity, Lists.newArrayList(dimensSet),batchTime);
                long t2 = System.currentTimeMillis();
                if(logger.isTraceEnabled()){
                    logger.trace("limit data query,stat:{},formula:{},dimens:{},dimens size:{},dimens value:{},cost:{}"
                            , statExtEntity.getId(),templateEntity.getStat(),templateEntity.getDimens(),dimensSet.size(), JsonUtil.toJSONString(dimensSet),(t2 - t1));
                }
                LinkedHashMap<String, Double> keyValueMap = Maps.newLinkedHashMap();
                if(limitTypeEnum == LimitTypeEnum.TOP){
                    long t3 = System.currentTimeMillis();
                    dbMap.entrySet().stream().sorted(Comparator.comparing(o -> 0 - Double.parseDouble(o.getValue().getValue().toString()))).limit(cacheSize)
                            .forEachOrdered(e -> keyValueMap.put(e.getKey(), Double.parseDouble(e.getValue().getValue().toString())));
                    long t4 = System.currentTimeMillis();
                    RedisHandler.getInstance().batchPutTopN(redisKey,keyValueMap,cacheSize,expireSeconds * 10);
                    long t5 = System.currentTimeMillis();
                    if(logger.isDebugEnabled()){
                        logger.debug("limit topN put,stat:{},batchTime:{},redis key:{},dimensSet size:{},keyValueMap size:{},cost1:{},cost2:{},cost3:{}"
                                ,statExtEntity.getId(), DateUtil.formatTimeStamp(batchTime,"yyyy-MM-dd HH:mm:ss"),redisKey, dimensSet.size(),keyValueMap.size(),(t2 - t1),(t4 - t3),(t5 - t4));
                    }
                }else if(limitTypeEnum == LimitTypeEnum.LAST){
                    long t3 = System.currentTimeMillis();
                    dbMap.entrySet().stream().sorted(Comparator.comparing(o -> Double.parseDouble(o.getValue().getValue().toString()))).limit(cacheSize)
                            .forEachOrdered(e -> keyValueMap.put(e.getKey(), Double.parseDouble(e.getValue().getValue().toString())));
                    long t4 = System.currentTimeMillis();
                    RedisHandler.getInstance().batchPutLastN(redisKey,keyValueMap,cacheSize,expireSeconds * 10);
                    long t5 = System.currentTimeMillis();
                    if(logger.isDebugEnabled()) {
                        logger.debug("limit topN put,stat:{},batchTime:{},redis key:{},dimensSet size:{},keyValueMap size:{},cost1:{},cost2:{},cost3:{}"
                                ,statExtEntity.getId(), DateUtil.formatTimeStamp(batchTime,"yyyy-MM-dd HH:mm:ss"),redisKey, dimensSet.size(),keyValueMap.size(),(t2 - t1),(t4 - t3),(t5 - t4));
                    }
                }
            }catch (Exception ex){
                logger.error("lighthouse stat,limit process error!",ex);
            }finally {
                RedLock.unLock(lockKey);
            }
        }else{
            logger.error("try lock failed,thread unable to acquire lock,this batch data may be lost,cost:{}ms!",stopWatch.getTime());
        }
        if(logger.isDebugEnabled()){
            logger.debug("limit process,stat:{},isLock:{},dimens size:{},cost:{}ms",statExtEntity.getId(),isLock,dimensSet.size(),stopWatch.getTime());
        }
    }

    private int getLimitCacheSize(StatExtEntity statExtEntity,int limitSize){
        List<StatState> stateList = statExtEntity.getTemplateEntity().getStatStateList();
        if(stateList.size() != 1){
            return StatConst.LIMIT_MAX_CACHE_SIZE;
        }
        FunctionEnum functionTypeEnum = stateList.get(0).getFunctionEnum();
        if(functionTypeEnum == FunctionEnum.AVG){
            return StatConst.LIMIT_MAX_CACHE_SIZE;
        }else{
            return limitSize;
        }
    }

    @Override
    public List<LimitValue> queryLimitDimens(StatExtEntity statExtEntity, long batchTime) throws Exception {
        List<LimitValue> resultList = new ArrayList<>();
        int groupId = statExtEntity.getGroupId();
        GroupExtEntity groupExtEntity = GroupDBWrapper.queryById(groupId);
        String baseKey = BatchAdapter.generateBatchBaseKey(groupExtEntity.getToken(), statExtEntity.getId(), statExtEntity.getDataVersion(),null,batchTime,0);
        Set<Tuple> limitSet = new HashSet<>();
        int limitSize = statExtEntity.getTemplateEntity().getLimitSize();
        if(statExtEntity.getTemplateEntity().getLimitTypeEnum() == LimitTypeEnum.TOP){
            IntStream.range(0,StatConst.LIMIT_SALT).forEach(z -> {
                String redisKey = RedisConst.LIMIT_N_PREFIX + "_" + baseKey + "_" + z;
                Set<Tuple> tempDimensSet = RedisHandler.getInstance().zrevrange(redisKey,0,limitSize);
                if(CollectionUtils.isNotEmpty(tempDimensSet)){
                    limitSet.addAll(tempDimensSet);
                }
            });
            limitSet.stream().sorted(Comparator.comparing(Tuple::getScore).reversed()).forEachOrdered(x -> {
                if(resultList.size() < limitSize){
                    LimitValue limitValue = new LimitValue(x.getElement(),x.getScore());
                    resultList.add(limitValue);
                }
            });
        }else if(statExtEntity.getTemplateEntity().getLimitTypeEnum() == LimitTypeEnum.LAST){
            IntStream.range(0,StatConst.LIMIT_SALT).forEach(z -> {
                String redisKey = RedisConst.LIMIT_N_PREFIX + "_" + baseKey + "_" + z;
                Set<Tuple> tempDimensSet = RedisHandler.getInstance().zrange(redisKey,0,limitSize);
                if(CollectionUtils.isNotEmpty(tempDimensSet)){
                    limitSet.addAll(tempDimensSet);
                }
            });
            limitSet.stream().sorted(Comparator.comparing(Tuple::getScore)).forEachOrdered(x -> {
                if(resultList.size() < limitSize){
                    LimitValue limitValue = new LimitValue(x.getElement(),x.getScore());
                    resultList.add(limitValue);
                }
            });
        }
        return resultList;
    }
}
