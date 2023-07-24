package com.dtstep.lighthouse.core.limited.strategy;
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
import com.dtstep.lighthouse.core.limited.device.CountingDevice;
import com.dtstep.lighthouse.core.limited.device.impl.HBaseLimitedCountingDevice;
import com.google.common.collect.Lists;
import com.dtstep.lighthouse.common.enums.limiting.LimitingStrategyEnum;

import java.util.List;

public abstract class AbstractStrategy<T> implements Strategy<T> {

    public static List<CountingDevice> getDeviceList(LimitingStrategyEnum limitingStrategyEnum) {
        if(limitingStrategyEnum == LimitingStrategyEnum.GROUP_MESSAGE_SIZE_LIMIT){
            return Lists.newArrayList(new HBaseLimitedCountingDevice());
        }else if(limitingStrategyEnum == LimitingStrategyEnum.STAT_RESULT_SIZE_LIMIT){
            return Lists.newArrayList(new HBaseLimitedCountingDevice());
        }
        return null;
    }
}
