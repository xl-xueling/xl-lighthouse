package com.dtstep.lighthouse.insights.service;
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
import com.dtstep.lighthouse.common.entity.ServiceResult;
import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.modal.LimitDataObject;
import com.dtstep.lighthouse.common.modal.StatDataObject;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface DataService {

    List<String> dimensArrangement(StatExtEntity statExtEntity, LinkedHashMap<String, String[]> dimensParams) throws Exception;

    LinkedHashMap<String, String> dimensArrangementV2(StatExtEntity statExtEntity, LinkedHashMap<String,String[]> dimensParams) throws Exception;

    ServiceResult<List<StatDataObject>> dataQuery(StatExtEntity statExtEntity, LocalDateTime startTime, LocalDateTime endTime, List<String> dimens) throws Exception;

    ServiceResult<List<StatDataObject>> testDataQuery(StatExtEntity statExtEntity, LocalDateTime startTime, LocalDateTime endTime, List<String> dimens) throws Exception;

    List<LimitDataObject> limitQuery(StatExtEntity statExtEntity,List<Long> batchTimeList) throws Exception;

    List<LimitDataObject> testLimitQuery(StatExtEntity statExtEntity, List<Long> batchTimeList) throws Exception;
}
