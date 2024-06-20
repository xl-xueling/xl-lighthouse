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

import com.dtstep.lighthouse.common.constant.SysConst;
import com.dtstep.lighthouse.common.modal.SystemEnv;
import com.dtstep.lighthouse.common.random.RandomID;
import com.dtstep.lighthouse.insights.dao.SystemEnvDao;
import com.dtstep.lighthouse.insights.service.SystemEnvService;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SystemEnvServiceImpl implements SystemEnvService {

    @Autowired
    private SystemEnvDao systemEnvDao;

    @Override
    public void generateSignKeyIfNotExist() {
        boolean isExist = systemEnvDao.isParamExist(SysConst.PARAM_SIGN_KEY);
        if(!isExist){
            SystemEnv systemEnv = new SystemEnv();
            systemEnv.setParam(SysConst.PARAM_SIGN_KEY);
            systemEnv.setValue(RandomID.id(64));
            systemEnvDao.insert(systemEnv);
        }
    }

    @Transactional
    @Override
    public void createIfNotExist(String param, String value) {
        Validate.notNull(param);
        Validate.notNull(value);
        SystemEnv systemEnv = new SystemEnv();
        systemEnv.setParam(param);
        systemEnv.setValue(value);
        boolean isExist = systemEnvDao.isParamExist(param);
        if(!isExist){
            systemEnvDao.insert(systemEnv);
        }else{
            systemEnvDao.update(systemEnv);
        }
    }

    @Override
    public String getParam(String param) {
        return systemEnvDao.getParam(param);
    }

    @Override
    public void delete(String param) {
        systemEnvDao.delete(param);
    }
}
