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
import com.dtstep.lighthouse.common.modal.TreeNode;
import com.dtstep.lighthouse.common.entity.ListData;
import com.dtstep.lighthouse.common.entity.ResultCode;
import com.dtstep.lighthouse.insights.dto.*;
import com.dtstep.lighthouse.common.modal.Indicator;
import com.dtstep.lighthouse.common.modal.MetricSet;
import com.dtstep.lighthouse.insights.vo.MetricSetVO;

import java.util.List;

public interface MetricSetService {

    int create(MetricSet metricSet);

    int delete(MetricSet metricSet);

    ResultCode star(MetricSet metricSet);

    ResultCode unStar(MetricSet metricSet);

    int binded(MetricBindParam bindParam) throws Exception;

    int bindRemove(MetricBindRemoveParam removeParam);

    ResultCode batchGrantPermissions(PermissionGrantParam grantParam) throws Exception;

    ResultCode releasePermission(PermissionReleaseParam releaseParam) throws Exception;

    int update(MetricSet metricSet);

    MetricSetVO queryById(Integer id);

    ListData<MetricSetVO> queryList(MetricSetQueryParam queryParam, Integer pageNum, Integer pageSize);

    int count(MetricSetQueryParam queryParam);

    List<MetricSetVO> queryStarList();

    TreeNode getStructure(MetricSetVO metricSet) throws Exception;

    ResultCode updateStructure(MetricUpdateStructureParam updateStructureParam);

    ListData<Indicator> queryIndicatorList(MetricPendQueryParam queryParam, Integer pageNum, Integer pageSize);
}
