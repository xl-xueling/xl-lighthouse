package com.dtstep.lighthouse.core.batch;
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
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.dtstep.lighthouse.common.constant.SysConst;
import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.hash.HashUtil;
import com.dtstep.lighthouse.common.sbr.StringBuilderHolder;
import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.common.util.Md5Util;
import com.dtstep.lighthouse.common.util.StringUtil;
import org.quartz.CronExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.TimeUnit;


public final class BatchAdapter {

    private static final Logger logger = LoggerFactory.getLogger(BatchAdapter.class);

    private final static Cache<String, String> intervalBatchKeyCache = Caffeine.newBuilder()
            .expireAfterWrite(2, TimeUnit.MINUTES)
            .maximumSize(5000)
            .softValues()
            .build();

    private static final CronBatchHandler cronBatchHandler = new CronBatchHandler();

    private static final IntervalBatchHandler intervalBatchHandler = new IntervalBatchHandler();

    @Deprecated
    public static long getBatch(final String timeParam,final long t){
        return getBatchInterface(timeParam).getBatch(timeParam,t);
    }

    public static long getBatch(final int interval, final TimeUnit timeUnit, final long t){
        return intervalBatchHandler.getBatch(interval,timeUnit,t);
    }

    public static String generateBatchKey(StatExtEntity statExtEntity, int functionIndex, String dimens, long batchTime) {
        String cacheKey;
        if(StringUtil.isEmpty(dimens)){
            cacheKey = statExtEntity.getId() + "_" + functionIndex + "_" + batchTime;
        }else{
            cacheKey = statExtEntity.getId() + "_" + functionIndex + "_" + dimens + "_" + batchTime;
        }
        String rowKey = intervalBatchKeyCache.get(cacheKey, k -> actualGenerateBatchKey(statExtEntity,functionIndex,dimens,batchTime));
        return Objects.requireNonNull(rowKey);
    }

    public static String actualGenerateBatchKey(StatExtEntity statExtEntity, int functionIndex, String dimens, long batchTime) {
        String key = null;
        try{
            long baseTime = 0L;
            String delta;
            long duration = statExtEntity.getTimeUnit().toMillis(statExtEntity.getTimeParamInterval());
            if(statExtEntity.getTimeUnit() == TimeUnit.MINUTES){
                baseTime = DateUtil.getHourStartTime(batchTime);
            }else if(statExtEntity.getTimeUnit() == TimeUnit.HOURS){
                baseTime = DateUtil.getDayStartTime(batchTime);
            }else if(statExtEntity.getTimeUnit() == TimeUnit.DAYS){
                baseTime = DateUtil.getMonthStartTime(batchTime);
            }else if(statExtEntity.getTimeUnit() == TimeUnit.SECONDS){
                baseTime = DateUtil.getHourStartTime(batchTime);
            }
            String baseKey = generateBatchBaseKey(statExtEntity.getToken(), statExtEntity.getId(), statExtEntity.getDataVersion(), dimens, baseTime,functionIndex);
            delta = Long.toHexString((batchTime - baseTime) / duration);
            key = StringBuilderHolder.Smaller.getStringBuilder().append(baseKey).append(";").append(delta).toString();
        }catch (Exception ex){
            logger.error("generate batch key error!",ex);
        }
        return key;
    }


    public static List<Long> queryBatchTimeList(String timeParam,long startTime,long endTime) throws Exception {
        return queryBatchTimeList(timeParam,startTime,endTime,-1);
    }

    public static List<Long> queryBatchTimeList(String timeParam,long startTime,long endTime,int limitSize) throws Exception {
        return Objects.requireNonNull(getBatchInterface(timeParam)).queryBatchTimeList(timeParam,startTime,endTime,limitSize);
    }

    public static String getDisplayFormat(String timeParam,Date startTime,Date endTime) throws Exception {
        return Objects.requireNonNull(getBatchInterface(timeParam)).getDisplayFormat(timeParam,startTime,endTime);
    }

    private static String getPrePartitionPrefix(String origin, String dimens){
         int index;
         if(StringUtil.isEmpty(dimens)){
            index = Math.abs((int) (HashUtil.BKDRHash(origin) % SysConst._DATA_STORAGE_PRE_PARTITIONS_SIZE));
         }else{
            index = Math.abs((int) (HashUtil.BKDRHash(origin + "_" + dimens) % SysConst._DATA_STORAGE_PRE_PARTITIONS_SIZE));
         }
        return SysConst._DBKeyPrefixArray[index];
    }

    public static String generateBatchBaseKey(String token,int statId,int dataVersion,String dimens,long baseTime,int functionIndex) {
        String time = DateUtil.formatTimeStamp(baseTime, "yyyyMMddHHmmss");
        String origin = Md5Util.getMD5(token + "_" + statId + "_" + dataVersion + "_" + time + "_" + dimens + "_" + functionIndex);
        String prefix = getPrePartitionPrefix(origin,dimens);
        return prefix + origin;
    }

    static BatchInterface getBatchInterface(String timeParam){
        if(isIntervalBatchParam(timeParam)){
            return intervalBatchHandler;
        }else{
            return cronBatchHandler;
        }
    }

    public static boolean isIntervalBatchParam(String timeParam){
        String [] array = timeParam.split("-");
        if(array.length != 2){
            return false;
        }
        return StringUtil.isNumber(array[0]) && ("second".equals(array[1])
                ||"minute".equals(array[1])
                || "hour".equals(array[1])
                || "day".equals(array[1]));
    }

    public static boolean isCronBatchParam(String timeParam){
        return CronExpression.isValidExpression(timeParam);
    }

    @Deprecated
    private static long binaryBatchSearch(long[] batchArray, long tempTime) throws Exception{
        int low = 0;
        int high = batchArray.length - 1;
        if(tempTime > batchArray[high] || tempTime < batchArray[low]){
            return -1;
        }
        int mid;
        while(low <= high){
            mid = (low + high) >> 1;
            if(batchArray[mid] > tempTime && batchArray[mid - 1] > tempTime){
                high = mid - 1;
            }else if(batchArray[mid] < tempTime && batchArray[mid + 1] <= tempTime){
                low = mid + 1;
            }else if(batchArray[mid] > tempTime && batchArray[mid - 1] <= tempTime){
                return batchArray[mid - 1];
            }else {
                return batchArray[mid];
            }
        }
        throw new Exception("binary batch search error");
    }
}
