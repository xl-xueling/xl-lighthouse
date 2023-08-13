package com.dtstep.lighthouse.core.test;

import com.dtstep.lighthouse.common.entity.user.UserEntity;
import com.dtstep.lighthouse.core.config.LDPConfig;
import com.dtstep.lighthouse.core.dao.DaoHelper;

import java.util.Date;

public class DBTest {

    public static void main(String[] args) throws Exception{
        LDPConfig.loadConfiguration();
        for(int i=0;i<100;i++){
            UserEntity userEntity = new UserEntity();
            userEntity.setUserName("admin"+i);
            userEntity.setPassword("e10adc3949ba59abbe56e057f20f883e");
            Date date = new Date();
            userEntity.setCreateTime(date);
            userEntity.setLastTime(date);
            userEntity.setState(1);
            userEntity.setDepartmentId(-1);
            DaoHelper.sql.insert(userEntity);
        }
    }
}
