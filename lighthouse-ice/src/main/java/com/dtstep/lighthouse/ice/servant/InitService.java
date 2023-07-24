package com.dtstep.lighthouse.ice.servant;

import com.dtstep.lighthouse.common.exception.InitializationException;
import com.dtstep.lighthouse.core.config.LDPConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

public class InitService {

    private static final Logger logger = LoggerFactory.getLogger(InitService.class);

    public static final AtomicBoolean isInit = new AtomicBoolean(false);

    public static synchronized void init() {
        if(isInit.get()){
            return;
        }
        try{
            LDPConfig.loadConfiguration();
        }catch (Exception ex){
            logger.error("ice server start error,system initialization error!",ex);
            throw new InitializationException();
        }
        isInit.set(true);
    }
}
