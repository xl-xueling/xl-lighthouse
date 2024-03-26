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
import com.dtstep.lighthouse.common.enums.UserStateEnum;
import com.dtstep.lighthouse.common.modal.PermissionEnum;
import com.dtstep.lighthouse.common.entity.ListData;
import com.dtstep.lighthouse.insights.dto.UserQueryParam;
import com.dtstep.lighthouse.common.modal.User;
import com.dtstep.lighthouse.insights.vo.UserVO;

import java.util.List;
import java.util.Set;

public interface UserService {

    int create(User user, boolean needApprove) throws Exception;

    int update(User user);

    List<User> termQuery(String search);

    boolean isUserNameExist(String username);

    User cacheQueryById(int id);

    UserStateEnum queryUserState(Integer id);

    User queryById(int id);

    User queryAllInfoByUserName(String userName);

    String queryUserPassword(Integer id);

    int deleteById(int userId);

    ListData<UserVO> queryList(UserQueryParam queryParam, Integer pageNum, Integer pageSize);

    int count(UserQueryParam queryParam);

    Set<PermissionEnum> getUserPermissions(Integer id);
}
