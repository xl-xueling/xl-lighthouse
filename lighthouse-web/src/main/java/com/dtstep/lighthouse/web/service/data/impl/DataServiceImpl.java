package com.dtstep.lighthouse.web.service.data.impl;
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
import com.dtstep.lighthouse.common.constant.StatConst;
import com.dtstep.lighthouse.common.entity.stat.FilterParam;
import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.entity.view.StatValue;
import com.dtstep.lighthouse.common.util.StringUtil;
import com.dtstep.lighthouse.core.storage.proxy.ResultStorageProxy;
import com.dtstep.lighthouse.core.wrapper.DimensDBWrapper;
import com.dtstep.lighthouse.web.service.data.DataService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DataServiceImpl implements DataService {


    @Override
    public List<StatValue> queryWithDimens(StatExtEntity statExtEntity, String dimensValue, List<Long> batchTimeList) throws Exception {
        return ResultStorageProxy.queryWithDimens(statExtEntity, dimensValue,batchTimeList);
    }

    @Override
    public LinkedHashMap<String,StatValue> queryWithDimensList(StatExtEntity statExtEntity, List<String> dimensList, long batchTime) throws Exception{
        return ResultStorageProxy.queryWithDimensList(statExtEntity, dimensList,batchTime);
    }

    @Override
    public List<StatValue> queryWithDimensList(StatExtEntity statExtEntity, List<String> dimensList, List<Long> batchTimeList) throws Exception {
        return ResultStorageProxy.queryWithDimensList(statExtEntity,dimensList,batchTimeList);
    }

    @Override
    public Set<String> transformFilterSet(List<FilterParam> filterParams) {
        Iterator<String> iterator = filterParams.stream().map(FilterParam::getFilterKey).iterator();
        Set<String> filterSet = new HashSet<>();
        while (iterator.hasNext()){
            String filter = iterator.next();
            if(filter.contains(StatConst.DIMENS_SEPARATOR)){
                String[] cascadeArray = filter.split(StatConst.DIMENS_SEPARATOR);
                for (String cascadeData : cascadeArray) {
                    if (!StringUtil.isEmpty(cascadeData)) {
                        filterSet.add(cascadeData);
                    }
                }
            }else{
                filterSet.add(filter);
            }
        }
        return filterSet;
    }

    @Override
    public HashMap<String, String> initDimensMapperMap(StatExtEntity statExtEntity, List<FilterParam> filterParams) {
        return DimensDBWrapper.columnCombination(filterParams, statExtEntity);
    }
}
