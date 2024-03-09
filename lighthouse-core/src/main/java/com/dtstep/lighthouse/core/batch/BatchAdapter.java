package com.dtstep.lighthouse.core.batch;
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
import com.dtstep.lighthouse.core.sort.SortOperator;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.dtstep.lighthouse.common.constant.SysConst;
import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.hash.HashUtil;
import com.dtstep.lighthouse.common.sbr.StringBuilderHolder;
import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.common.util.Md5Util;
import com.dtstep.lighthouse.common.util.StringUtil;
import com.google.common.collect.Lists;
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

    private static final IntervalBatchHandler intervalBatchHandler = new IntervalBatchHandler();

    public static long getBatch(final int interval, final TimeUnit timeUnit, final long t){
        return intervalBatchHandler.getBatch(interval,timeUnit,t);
    }

    public static String generateBatchBaseKey(String token,int statId,int dataVersion,String dimens,long baseTime,int functionIndex) {
        String time = DateUtil.formatTimeStamp(baseTime, "yyyyMMddHHmmss");
        String origin = Md5Util.getMD5(token + "_" + statId + "_" + dataVersion + "_" + time + "_" + dimens + "_" + functionIndex);
        String prefix = getPrePartitionPrefix(origin,dimens);
        return prefix + origin;
    }

    private static String getPrePartitionPrefix(String origin, String dimens){
        int index;
        if(StringUtil.isEmpty(dimens)){
            index = Math.abs((int) (HashUtil.BKDRHash(origin) % SysConst._RESULT_STORAGE_PRE_PARTITIONS_SIZE));
        }else{
            index = Math.abs((int) (HashUtil.BKDRHash(origin + "_" + dimens) % SysConst._RESULT_STORAGE_PRE_PARTITIONS_SIZE));
        }
        return SysConst._DBKeyPrefixArray[index];
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

    static BatchInterface getBatchInterface(String timeParam){
        return intervalBatchHandler;
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
