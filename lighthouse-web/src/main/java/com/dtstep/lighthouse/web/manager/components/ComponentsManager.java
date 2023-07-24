package com.dtstep.lighthouse.web.manager.components;
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
import com.dtstep.lighthouse.common.entity.components.ComponentsEntity;
import com.dtstep.lighthouse.core.dao.DaoHelper;
import com.dtstep.lighthouse.web.dao.ComponentsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class ComponentsManager {

    @Autowired
    private ComponentsDao componentsDao;

    @Cacheable(value = "normal",key = "#targetClass + '_' + 'queryById' + '_' + #id",cacheManager = "caffeineCacheManager",unless = "#result == null")
    public ComponentsEntity queryById(int id) throws Exception {
        return componentsDao.queryById(id);
    }

    public int count(int userId) throws Exception {
        return DaoHelper.sql.count("select count(1) from ldp_components where (private_flag = 0 or user_id = ?)" ,userId);
    }
}
