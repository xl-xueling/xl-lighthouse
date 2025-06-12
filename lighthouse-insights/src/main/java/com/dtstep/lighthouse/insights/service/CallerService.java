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
import com.dtstep.lighthouse.common.entity.ListData;
import com.dtstep.lighthouse.common.entity.ResultCode;
import com.dtstep.lighthouse.common.entity.ServiceResult;
import com.dtstep.lighthouse.common.modal.Caller;
import com.dtstep.lighthouse.common.modal.CallerQueryParam;
import com.dtstep.lighthouse.insights.dto.PermissionGrantParam;
import com.dtstep.lighthouse.insights.dto.PermissionReleaseParam;
import com.dtstep.lighthouse.insights.vo.CallerVO;

import java.util.Map;

public interface CallerService {

    ServiceResult<Integer> create(Caller caller) throws Exception;

    int update(Caller caller) throws Exception;

    CallerVO queryById(Integer id) throws Exception;

    void deleteById(Integer id) throws Exception;

    String getSecretKey(Integer id);

    int count(CallerQueryParam queryParam) throws Exception;

    ListData<CallerVO> queryList(CallerQueryParam queryParam, Integer pageNum, Integer pageSize) throws Exception;

    ResultCode batchGrantPermissions(PermissionGrantParam grantParam) throws Exception;

    ResultCode releasePermission(PermissionReleaseParam releaseParam) throws Exception;
}
