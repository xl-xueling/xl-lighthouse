package com.dtstep.lighthouse.test.mode;

import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.test.local.LocalUtil;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class PMSocialPrivateChatMsgStat implements SimulationModalSample<HashMap<String,Object>> {

    @Override
    public HashMap<String, Object> generateSample() throws Exception {
        HashMap<String,Object> paramMap = new HashMap<>();
        paramMap.put("userId","userID_"+ ThreadLocalRandom.current().nextInt(10000));
        paramMap.put("type",ThreadLocalRandom.current().nextInt(1,5));
        LocalUtil.LocalEntity city = LocalUtil.getRandomCity();
        LocalUtil.LocalEntity province = LocalUtil.getById(city.getPid());
        paramMap.put("province",province.getId());
        paramMap.put("city",city.getId());
        paramMap.put("sex",ThreadLocalRandom.current().nextInt(1,2));
        paramMap.put("age",ThreadLocalRandom.current().nextInt(1,5));
        paramMap.put("education",ThreadLocalRandom.current().nextInt(1,5));
        paramMap.put("os",ThreadLocalRandom.current().nextInt(1,2));
        paramMap.put("version",ThreadLocalRandom.current().nextInt(1,3) + "." + ThreadLocalRandom.current().nextInt(1,5) + "." + ThreadLocalRandom.current().nextInt(1,5));
        return paramMap;
    }
}
