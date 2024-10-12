package com.dtstep.lighthouse.insights.service.impl;
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
import com.dtstep.lighthouse.common.entity.Owner;
import com.dtstep.lighthouse.common.enums.OwnerTypeEnum;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.insights.config.CallerKeyAuthenticationToken;
import com.dtstep.lighthouse.insights.config.SeedAuthenticationToken;
import com.dtstep.lighthouse.insights.service.BaseService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class BaseServiceImpl implements BaseService {

    @Override
    public Integer getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || authentication.getClass() != SeedAuthenticationToken.class){
            return null;
        }else{
            return (Integer) authentication.getPrincipal();
        }
    }

    @Override
    public Owner getCurrentOwner() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null){
            return null;
        }else if(authentication.getClass() == SeedAuthenticationToken.class){
            Owner owner = new Owner();
            owner.setOwnerId((Integer) authentication.getPrincipal());
            owner.setOwnerType(OwnerTypeEnum.USER);
            return owner;
        }else if(authentication.getClass() == CallerKeyAuthenticationToken.class){
            Owner owner = new Owner();
            owner.setOwnerId((Integer) authentication.getPrincipal());
            owner.setOwnerType(OwnerTypeEnum.CALLER);
            return owner;
        }
        return null;
    }
}
