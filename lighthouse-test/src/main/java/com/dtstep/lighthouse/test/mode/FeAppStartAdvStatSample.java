package com.dtstep.lighthouse.test.mode;

import com.dtstep.lighthouse.common.key.RandomID;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class FeAppStartAdvStatSample implements SimulationModalSample<HashMap<String,Object>> {

    @Override
    public HashMap<String, Object> generateSample() throws Exception {
        HashMap<String,Object> paramMap = new HashMap<>();
        paramMap.put("request_id", RandomID.id(32));
        paramMap.put("imei","imei_"+ ThreadLocalRandom.current().nextInt(1,100000));
        paramMap.put("adv_id","adv_"+ThreadLocalRandom.current().nextInt(1,100));
        paramMap.put("adv_type",ThreadLocalRandom.current().nextInt(1,2));
        long t1 = ThreadLocalRandom.current().nextInt(1,50);
        paramMap.put("step1_cost",t1);
        long t2 = ThreadLocalRandom.current().nextInt(1,100);
        paramMap.put("step2_cost",t2);
        long t3 = ThreadLocalRandom.current().nextInt(1,100);
        paramMap.put("step3_cost",t3);
        paramMap.put("cost",(t1 + t2 + t3));
        paramMap.put("state",ThreadLocalRandom.current().nextInt(1,2));
        paramMap.put("net",ThreadLocalRandom.current().nextInt(1,5));
        paramMap.put("os",ThreadLocalRandom.current().nextInt(1,2));
        paramMap.put("app_version",ThreadLocalRandom.current().nextInt(1,3) + "." + ThreadLocalRandom.current().nextInt(1,5) + "." + ThreadLocalRandom.current().nextInt(1,5));
        return paramMap;
    }
}
