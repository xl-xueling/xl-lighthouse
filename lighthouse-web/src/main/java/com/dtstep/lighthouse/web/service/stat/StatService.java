package com.dtstep.lighthouse.web.service.stat;
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
import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.entity.user.UserEntity;
import com.dtstep.lighthouse.common.enums.display.DisplayTypeEnum;
import com.dtstep.lighthouse.common.enums.stat.StatStateEnum;

import java.util.List;
import java.util.Set;


public interface StatService {

    ListViewDataObject queryListByPage(UserEntity currentUser, int page, int departmentId, int projectId, String search) throws Exception;

    void deleteById(int statId) throws Exception;

    boolean isExist(int statId) throws Exception;

    void changeState(int statId, StatStateEnum stateEnum) throws Exception;

    List<StatExtEntity> queryListByGroupId(int groupId) throws Exception;

    StatExtEntity queryById(int statId) throws Exception;

    void changeDisplayType(int statId, DisplayTypeEnum displayTypeEnum) throws Exception;

    StatExtEntity queryMatchStat(UserEntity userEntity, StatExtEntity statExtEntity, Set<String> queryDimensSet) throws Exception;

    void update(StatExtEntity statExtEntity) throws Exception;

}
