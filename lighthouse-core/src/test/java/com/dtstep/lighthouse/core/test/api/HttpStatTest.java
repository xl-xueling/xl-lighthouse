package com.dtstep.lighthouse.core.test.api;

import com.dtstep.lighthouse.common.random.RandomID;
import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.common.util.OkHttpUtil;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class HttpStatTest {

    @Test
    public void testStat() throws Exception {
        String apiUrl = "http://10.206.6.31:18101/api/rpc/v1/stat";
        long t = System.currentTimeMillis();
        for(int i=0;i<1676;i++){
            Map<String,Object> requestMap = new HashMap<>();
            requestMap.put("token","N4C:order_stat");
            requestMap.put("secretKey","YEWU3tGjNQL1AevvC9FjNj9SCuvzpYPmLY5akKYz");
            requestMap.put("timestamp",t);
            Map<String,Object> paramsMap = new HashMap<>();
            paramsMap.put("order_id", RandomID.id(6));
            paramsMap.put("biz", RandomID.id(2));
            paramsMap.put("user_id", RandomID.id(6));
            Double d = ThreadLocalRandom.current().nextDouble(1000);
            paramsMap.put("amount",String.format("%.3f", d));//防止上面随机数出现科学计数法
            requestMap.put("params",paramsMap);
            String requestParams = JsonUtil.toJSONString(requestMap);
            System.out.println("Send Params:" + JsonUtil.toJSONString(requestParams));
            String response = OkHttpUtil.post(apiUrl,requestParams);
            System.out.println("Send Index:" + i + ",response:" + response);
        }
        System.out.println("Send OK!");
    }

    @Test
    public void testStats() throws Exception{
        long t = System.currentTimeMillis();
        String apiUrl = "http://10.206.6.31:18101/api/rpc/v1/stats";
        for(int m=0;m<100;m++){
            List<Map<String,Object>> requestList = new ArrayList<>();
            for(int n=0;n<300;n++){
                Map<String,Object> requestMap = new HashMap<>();
                requestMap.put("token","N4C:order_stat");
                requestMap.put("secretKey","YEWU3tGjNQL1AevvC9FjNj9SCuvzpYPmLY5akKYz");
                requestMap.put("timestamp",t);
                Map<String,Object> paramsMap = new HashMap<>();
                paramsMap.put("order_id", RandomID.id(6));
                paramsMap.put("biz", RandomID.id(2));
                paramsMap.put("user_id", RandomID.id(6));
                Double d = ThreadLocalRandom.current().nextDouble(1000);
                paramsMap.put("amount",String.format("%.3f", d));//防止上面随机数出现科学计数法
                requestMap.put("params",paramsMap);
                requestList.add(requestMap);
            }
            String requestParams = JsonUtil.toJSONString(requestList);
            System.out.println("Send Params:" + JsonUtil.toJSONString(requestParams));
            String response = OkHttpUtil.post(apiUrl,requestParams);
            System.out.println("Send Index:" + m + ",response:" + response);
        }
        System.out.println("Send OK!");
    }
}
