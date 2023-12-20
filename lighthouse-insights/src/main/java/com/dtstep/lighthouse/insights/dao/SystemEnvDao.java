package com.dtstep.lighthouse.insights.dao;

import com.dtstep.lighthouse.insights.modal.SystemEnv;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemEnvDao {

    int insert(SystemEnv systemEnv);

    boolean isParamExist(String param);

    String getParam(String param);
}
