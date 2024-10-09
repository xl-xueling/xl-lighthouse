package com.dtstep.lighthouse.ice.test.servant.rpc.http;

import com.dtstep.lighthouse.client.LightHouse;
import com.dtstep.lighthouse.core.config.LDPConfig;

public class CoreBaseTest {

    static {
        try{
            LDPConfig.initWithHomePath("/Users/xueling/lighthouse");
            LightHouse.init(LDPConfig.getVal(LDPConfig.KEY_LIGHTHOUSE_ICE_LOCATORS));
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }
}
