package com.dtstep.lighthouse.test.mode;

import com.dtstep.lighthouse.common.key.RandomID;
import com.dtstep.lighthouse.test.local.LocalUtil;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class PMFeedNewsBehaviorStatSample implements SimulationModalSample<HashMap<String,Object>> {

    @Override
    public HashMap<String, Object> generateSample() throws Exception {
        HashMap<String,Object> paramMap = new HashMap<>();
        paramMap.put("request_id", RandomID.id(30));
        paramMap.put("imei","imei_"+ ThreadLocalRandom.current().nextInt(100000));
        paramMap.put("recall_no","recall_"+ThreadLocalRandom.current().nextInt(1,30));
        paramMap.put("abtest_no","abtest_"+ThreadLocalRandom.current().nextInt(1,10));
        paramMap.put("rank_no","rank_"+ThreadLocalRandom.current().nextInt(1,10));
        paramMap.put("tab","tab_"+ThreadLocalRandom.current().nextInt(1,5));
        long n = ThreadLocalRandom.current().nextInt(100);
        if(n < 60){
            paramMap.put("behavior_type",1);
        }else if(n < 80){
            paramMap.put("behavior_type",2);
        }else if(n < 90){
            paramMap.put("behavior_type",3);
        }else if(n < 96){
            paramMap.put("behavior_type",4);
        }else{
            paramMap.put("behavior_type",5);
        }
        paramMap.put("item_id","item_"+ThreadLocalRandom.current().nextInt(1,100000));
        long cate = 100 + ThreadLocalRandom.current().nextInt(1,100);
        paramMap.put("top_level",String.valueOf(cate));
        paramMap.put("sec_level", cate + "_" + ThreadLocalRandom.current().nextInt(1, 20));
        LocalUtil.LocalEntity city = LocalUtil.getRandomCity();
        LocalUtil.LocalEntity province = LocalUtil.getById(city.getPid());
        paramMap.put("province",province.getId());
        paramMap.put("city",city.getId());
        paramMap.put("os",ThreadLocalRandom.current().nextInt(1,3));
        paramMap.put("app_version",ThreadLocalRandom.current().nextInt(1,3) + "." + ThreadLocalRandom.current().nextInt(1,5) + "." + ThreadLocalRandom.current().nextInt(1,5));
        return paramMap;
    }
}
