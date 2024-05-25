package com.dtstep.lighthouse.client.standalone;

import com.dtstep.lighthouse.client.LightHouse;
import com.dtstep.lighthouse.common.enums.RunningMode;

public class StandaloneBaseTest {

    static {
        try{
            LightHouse.init("127.0.0.1:4061");
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
