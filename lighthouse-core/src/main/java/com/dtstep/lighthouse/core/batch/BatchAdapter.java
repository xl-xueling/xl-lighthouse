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

import com.dtstep.lighthouse.common.util.Md5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;

public final class BatchAdapter {

    private static final Logger logger = LoggerFactory.getLogger(BatchAdapter.class);

    private static final IntervalBatchHandler intervalBatchHandler = new IntervalBatchHandler();

    public static String generateLimitKey(String mapperStatId,int dataVersion,long batchTime){
        return Md5Util.getMD5(mapperStatId + "_" + dataVersion + "_" + batchTime);
    }

    public static List<Long> queryBatchTimeList(String timeParam,long startTime,long endTime) throws Exception {
        return queryBatchTimeList(timeParam,startTime,endTime,-1);
    }

    public static List<Long> queryBatchTimeList(String timeParam,long startTime,long endTime,int limitSize) throws Exception {
        return Objects.requireNonNull(getBatchInterface(timeParam)).queryBatchTimeList(timeParam,startTime,endTime,limitSize);
    }

    public static String dateTimeFormat(String timeParam, long batchTime){
        return Objects.requireNonNull(getBatchInterface(timeParam)).dateTimeFormat(timeParam,batchTime);
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
