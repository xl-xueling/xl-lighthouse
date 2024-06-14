package com.dtstep.lighthouse.insights.service;
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
import com.dtstep.lighthouse.common.entity.ListData;
import com.dtstep.lighthouse.common.entity.ResultCode;
import com.dtstep.lighthouse.common.enums.StatStateEnum;
import com.dtstep.lighthouse.common.enums.SwitchStateEnum;
import com.dtstep.lighthouse.common.modal.*;
import com.dtstep.lighthouse.insights.vo.StatVO;
import com.dtstep.lighthouse.insights.dto.StatQueryParam;

import java.util.List;

public interface StatService {

    ResultCode create(Stat stat) throws Exception;

    ResultCode update(Stat stat);

    void changeState(Stat stat, StatStateEnum statStateEnum) throws Exception;

    int delete(Stat stat);

    StatVO queryById(Integer id) throws Exception;

    List<Stat> queryByProjectId(Integer projectId);

    ListData<StatVO> queryList(StatQueryParam queryParam, Integer pageNum, Integer pageSize);

    List<StatVO> queryByIds(List<Integer> ids) throws Exception;

    int count(StatQueryParam queryParam);

    RenderConfig getStatRenderConfig(StatVO stat) throws Exception;

    RenderConfig getTestStatRenderConfig(StatVO stat) throws Exception;

    ResultCode filterConfig(StatVO stat, List<RenderFilterConfig> filterConfigs);

    ResultCode chartsConfig(StatVO stat,List<RenderChartConfig> chartConfigs);

    List<DBStatistics> getTopDepartmentStatSize();
}
