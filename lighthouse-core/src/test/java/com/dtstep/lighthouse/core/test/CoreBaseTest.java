package com.dtstep.lighthouse.core.test;

import com.dtstep.lighthouse.core.config.LDPConfig;

public class CoreBaseTest {

    static {
        try{
            LDPConfig.initWithHomePath("/Users/xueling/lighthouse");
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
