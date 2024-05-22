package com.dtstep.lighthouse.client.standalone;

import com.dtstep.lighthouse.client.LightHouse;
import com.dtstep.lighthouse.common.enums.RunningMode;

public class StandaloneBaseTest {

    static {
        try{
            LightHouse.init("10.206.6.36:4061", RunningMode.STANDALONE);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
