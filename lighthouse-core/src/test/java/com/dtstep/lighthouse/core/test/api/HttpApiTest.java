package com.dtstep.lighthouse.core.test.api;

import com.dtstep.lighthouse.common.random.RandomID;
import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.common.util.OkHttpUtil;
import com.dtstep.lighthouse.core.batch.BatchAdapter;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class HttpApiTest {

    @Test
    public void testStat() throws Exception {
        String apiUrl = "http://10.206.6.31:18101/api/rpc/v1/stat";
        long t = System.currentTimeMillis();
        for(int i=0;i<676;i++){
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
            System.out.println("requestParams:" + JsonUtil.toJSONString(requestParams));
            String response = OkHttpUtil.post(apiUrl,requestParams);
            System.out.println("index:" + i + ",response:" + response.getBytes().length);
        }
        System.out.println("Send OK!");
    }

    @Test
    public void testStats() throws Exception{
        long t = System.currentTimeMillis();
        String apiUrl = "http://10.206.6.31:18101/api/rpc/v1/stats";
        for(int m=0;m<10;m++){
            List<Map<String,Object>> requestList = new ArrayList<>();
            for(int n=0;n<500;n++){
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
            String text = JsonUtil.toJSONString(requestList);
            System.out.println(text);
            String response = OkHttpUtil.post(apiUrl,text);
            System.out.println(response);
        }
        System.out.println("Send OK!");
    }

    @Test
    public void testDataQuery() throws Exception {
        String apiUrl = "http://10.206.6.31:18101/api/rpc/v1/dataQuery";
        Map<String,Object> requestMap = new HashMap<>();
        requestMap.put("statId","1100607");
        requestMap.put("secretKey","dTdYSwzPz5GRMm1GDAMYKouGKoeD5IW8YVDiAAdH");
        requestMap.put("startTime", DateUtil.getDayStartTime(System.currentTimeMillis()));
        requestMap.put("endTime", DateUtil.getDayEndTime(System.currentTimeMillis()));
        String requestParams = JsonUtil.toJSONString(requestMap);
        System.out.println("requestParams:" + JsonUtil.toJSONString(requestParams));
        String response = OkHttpUtil.post(apiUrl,requestParams);
        System.out.println(response);
    }

    @Test
    public void testDataQueryWithDimensList() throws Exception {
        String apiUrl = "http://10.206.6.31:18101/api/rpc/v1/dataQueryWithDimensList";
        Map<String,Object> requestMap = new HashMap<>();
        requestMap.put("statId","1100613");
        requestMap.put("secretKey","dTdYSwzPz5GRMm1GDAMYKouGKoeD5IW8YVDiAAdH");
        List<String> list = List.of("21","72","36");
        requestMap.put("dimensValueList",list);
        requestMap.put("startTime", DateUtil.getDayStartTime(System.currentTimeMillis()));
        requestMap.put("endTime", DateUtil.getDayEndTime(System.currentTimeMillis()));
        String requestParams = JsonUtil.toJSONString(requestMap);
        System.out.println("requestParams:" + JsonUtil.toJSONString(requestParams));
        String response = OkHttpUtil.post(apiUrl,requestParams);
        System.out.println(response);
    }

    @Test
    public void testLimitQuery() throws Exception{
        String apiUrl = "http://10.206.6.31:18101/api/rpc/v1/limitQuery";
        Map<String,Object> requestMap = new HashMap<>();
        requestMap.put("statId","1100617");
        requestMap.put("secretKey","dTdYSwzPz5GRMm1GDAMYKouGKoeD5IW8YVDiAAdH");
        long batchTime = DateUtil.batchTime(5, TimeUnit.MINUTES,DateUtil.getMinuteBefore(System.currentTimeMillis(),5));
        System.out.println("batchTime:" + DateUtil.formatTimeStamp(batchTime,"yyyy-MM-dd HH:mm:ss"));
        requestMap.put("batchTime", batchTime);
        String requestParams = JsonUtil.toJSONString(requestMap);
        System.out.println("requestParams:" + JsonUtil.toJSONString(requestParams));
        String response = OkHttpUtil.post(apiUrl,requestParams);
        System.out.println(response);
    }
}
