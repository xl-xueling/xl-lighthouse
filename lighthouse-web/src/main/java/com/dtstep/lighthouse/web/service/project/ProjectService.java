package com.dtstep.lighthouse.web.service.project;
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
import com.dtstep.lighthouse.common.entity.list.ListViewDataObject;
import com.dtstep.lighthouse.common.entity.project.ProjectEntity;
import com.dtstep.lighthouse.common.entity.tree.ZTreeViewNode;
import com.dtstep.lighthouse.common.entity.user.UserEntity;
import java.util.List;
import java.util.Set;


public interface ProjectService {

    ListViewDataObject queryListByPage(UserEntity currentUser,int page,boolean isOwner, int departmentId, String search) throws Exception;

    ProjectEntity queryById(int projectId) throws Exception;

    int save(ProjectEntity projectEntity, Set<Integer> adminList) throws Exception;

    void update(ProjectEntity projectEntity,Set<Integer> adminList) throws Exception;

    boolean isExist(String projectName) throws Exception;

    boolean isExist(int projectId) throws Exception;

    void delete(int id) throws Exception;

    List<ZTreeViewNode> queryZTreeInfo(int projectId) throws Exception;
}
