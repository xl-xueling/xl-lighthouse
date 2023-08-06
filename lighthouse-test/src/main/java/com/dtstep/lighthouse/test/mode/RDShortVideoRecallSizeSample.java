package com.dtstep.lighthouse.test.mode;

import com.dtstep.lighthouse.common.key.RandomID;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class RDShortVideoRecallSizeSample implements SimulationModalSample<HashMap<String,Object>> {

    @Override
    public HashMap<String, Object> generateSample() throws Exception {
        HashMap<String,Object> paramMap = new HashMap<>();
        paramMap.put("request_id", RandomID.id(32));
        paramMap.put("recall_no","recall_" + ThreadLocalRandom.current().nextInt(1,100));
        paramMap.put("abtest_no","abtest_" + ThreadLocalRandom.current().nextInt(1,30));
        paramMap.put("item_size",ThreadLocalRandom.current().nextInt(5,10));
        return paramMap;
    }
}
