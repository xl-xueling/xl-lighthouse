package com.dtstep.lighthouse.test.mode;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class ITKVDBRequestMonitorSample implements SimulationModalSample<HashMap<String,Object>>{

    @Override
    public HashMap<String, Object> generateSample() throws Exception {
        HashMap<String,Object> paramMap = new HashMap<>();
        paramMap.put("clusterId","cluster_"+ ThreadLocalRandom.current().nextInt(3000));
        paramMap.put("in_bytes",ThreadLocalRandom.current().nextInt(10,300));
        paramMap.put("out_bytes",ThreadLocalRandom.current().nextInt(10,3000));
        paramMap.put("cmd",ThreadLocalRandom.current().nextInt(1,8));
        paramMap.put("key","prefix_" +ThreadLocalRandom.current().nextInt(1000)+"#" + ThreadLocalRandom.current().nextInt(1,10000));
        paramMap.put("cost",ThreadLocalRandom.current().nextInt(1,100));
        paramMap.put("ip",getRandomIp());
        paramMap.put("request_ip",getRandomIp());
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
