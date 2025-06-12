package com.dtstep.lighthouse.insights.service.impl;
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
import com.dtstep.lighthouse.insights.dao.DomainDao;
import com.dtstep.lighthouse.common.enums.ResourceTypeEnum;
import com.dtstep.lighthouse.common.modal.Domain;
import com.dtstep.lighthouse.common.modal.ResourceDto;
import com.dtstep.lighthouse.insights.service.DomainService;
import com.dtstep.lighthouse.insights.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class DomainServiceImpl implements DomainService {

    @Autowired
    private DomainDao domainDao;

    @Autowired
    private ResourceService resourceService;

    @Override
    public int create(Domain domain) {
        domainDao.insert(domain);
        int id = domain.getId();
        resourceService.addResourceCallback(ResourceDto.newResource(ResourceTypeEnum.Domain,id,ResourceTypeEnum.System,0));
        return id;
    }

    @Override
    @Cacheable(value = "NormalPeriod",key = "#targetClass + '_' + 'queryById' + '_' + #id",cacheManager = "caffeineCacheManager",unless = "#result == null")
    public Domain queryById(Integer id) {
        return domainDao.queryById(id);
    }

    @Override
    @Cacheable(value = "NormalPeriod",key = "#targetClass + '_' + 'queryDefault'",cacheManager = "caffeineCacheManager",unless = "#result == null")
    public Domain queryDefault() {
        return domainDao.queryDefault();
    }
}
