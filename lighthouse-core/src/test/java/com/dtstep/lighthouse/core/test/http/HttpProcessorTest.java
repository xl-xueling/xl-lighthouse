package com.dtstep.lighthouse.core.test.http;

import com.dtstep.lighthouse.client.LightHouse;
import com.dtstep.lighthouse.common.entity.ApiResultData;
import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.core.config.LDPConfig;
import com.dtstep.lighthouse.core.http.HttpProcessor;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class HttpProcessorTest {

    static {
        try{
            LDPConfig.loadConfiguration();
            LightHouse.init("10.206.6.31:4061");
        }catch (Exception ex){
            ex.printStackTrace();
        }
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
