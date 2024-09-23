package com.dtstep.lighthouse.core.test.http;

import com.dtstep.lighthouse.client.LightHouse;
import com.dtstep.lighthouse.common.entity.ApiResultData;
import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.core.config.LDPConfig;
import com.dtstep.lighthouse.core.http.HttpProcessor;
import com.dtstep.lighthouse.core.test.CoreBaseTest;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class HttpProcessorTest extends CoreBaseTest {

    @Before
    public void before() throws Exception{
        System.out.println("before...");
    }

    @Test
    public void testStat() throws Exception{
        String text = "{\"params\":{\"imei\":\"2553889d-017a-4ef8-aa32-26751933ac8f\"},\"secretKey\":\"dTdYSwzPz5GRMm1GDAMYKouGKoeD5IW8YVDiAAdH\",\"timestamp\":1727061587634,\"token\":\"_demo_feed_behavior_stat\"}";
        ApiResultData apiResultData = HttpProcessor.stat(text);
        System.out.println("result:" + JsonUtil.toJSONString(apiResultData));
    }

    @Test
    public void testStats() throws Exception{
        String text = "";
    }

    @Test
    public void testDataQuery() throws Exception {
        Map<String,Object> requestMap = new HashMap<>();
        requestMap.put("statId","1100607");
        requestMap.put("secretKey","dTdYSwzPz5GRMm1GDAMYKouGKoeD5IW8YVDiAAdH");
        requestMap.put("startTime", DateUtil.getDayStartTime(System.currentTimeMillis()));
        requestMap.put("endTime", DateUtil.getDayEndTime(System.currentTimeMillis()));
        String requestData = JsonUtil.toJSONString(requestMap);
        System.out.println("requestData:" + requestData);
        ApiResultData apiResultData = HttpProcessor.dataQuery(requestData);
        System.out.println("result:" + JsonUtil.toJSONString(apiResultData));
    }
}
