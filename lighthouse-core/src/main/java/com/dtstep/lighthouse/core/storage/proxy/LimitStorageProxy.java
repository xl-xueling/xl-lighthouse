package com.dtstep.lighthouse.core.storage.proxy;
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
import com.dtstep.lighthouse.core.storage.engine.impl.RedisLimitStorageEngine;
import com.dtstep.lighthouse.common.entity.event.LimitBucket;
import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.entity.view.LimitValue;
import com.dtstep.lighthouse.core.storage.engine.LimitStorageEngine;

import java.util.List;

public class LimitStorageProxy {

    private static final LimitStorageEngine<LimitBucket, LimitValue> limitStorageEngine = new RedisLimitStorageEngine();

    public static void limit(List<LimitBucket> eventList) throws Exception{
        limitStorageEngine.limit(eventList);
    }

    public static List<LimitValue> queryLimitDimens(StatExtEntity statExtEntity, long batchTime) throws Exception {
        return limitStorageEngine.queryLimitDimens(statExtEntity, batchTime);
    }

}
