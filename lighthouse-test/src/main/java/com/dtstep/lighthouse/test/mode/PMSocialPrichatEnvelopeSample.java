package com.dtstep.lighthouse.test.mode;

import com.dtstep.lighthouse.test.local.LocalUtil;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class PMSocialPrichatEnvelopeSample implements SimulationModalSample<HashMap<String,Object>> {

    @Override
    public HashMap<String, Object> generateSample() throws Exception {
        HashMap<String,Object> paramMap = new HashMap<>();
        paramMap.put("enveloperId","enveloperid_"+ ThreadLocalRandom.current().nextInt(1000));
        paramMap.put("event_type",ThreadLocalRandom.current().nextInt(1,5));
        double d = ThreadLocalRandom.current().nextDouble(1,9999);
        paramMap.put("amount",String.format("%.2f", d));
        paramMap.put("sender_id","sender_"+ThreadLocalRandom.current().nextInt(1000));
        paramMap.put("receiver_id","receiver_"+ThreadLocalRandom.current().nextInt(1000));
        LocalUtil.LocalEntity city = LocalUtil.getRandomCity();
        LocalUtil.LocalEntity province = LocalUtil.getById(city.getPid());
        paramMap.put("province",province.getId());
        paramMap.put("city",city.getId());
        paramMap.put("sex",ThreadLocalRandom.current().nextInt(1,3));
        paramMap.put("age",ThreadLocalRandom.current().nextInt(1,6));
        return paramMap;
    }
}
