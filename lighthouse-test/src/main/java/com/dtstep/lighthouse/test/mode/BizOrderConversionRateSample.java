package com.dtstep.lighthouse.test.mode;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class BizOrderConversionRateSample implements SimulationModalSample<HashMap<String,Object>> {

    @Override
    public HashMap<String, Object> generateSample() throws Exception {
        HashMap<String,Object> paramMap = new HashMap<>();
        paramMap.put("orderId","order_"+ ThreadLocalRandom.current().nextInt(1,100000));
        long n = ThreadLocalRandom.current().nextInt(100);
        if(n < 60){
            paramMap.put("action_type",1);
        }else if(n < 80){
            paramMap.put("action_type",2);
        }else if(n < 90){
            paramMap.put("action_type",3);
        }else if(n < 96){
            paramMap.put("action_type",4);
        }else{
            paramMap.put("action_type",5);
        }
        paramMap.put("userId","user_" + ThreadLocalRandom.current().nextInt(1,100000));
        double d = ThreadLocalRandom.current().nextDouble(1,9999);
        paramMap.put("amount",String.format("%.2f", d));
        return paramMap;
    }
}
