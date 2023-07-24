package com.dtstep.lighthouse.ice.servant;

import IceBox.Service;
import com.dtstep.lighthouse.common.exception.InitializationException;
import com.dtstep.lighthouse.common.util.StringUtil;
import com.dtstep.lighthouse.core.config.LDPConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AbstractService implements Service {

    private static final Logger logger = LoggerFactory.getLogger(AbstractService.class);

    public void init(){
        try{
            initWithCommunicator();
        }catch (Exception ex){
            logger.error("Ice Node initialization error",ex);
        }
    }


    public static final AtomicBoolean isInit = new AtomicBoolean(false);

    public static synchronized void initWithCommunicator() {
        if(isInit.get()){
            return;
        }
        String ldpHome = System.getenv("LDP_HOME");
        if(StringUtil.isEmpty(ldpHome)){
            logger.error("lighthouse-ice start error,system environment variable[LDP_HOME] does not exist!");
            throw new InitializationException();
        }
        try{
            LDPConfig.initWithHomePath(ldpHome);
        }catch (Exception ex){
            logger.error("ice server start error,system initialization error!",ex);
            throw new InitializationException();
        }
        isInit.set(true);
    }
}

