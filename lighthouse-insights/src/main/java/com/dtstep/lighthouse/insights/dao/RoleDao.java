package com.dtstep.lighthouse.insights.dao;
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
import com.dtstep.lighthouse.common.enums.RoleTypeEnum;
import com.dtstep.lighthouse.common.modal.Role;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleDao {

    int insert(Role role);

    int update(Role role);

    Role queryById(Integer id);

    int deleteById(Integer id);

    void batchInsert(List<Role> list);

    Role queryRole(RoleTypeEnum roleTypeEnum, Integer resourceId);

    boolean isRoleExist(RoleTypeEnum roleTypeEnum,Integer resourceId);

    boolean isChildRoleExist(Integer pid);

    List<Role> queryListByPid(Integer pid,Integer pageNum,Integer pageSize);
}
