package com.dtstep.lighthouse.insights.dao;

import com.dtstep.lighthouse.insights.modal.SystemEnv;

public interface SystemEnvDao {

    boolean isParamExist(String param);

    int insert(SystemEnv systemEnv);
}
