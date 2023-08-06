package com.dtstep.lighthouse.test.mode;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class AiShortVideoTrainMonitorSample implements SimulationModalSample<HashMap<String,Object>> {

    @Override
    public HashMap<String, Object> generateSample() throws Exception {
        HashMap<String,Object> paramMap = new HashMap<>();
        paramMap.put("model_id","model_"+ ThreadLocalRandom.current().nextInt(1,100));
        paramMap.put("training_cost",ThreadLocalRandom.current().nextInt(3600,100000));
        paramMap.put("positive_size",ThreadLocalRandom.current().nextInt(1000,10000));
        paramMap.put("negative_size",ThreadLocalRandom.current().nextInt(10000,1000000));
        paramMap.put("auc",ThreadLocalRandom.current().nextDouble(0,0.99));
        paramMap.put("training_batch",ThreadLocalRandom.current().nextInt(1,10));
        paramMap.put("model_size",ThreadLocalRandom.current().nextInt(1000,1000000));
        paramMap.put("state",ThreadLocalRandom.current().nextInt(1,3));
        return paramMap;
    }
}
