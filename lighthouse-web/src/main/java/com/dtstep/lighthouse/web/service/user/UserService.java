package com.dtstep.lighthouse.web.service.user;
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
import com.dtstep.lighthouse.common.entity.user.UserEntity;
import com.dtstep.lighthouse.common.enums.user.UserStateEnum;

import java.util.List;


public interface UserService {

    ListViewDataObject queryListByPage(int page,int state, int departmentId, String search) throws Exception;

    UserEntity queryById(int id) throws Exception;

    int countUserName(String userName) throws Exception;

    int save(UserEntity userEntity) throws Exception;

    int countByState(UserStateEnum stateEnum) throws Exception;

    void deleteById(int userId) throws Exception;

    void changeState(int userId, UserStateEnum userStateEnum) throws Exception;

    void update(UserEntity userEntity) throws Exception;

    void changePassword(int userId, String password) throws Exception;

    List<UserEntity> termQuery(String term,int pageSize) throws Exception;
}
