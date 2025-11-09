package com.dtstep.lighthouse.core.storage.result;
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
import com.dtstep.lighthouse.common.entity.calculate.MicroBucket;
import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.entity.view.StatValue;
import com.dtstep.lighthouse.core.storage.common.IndicatorGet;
import com.dtstep.lighthouse.core.storage.result.impl.DefaultResultStorageHandler;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class ResultStorageSelector {

    private static final ResultStorageHandler<MicroBucket,StatValue> resultStorageHandler = new DefaultResultStorageHandler();

    public static void increment(List<MicroBucket> bucketList) throws Exception{
        resultStorageHandler.increment(bucketList);
    }

    public static void maxPut(List<MicroBucket> bucketList) throws Exception{
        resultStorageHandler.maxPut(bucketList);
    }

    public static void minPut(List<MicroBucket> bucketList) throws Exception{
        resultStorageHandler.minPut(bucketList);
    }

    public static void put(List<MicroBucket> bucketList) throws Exception {
        resultStorageHandler.put(bucketList);
    }

    public static StatValue query(StatExtEntity statExtEntity, String dimensValue, long batchTime) throws Exception{
        return resultStorageHandler.query(statExtEntity,dimensValue,batchTime);
    }

    public static List<StatValue> query(StatExtEntity statExtEntity, String dimensValue, List<Long> batchTimeList) throws Exception {
        return resultStorageHandler.query(statExtEntity, dimensValue, batchTimeList);
    }

    public static LinkedHashMap<String,StatValue> queryWithDimensList(StatExtEntity statExtEntity, List<String> dimensValueList, long batchTime) throws Exception {
        return resultStorageHandler.queryWithDimensList(statExtEntity, dimensValueList, batchTime);
    }

    public static LinkedHashMap<String,List<StatValue>> queryWithDimensList(StatExtEntity statExtEntity, List<String> dimensValueList, List<Long> batchTimeList) throws Exception {
        return resultStorageHandler.queryWithDimensList(statExtEntity, dimensValueList, batchTimeList);
    }

    @Deprecated
    public static LinkedHashMap<String,List<StatValue>> queryWithDimensList0(StatExtEntity statExtEntity, List<String> dimensValueList, List<Long> batchTimeList) throws Exception {
        return resultStorageHandler.queryWithDimensList0(statExtEntity, dimensValueList, batchTimeList);
    }

    public static HashMap<IndicatorGet, Object> query(List<IndicatorGet> indicatorGets) throws Exception {
        return resultStorageHandler.query(indicatorGets);
    }
}
