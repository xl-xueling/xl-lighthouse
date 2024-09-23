package com.dtstep.lighthouse.core.test.api;

import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.common.util.OkHttpUtil;
import org.junit.Test;

import java.util.*;

public class HttpApiTest {

    @Test
    public void testStat() throws Exception {
        String apiUrl = "http://10.206.6.31:18101/api/rpc/v1/stat";
        Map<String,Object> requestMap = new HashMap<>();
        requestMap.put("token","_demo_feed_behavior_stat");
        requestMap.put("secretKey","dTdYSwzPz5GRMm1GDAMYKouGKoeD5IW8YVDiAAdH");
        requestMap.put("timestamp",System.currentTimeMillis());
        Map<String,Object> paramsMap = new HashMap<>();
        paramsMap.put("imei", UUID.randomUUID());
        paramsMap.put("behavior_type","3");
        requestMap.put("params",paramsMap);
        String requestParams = JsonUtil.toJSONString(requestMap);
        System.out.println("requestParams:" + JsonUtil.toJSONString(requestParams));
        String response = OkHttpUtil.post(apiUrl,requestParams);
        System.out.println(response);
    }

    @Test
    public void testStats() throws Exception{
        String apiUrl = "http://10.206.6.31:18101/api/rpc/v1/stats";
        List<Map<String,Object>> requestList = new ArrayList<>();
        for(int i=0;i<10;i++){
            Map<String,Object> requestMap = new HashMap<>();
            requestMap.put("token","_demo_feed_behavior_stat");
            requestMap.put("secretKey","dTdYSwzPz5GRMm1GDAMYKouGKoeD5IW8YVDiAAdH");
            requestMap.put("timestamp",System.currentTimeMillis());
            Map<String,Object> paramsMap = new HashMap<>();
            paramsMap.put("imei", UUID.randomUUID());
            paramsMap.put("behavior_type","3");
            requestMap.put("params",paramsMap);
            requestList.add(requestMap);
        }
        String text = JsonUtil.toJSONString(requestList);
        System.out.println(text);
        String response = OkHttpUtil.post(apiUrl,text);
        System.out.println(response);
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


}
