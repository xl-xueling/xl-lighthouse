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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpProcessorTest extends CoreBaseTest {

    private final String callerName = "caller:app_waimai_order";

    private final String callerKey = "bK7dImv3HCzBBTQvS9pvlieSfXlVeyMDbWs8RNmj";

    @Test
    public void testDataQuery() throws Exception {
        Map<String,Object> requestMap = new HashMap<>();
        requestMap.put("statId","1100607");
        requestMap.put("startTime", DateUtil.getDayStartTime(System.currentTimeMillis()));
        requestMap.put("endTime", DateUtil.getDayEndTime(System.currentTimeMillis()));
        List<Long> batchList = new ArrayList<>();
        batchList.add(System.currentTimeMillis());
        requestMap.put("batchList",batchList);
        String requestData = JsonUtil.toJSONString(requestMap);
        System.out.println("requestData:" + requestData);
        ApiResultData apiResultData = HttpProcessor.dataQuery(callerName,callerKey,requestData);
        System.out.println("apiResultData is:" + JsonUtil.toJSONString(apiResultData));
    }
}
