package com.dtstep.lighthouse.core.limited.strategy.impl;
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
import com.dtstep.lighthouse.core.builtin.BuiltinLoader;
import com.dtstep.lighthouse.core.limited.device.CountingDevice;
import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.enums.limiting.LimitingStrategyEnum;
import com.dtstep.lighthouse.core.limited.strategy.AbstractStrategy;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

public class StatResultStrategyService extends AbstractStrategy<StatExtEntity> {

    private static final List<CountingDevice> list = getDeviceList(LimitingStrategyEnum.STAT_RESULT_SIZE_LIMIT);

    private static final StatExtEntity builtInStat = BuiltinLoader.getBuiltinStat(1021);

    @Override
    public boolean process(StatExtEntity statExtEntity, int permitsPerSecond, int step) throws Exception {
        CountingDevice.Params params = new CountingDevice.Params(LimitingStrategyEnum.STAT_RESULT_SIZE_LIMIT, builtInStat, statExtEntity.getId(),permitsPerSecond,step);
        if(CollectionUtils.isEmpty(list)){
            return true;
        }
        for(CountingDevice recorder : list){
            if(!recorder.tryRequire(params)){
                return false;
            }
        }
        return true;
    }
}
