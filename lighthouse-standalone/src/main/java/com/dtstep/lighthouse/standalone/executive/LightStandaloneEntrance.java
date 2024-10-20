package com.dtstep.lighthouse.standalone.executive;

import com.dtstep.lighthouse.common.exception.InitializationException;
import com.dtstep.lighthouse.core.config.LDPConfig;
import com.dtstep.lighthouse.core.http.LightHouseHttpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LightStandaloneEntrance {

    private static final Logger logger = LoggerFactory.getLogger(LightStandaloneEntrance.class);

    public static void main(String [] args) throws Exception {
        try{
            LDPConfig.loadConfiguration();
        }catch (Exception ex){
            logger.error("standalone service initialization error!",ex);
            throw new InitializationException();
        }
        new Thread(LightHouseHttpService::start).start();
        new Thread(LightStandaloneService::start).start();
    }
}
