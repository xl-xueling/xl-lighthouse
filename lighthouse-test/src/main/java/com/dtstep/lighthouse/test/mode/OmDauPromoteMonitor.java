package com.dtstep.lighthouse.test.mode;

import com.dtstep.lighthouse.test.local.LocalUtil;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class OmDauPromoteMonitor implements SimulationModalSample<HashMap<String,Object>>{

    @Override
    public HashMap<String, Object> generateSample() throws Exception {
        HashMap<String,Object> paramMap = new HashMap<>();
        paramMap.put("imei","imei_"+ ThreadLocalRandom.current().nextInt(1,100000));
        LocalUtil.LocalEntity city = LocalUtil.getRandomCity();
        LocalUtil.LocalEntity province = LocalUtil.getById(city.getPid());
        paramMap.put("province",province.getId());
        paramMap.put("city",city.getId());
        paramMap.put("from",ThreadLocalRandom.current().nextInt(1,10));
        return paramMap;
    }
}
