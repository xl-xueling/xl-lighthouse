package com.dtstep.lighthouse.web.service.components;
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
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.dtstep.lighthouse.common.entity.components.ComponentsEntity;
import com.dtstep.lighthouse.common.entity.list.ListViewDataObject;
import com.dtstep.lighthouse.common.entity.user.UserEntity;

import java.util.List;


public interface ComponentsService {

    ListViewDataObject queryListByPage(UserEntity currentUser,int page, String search) throws Exception;

    void register(ComponentsEntity componentsEntity) throws Exception;

    void delete(int componentsId) throws Exception;

    void update(ComponentsEntity componentsEntity) throws Exception;

    ComponentsEntity queryById(int id) throws Exception;

    List<ComponentsEntity> queryComponentsList(int userId, int startIndex, int limitSize) throws Exception;

    ArrayNode querySystemComponentsList() throws Exception;

    void checkData(String data,int componentsType) throws Exception;

    int getLevel(String data) throws Exception;
}
