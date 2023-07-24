package com.dtstep.lighthouse.web.service.department;
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
import com.dtstep.lighthouse.common.entity.department.DepartmentEntity;
import com.dtstep.lighthouse.common.entity.list.ListViewDataObject;
import com.dtstep.lighthouse.common.entity.tree.ZTreeViewNode;

import java.util.List;


public interface DepartmentService {

    ListViewDataObject queryListByPage(int page,int level,String search) throws Exception;

    DepartmentEntity queryById(int id) throws Exception;

    List<ZTreeViewNode> queryTreeInfo(int pid) throws Exception;

    int save(DepartmentEntity departmentEntity) throws Exception;

    void update(DepartmentEntity departmentEntity) throws Exception;

    boolean isExist(int departmentId) throws Exception;

    void delete(int id) throws Exception;

    int countChild(int pid) throws Exception;
}
