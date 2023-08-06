package com.dtstep.lighthouse.test.mode;

import com.dtstep.lighthouse.test.local.LocalUtil;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class BizOrderStatSample implements SimulationModalSample<HashMap<String,Object>>{

    @Override
    public HashMap<String, Object> generateSample() throws Exception {
        HashMap<String,Object> paramMap = new HashMap<>();
        paramMap.put("userId","user_" + ThreadLocalRandom.current().nextInt(10000));
        paramMap.put("orderId","order_" + ThreadLocalRandom.current().nextInt(100000));
        paramMap.put("supplierId","supplier_" + ThreadLocalRandom.current().nextInt(1000));
        paramMap.put("age",ThreadLocalRandom.current().nextInt(1,5));
        paramMap.put("sex",ThreadLocalRandom.current().nextInt(1,2));
        LocalUtil.LocalEntity city = LocalUtil.getRandomCity();
        LocalUtil.LocalEntity province = LocalUtil.getById(city.getPid());
        paramMap.put("province",province.getId());
        paramMap.put("city",city.getId());
        paramMap.put("os",ThreadLocalRandom.current().nextInt(1,2));
        double d = ThreadLocalRandom.current().nextDouble(1,9999);
        paramMap.put("amount",String.format("%.2f", d));
        paramMap.put("paymode",ThreadLocalRandom.current().nextInt(1,5));
        paramMap.put("version",ThreadLocalRandom.current().nextInt(1,3) + "." + ThreadLocalRandom.current().nextInt(1,5) + "." + ThreadLocalRandom.current().nextInt(1,5));
        return paramMap;
    }
}
