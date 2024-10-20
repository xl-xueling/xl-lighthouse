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
        String text = "{\n" +
                "\t\"params\": {\n" +
                "\t\t\"behavior_type\": \"3\",\n" +
                "\t\t\"imei\": \"9dad2cd3-0c58-444e-a31a-832308a9f8c5\"\n" +
                "\t},\n" +
                "\t\"secretKey\": \"dTdYSwzPz5GRMm1GDAMYKouGKoeD5IW8YVDiAAdH\",\n" +
                "\t\"timestamp\": 1727075686477,\n" +
                "\t\"token\": \"_demo_feed_behavior_stat\"\n" +
                "}";
        ApiResultData apiResultData = HttpProcessor.stat(text);
        System.out.println("result:" + JsonUtil.toJSONString(apiResultData));
    }

    @Test
    public void testStats() throws Exception{
        String text = "";
    }

    private final String callerName = "caller:app_waimai_order";

    private final String callerKey = "bK7dImv3HCzBBTQvS9pvlieSfXlVeyMDbWs8RNmj";

    @Test
    public void testDataQuery() throws Exception {
        Map<String,Object> requestMap = new HashMap<>();
        requestMap.put("statId","1100607");
        requestMap.put("startTime", DateUtil.getDayStartTime(System.currentTimeMillis()));
        requestMap.put("endTime", DateUtil.getDayEndTime(System.currentTimeMillis()));
        String requestData = JsonUtil.toJSONString(requestMap);
        System.out.println("requestData:" + requestData);
        ApiResultData apiResultData = HttpProcessor.dataQuery(callerName,callerKey,requestData);
        System.out.println("apiResultData is:" + JsonUtil.toJSONString(apiResultData));
    }
}
