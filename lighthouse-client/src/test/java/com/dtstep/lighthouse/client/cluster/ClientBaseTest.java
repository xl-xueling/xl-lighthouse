package com.dtstep.lighthouse.client.cluster;

import com.dtstep.lighthouse.client.LightHouse;

public class ClientBaseTest {

    static {
        try{
            LightHouse.init("10.206.6.12:4061");
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
