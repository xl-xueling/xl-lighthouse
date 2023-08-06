package com.dtstep.lighthouse.test.mode;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class OpsNodesLoadStateMonitorSample implements SimulationModalSample<HashMap<String,Object>> {

    @Override
    public HashMap<String, Object> generateSample() throws Exception {
        HashMap<String,Object> paramMap = new HashMap<>();
        paramMap.put("idc_id", "idc_" + ThreadLocalRandom.current().nextInt(1,100));
        paramMap.put("region_id","region_" + ThreadLocalRandom.current().nextInt(1,100));
        paramMap.put("cabinet_id","cabinet_"+ThreadLocalRandom.current().nextInt(1,100));
        paramMap.put("ip",getRandomIp());
        paramMap.put("load_average",ThreadLocalRandom.current().nextDouble(0.5,10));
        paramMap.put("load_state",ThreadLocalRandom.current().nextInt(1,4));
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
