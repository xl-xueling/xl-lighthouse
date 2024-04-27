package com.dtstep.lighthouse.client.rpc;
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
import com.dtstep.lighthouse.common.entity.group.GroupVerifyEntity;
import com.dtstep.lighthouse.common.entity.stat.StatVerifyEntity;
import com.dtstep.lighthouse.common.entity.view.LimitValue;
import com.dtstep.lighthouse.common.entity.view.StatValue;

import java.util.List;
import java.util.Map;


public interface RPCClient {

    boolean init(String configuration) throws Exception;

    void reconnect() throws Exception;

    void send(String text) throws Exception;

    GroupVerifyEntity queryGroup(String token) throws Exception;

    StatVerifyEntity queryStat(int id) throws Exception;

    List<StatValue> dataQuery(int statId, String dimensValue, long startTime, long endTime) throws Exception;

    List<StatValue> dataQuery(int statId,String dimensValue,List<Long> batchList) throws Exception;

    Map<String,List<StatValue>> dataQueryWithDimensList(int statId, List<String> dimensValueList, long startTime, long endTime) throws Exception;

    Map<String,List<StatValue>> dataQueryWithDimensList(int statId,List<String> dimensValueList,List<Long> batchList) throws Exception;

    List<LimitValue> limitQuery(int statId, long batchTime) throws Exception;
}
