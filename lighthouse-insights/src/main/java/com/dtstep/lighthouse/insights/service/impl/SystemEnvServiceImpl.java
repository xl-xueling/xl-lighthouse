package com.dtstep.lighthouse.insights.service.impl;

import com.dtstep.lighthouse.common.key.RandomID;
import com.dtstep.lighthouse.commonv2.constant.SystemConstant;
import com.dtstep.lighthouse.insights.dao.SystemEnvDao;
import com.dtstep.lighthouse.insights.modal.SystemEnv;
import com.dtstep.lighthouse.insights.service.SystemEnvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SystemEnvServiceImpl implements SystemEnvService {

    @Autowired
    private SystemEnvDao systemEnvDao;

    @Override
    public void createSignKeyIfNotExist() {
        boolean isExist = systemEnvDao.isParamExist(SystemConstant.PARAM_SIGN_KEY);
        if(!isExist){
            SystemEnv systemEnv = new SystemEnv();
            systemEnv.setParam(SystemConstant.PARAM_SIGN_KEY);
            systemEnv.setValue(RandomID.id(64));
            systemEnvDao.insert(systemEnv);
        }
    }

    @Override
    public String getParam(String param) {
        return systemEnvDao.getParam(param);
    }
}
