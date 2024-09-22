package com.dtstep.lighthouse.ice.test.servant.rpc.http;

import com.dtstep.lighthouse.common.entity.ApiResultData;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.common.util.OkHttpUtil;
import com.dtstep.lighthouse.core.http.HttpProcessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class TestHttpService {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testHttpService() throws Exception {
        String token = "_demo_feed_behavior_stat";
        String secretKey = "dTdYSwzPz5GRMm1GDAMYKouGKoeD5IW8YVDiAAdH";
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("province", ThreadLocalRandom.current().nextInt(10));
        paramMap.put("score",ThreadLocalRandom.current().nextDouble(1000));
        paramMap.put("uuid","test_"+ThreadLocalRandom.current().nextInt(300));
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("token",token);
        objectNode.put("secretKey",secretKey);
        objectNode.put("timestamp",System.currentTimeMillis());
        objectNode.put("repeat",1);
        objectNode.putPOJO("params",paramMap);
        System.out.println(objectNode.toPrettyString());
//        for(int i=0;i<100;i++){
//            String response = OkHttpUtil.post("http://10.206.6.31:18105",objectNode.toString());
//
//            Thread.sleep(500);
//        }
        String response = OkHttpUtil.post("http://10.206.6.31:18101/api/rpc/v1/stat",objectNode.toString());
        System.out.println("response:" + response);
//        String body = objectNode.toString();
//        ApiResultData apiResultData = HttpProcessor.stat(body);
//        System.out.println("result:" + JsonUtil.toJSONString(apiResultData));

    }
}
