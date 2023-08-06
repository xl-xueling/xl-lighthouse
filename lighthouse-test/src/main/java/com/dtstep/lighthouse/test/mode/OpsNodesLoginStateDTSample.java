package com.dtstep.lighthouse.test.mode;

import com.dtstep.lighthouse.common.key.RandomID;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class OpsNodesLoginStateDTSample implements SimulationModalSample<HashMap<String,Object>>{

    @Override
    public HashMap<String, Object> generateSample() throws Exception {
        HashMap<String,Object> paramMap = new HashMap<>();
        paramMap.put("idc_id", "idc_" + ThreadLocalRandom.current().nextInt(1,100));
        paramMap.put("login_name", RandomID.id(5));
        paramMap.put("state",ThreadLocalRandom.current().nextInt(1,3));
        paramMap.put("user_ip",getRandomIp());
        return paramMap;
    }

    private String getRandomIp(){
        int a = ThreadLocalRandom.current().nextInt(1,255);
        int b = ThreadLocalRandom.current().nextInt(1,255);
        int c = ThreadLocalRandom.current().nextInt(1,255);
        int d = ThreadLocalRandom.current().nextInt(1,255);
        return a + "." + b + "." + c + "." + d;
    }
}
