package com.dtstep.lighthouse.web.service.group;
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
import com.dtstep.lighthouse.common.entity.group.GroupEntity;
import com.dtstep.lighthouse.common.entity.group.GroupExtEntity;
import com.dtstep.lighthouse.common.entity.stat.StatEntity;

import java.util.List;


public interface GroupService {

    int save(int userId, GroupEntity groupEntity, List<StatEntity> statList) throws Exception;

    void update(int userId, GroupEntity groupEntity, List<StatEntity> statList) throws Exception;

    boolean isExist(String token) throws Exception;

    GroupExtEntity queryById(int groupId) throws Exception;

    void delete(int groupId) throws Exception;
}
