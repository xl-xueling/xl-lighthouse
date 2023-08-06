package com.dtstep.lighthouse.test.mode;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class RDShortVideoRequestStatSample implements SimulationModalSample<HashMap<String,Object>> {

    @Override
    public HashMap<String, Object> generateSample() throws Exception {
        HashMap<String,Object> paramMap = new HashMap<>();
        paramMap.put("interface","interface_"+ ThreadLocalRandom.current().nextInt(1,100));
        paramMap.put("ip",getRandomIp());
        paramMap.put("cost",ThreadLocalRandom.current().nextInt(1,500));
        paramMap.put("state",ThreadLocalRandom.current().nextInt(1,3));
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
