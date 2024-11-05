package com.dtstep.lighthouse.core.storage.result;
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
import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.entity.view.StatValue;
import com.dtstep.lighthouse.core.storage.common.IndicatorGet;
import com.dtstep.lighthouse.core.storage.common.LdpResult;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface ResultStorageHandler<W,R> {

    void increment(List<W> list) throws Exception;

    void put(List<W> list) throws Exception;

    void maxPut(List<W> list) throws Exception;

    void minPut(List<W> list) throws Exception;

    R query(StatExtEntity statExtEntity, String dimensValue, long batchTime) throws Exception;

    List<R> query(StatExtEntity statExtEntity, String dimensValue, List<Long> batchTimeList) throws Exception;

    LinkedHashMap<String,R> queryWithDimensList(StatExtEntity statExtEntity, List<String> dimensValueList, long batchTime) throws Exception;

    LinkedHashMap<String,List<R>> queryWithDimensList(StatExtEntity statExtEntity, List<String> dimensValueList, List<Long> batchTimeList) throws Exception;

    LinkedHashMap<String,List<R>> queryWithDimensList0(StatExtEntity statExtEntity, List<String> dimensValueList, List<Long> batchTimeList) throws Exception;

    HashMap<IndicatorGet, Object> query(List<IndicatorGet> indicators) throws Exception;
}
