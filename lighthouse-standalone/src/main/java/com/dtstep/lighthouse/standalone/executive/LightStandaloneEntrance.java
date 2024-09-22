package com.dtstep.lighthouse.standalone.executive;

import com.dtstep.lighthouse.client.LightHouse;
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

        new Thread(() -> {
            try{
                new LightHouseHttpService().start();
                logger.info("standalone service http listening has been started!");
            }catch (Exception ex){
                logger.error("standalone service http listening start error!",ex);
                throw new InitializationException();
            }
        }).start();

        new Thread(() -> {
            try{
                new LightStandaloneService().start();
                logger.info("standalone service has been started!");
            }catch (Exception ex){
                logger.error("standalone service start error!",ex);
                throw new InitializationException();
            }
        }).start();

        try{
            LightHouse.init(LDPConfig.getVal(LDPConfig.KEY_LIGHTHOUSE_ICE_LOCATORS));
        }catch (Exception ex){
            logger.error("standalone service initialization error!",ex);
            throw new InitializationException();
        }
    }
}
