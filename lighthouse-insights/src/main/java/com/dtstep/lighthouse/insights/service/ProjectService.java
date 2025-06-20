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
import com.dtstep.lighthouse.insights.dto.PermissionGrantParam;
import com.dtstep.lighthouse.insights.dto.PermissionReleaseParam;
import com.dtstep.lighthouse.insights.dto.ProjectQueryParam;
import com.dtstep.lighthouse.common.modal.Project;
import com.dtstep.lighthouse.insights.vo.ProjectVO;
import com.dtstep.lighthouse.common.entity.ServiceResult;

import java.util.List;

public interface ProjectService {

    ServiceResult<Integer> create(Project project);

    int update(Project project);

    ProjectVO queryById(Integer id);

    ServiceResult<Integer> deleteById(Integer id);

    ProjectVO cacheQueryById(Integer id);

    TreeNode getStructure(Project project) throws Exception;

    ListData<ProjectVO> queryList(ProjectQueryParam queryParam, Integer pageNum, Integer pageSize);

    List<ProjectVO> queryByIds(List<Integer> ids);

    int count(ProjectQueryParam queryParam);

    ResultCode batchGrantPermissions(PermissionGrantParam grantParam) throws Exception;

    ResultCode releasePermission(PermissionReleaseParam releaseParam) throws Exception;

    ResultCode star(Project project);

    ResultCode unStar(Project project);

    List<ProjectVO> queryStarList();
}
