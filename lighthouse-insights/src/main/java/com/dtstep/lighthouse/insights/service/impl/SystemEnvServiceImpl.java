package com.dtstep.lighthouse.insights.service.impl;

import com.dtstep.lighthouse.common.key.RandomID;
import com.dtstep.lighthouse.insights.dao.SystemEnvDao;
import com.dtstep.lighthouse.insights.modal.SystemEnv;
import com.dtstep.lighthouse.insights.service.SystemEnvService;
import org.springframework.beans.factory.annotation.Autowired;

public class SystemEnvServiceImpl implements SystemEnvService {

    private static final String PARAM_SIGN_KEY = "secret_key";

    @Autowired
    private SystemEnvDao systemEnvDao;

    @Override
    public void createSignKeyIfNotExist() {
        boolean isExist = systemEnvDao.isParamExist(PARAM_SIGN_KEY);
        if(!isExist){
            SystemEnv systemEnv = new SystemEnv();
            systemEnv.setParam(PARAM_SIGN_KEY);
            systemEnv.setValue(RandomID.id(64));
            systemEnvDao.insert(systemEnv);
        }
    }
}
