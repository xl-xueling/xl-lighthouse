package com.dtstep.lighthouse.test.mode;

import com.dtstep.lighthouse.test.local.LocalUtil;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class OmAppStartDauStatSample implements SimulationModalSample<HashMap<String,Object>>{

    @Override
    public HashMap<String, Object> generateSample() throws Exception {
        HashMap<String,Object> paramMap = new HashMap<>();
        paramMap.put("imei","user_"+ ThreadLocalRandom.current().nextInt(1000));
        LocalUtil.LocalEntity city = LocalUtil.getRandomCity();
        LocalUtil.LocalEntity province = LocalUtil.getById(city.getPid());
        paramMap.put("province",province.getId());
        paramMap.put("city",city.getId());
        paramMap.put("os",ThreadLocalRandom.current().nextInt(2)+1);
        paramMap.put("net",ThreadLocalRandom.current().nextInt(5) + 1);
        paramMap.put("app_version",ThreadLocalRandom.current().nextInt(1,3) + "." + ThreadLocalRandom.current().nextInt(1,5) + "." + ThreadLocalRandom.current().nextInt(1,5));
        return paramMap;
    }
}
