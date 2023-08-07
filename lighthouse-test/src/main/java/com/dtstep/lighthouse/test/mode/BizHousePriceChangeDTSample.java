package com.dtstep.lighthouse.test.mode;

import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.test.local.LocalUtil;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class BizHousePriceChangeDTSample implements SimulationModalSample<HashMap<String,Object>>{

    @Override
    public HashMap<String, Object> generateSample() throws Exception {
        HashMap<String,Object> paramMap = new HashMap<>();
        paramMap.put("houseId","house_"+ ThreadLocalRandom.current().nextInt(10000));
        LocalUtil.LocalEntity city = LocalUtil.getRandomCity();
        LocalUtil.LocalEntity province = LocalUtil.getById(city.getPid());
        paramMap.put("province",province.getId());
        paramMap.put("city",city.getId());
        String townId = city.getId() + "_" + ThreadLocalRandom.current().nextInt(5);
        paramMap.put("town",townId);
        String districtId = townId + "_" + ThreadLocalRandom.current().nextInt(10);
        paramMap.put("district",districtId);
        paramMap.put("change_type",ThreadLocalRandom.current().nextInt(1,3));
        paramMap.put("rate",((double) (ThreadLocalRandom.current().nextInt(1,5)))/ 10.0d);
        paramMap.put("quality",ThreadLocalRandom.current().nextInt(1,5));
        return paramMap;
    }
}
