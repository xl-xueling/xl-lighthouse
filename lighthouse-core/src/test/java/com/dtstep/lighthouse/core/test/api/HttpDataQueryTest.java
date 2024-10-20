package com.dtstep.lighthouse.core.test.api;

import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.common.util.OkHttpUtil;
import okhttp3.*;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Http 数据查询API调用示例
 * 参考文档：
 */
public class HttpDataQueryTest {

    private static final OkHttpClient client = new OkHttpClient();

    private static final String callerName = "caller:app_waimai_order";

    private static final String callerKey = "bK7dImv3HCzBBTQvS9pvlieSfXlVeyMDbWs8RNmj";


    /**
     * 根据批次时间查询单个维度的统计结果
     * Header参数：Caller-Name和Caller-Key
     * Body参数格式：{"batchList":[1725498000000,1725494400000,1725490800000,1725487200000,1725483600000],"dimensValue":"山东省","statId":"1100607"}
     * @throws Exception
     */
    @Test
    public void testDataQuery() throws Exception {
        String apiUrl = "http://10.206.6.31:18101/api/rpc/v1/dataQuery";
        Map<String,Object> requestMap = new HashMap<>();
        requestMap.put("statId","1100607");
        List<Long> batchList = new ArrayList<>();
        batchList.add(DateUtil.parseDate("2024-09-05 09:00:00","yyyy-MM-dd HH:mm:ss"));
        batchList.add(DateUtil.parseDate("2024-09-05 08:00:00","yyyy-MM-dd HH:mm:ss"));
        batchList.add(DateUtil.parseDate("2024-09-05 07:00:00","yyyy-MM-dd HH:mm:ss"));
        batchList.add(DateUtil.parseDate("2024-09-05 06:00:00","yyyy-MM-dd HH:mm:ss"));
        batchList.add(DateUtil.parseDate("2024-09-05 05:00:00","yyyy-MM-dd HH:mm:ss"));
        requestMap.put("dimensValue","山东省");
        requestMap.put("batchList",batchList);
        String requestParams = JsonUtil.toJSONString(requestMap);
        System.out.println("Body Params:" + JsonUtil.toJSONString(requestParams));
        RequestBody body = RequestBody.create(MediaType.parse("application/json"),requestParams);
        Request request = new Request.Builder()
                .header("Caller-Name",callerName)
                .header("Caller-Key",callerKey)
                .url(apiUrl)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            System.out.println(response.body().string()) ;
        }
    }

    /**
     * 根据时间范围查询单个维度的统计结果
     *
     * Header参数：Caller-Name和Caller-Key
     * Body参数格式：{"dimensValue":"山东省","endTime":1729439999999,"startTime":1729353600000,"statId":"1100607"}
     * @throws Exception
     */
    @Test
    public void testDataDurationQuery() throws Exception {
        String apiUrl = "http://10.206.6.31:18101/api/rpc/v1/dataDurationQuery";
        Map<String,Object> requestMap = new HashMap<>();
        requestMap.put("statId","1100607");
        requestMap.put("startTime", DateUtil.getDayStartTime(System.currentTimeMillis()));
        requestMap.put("endTime", DateUtil.getDayEndTime(System.currentTimeMillis()));
        requestMap.put("dimensValue","山东省");
        String requestParams = JsonUtil.toJSONString(requestMap);
        System.out.println("Body Params:" + JsonUtil.toJSONString(requestParams));
        RequestBody body = RequestBody.create(MediaType.parse("application/json"),requestParams);
        Request request = new Request.Builder()
                .header("Caller-Name",callerName)
                .header("Caller-Key",callerKey)
                .url(apiUrl)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            System.out.println(response.body().string()) ;
        }
    }

    /**
     * 按照批次时间批量查询多个维度的数据
     * Header参数：Caller-Name和Caller-Key
     * 包体参数格式：{"batchList":[1725498000000,1725494400000,1725490800000,1725487200000,1725483600000],"dimensValueList2":["21","72","36"],"statId":"1100613"}
     * @throws Exception
     */
    @Test
    public void testDataQueryWithDimensList() throws Exception {
        String apiUrl = "http://10.206.6.31:18101/api/rpc/v1/dataQueryWithDimensList";
        Map<String,Object> requestMap = new HashMap<>();
        requestMap.put("statId","1100613");
        List<String> list = List.of("21","72","36");
        requestMap.put("dimensValueList",list);
        List<Long> batchList = new ArrayList<>();
        batchList.add(DateUtil.parseDate("2024-09-05 09:00:00","yyyy-MM-dd HH:mm:ss"));
        batchList.add(DateUtil.parseDate("2024-09-05 08:00:00","yyyy-MM-dd HH:mm:ss"));
        batchList.add(DateUtil.parseDate("2024-09-05 07:00:00","yyyy-MM-dd HH:mm:ss"));
        batchList.add(DateUtil.parseDate("2024-09-05 06:00:00","yyyy-MM-dd HH:mm:ss"));
        batchList.add(DateUtil.parseDate("2024-09-05 05:00:00","yyyy-MM-dd HH:mm:ss"));
        requestMap.put("batchList",batchList);
        String requestParams = JsonUtil.toJSONString(requestMap);
        System.out.println("requestParams:" + JsonUtil.toJSONString(requestParams));
        RequestBody body = RequestBody.create(MediaType.parse("application/json"),requestParams);
        Request request = new Request.Builder()
                .header("Caller-Name",callerName)
                .header("Caller-Key",callerKey)
                .url(apiUrl)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            System.out.println(response.body().string()) ;
        }
    }

    /**
     * 按照起止时间批量查询多个维度的数据
     * Header参数：Caller-Name和Caller-Key
     * 包体参数格式：{"dimensValueList":["21","72","36"],"endTime":1729439999999,"startTime":1729353600000,"statId":"1100613"}
     * @throws Exception
     */
    @Test
    public void testDataDurationQueryWithDimensList() throws Exception {
        String apiUrl = "http://10.206.6.31:18101/api/rpc/v1/dataDurationQueryWithDimensList";
        Map<String,Object> requestMap = new HashMap<>();
        requestMap.put("statId","1100613");
        List<String> list = List.of("21","72","36");
        requestMap.put("dimensValueList",list);
        requestMap.put("startTime", DateUtil.getDayStartTime(System.currentTimeMillis()));
        requestMap.put("endTime", DateUtil.getDayEndTime(System.currentTimeMillis()));
        String requestParams = JsonUtil.toJSONString(requestMap);
        System.out.println("requestParams:" + JsonUtil.toJSONString(requestParams));
        RequestBody body = RequestBody.create(MediaType.parse("application/json"),requestParams);
        Request request = new Request.Builder()
                .header("Caller-Name",callerName)
                .header("Caller-Key",callerKey)
                .url(apiUrl)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            System.out.println(response.body().string()) ;
        }
    }

    /**
     * 按照批次时间查询Limit统计结果数据
     * Header参数：Caller-Name和Caller-Key
     * 包体参数格式：{"batchTime":1729415100000,"secretKey":"dTdYSwzPz5GRMm1GDAMYKouGKoeD5IW8YVDiAAdH","statId":"1100617"}
     * @throws Exception
     */
    @Test
    public void testLimitQuery() throws Exception{
        String apiUrl = "http://10.206.6.31:18101/api/rpc/v1/limitQuery";
        Map<String,Object> requestMap = new HashMap<>();
        requestMap.put("statId","1100617");
        long batchTime = DateUtil.batchTime(5, TimeUnit.MINUTES,DateUtil.getMinuteBefore(System.currentTimeMillis(),5));
        System.out.println("batchTime:" + DateUtil.formatTimeStamp(batchTime,"yyyy-MM-dd HH:mm:ss"));
        requestMap.put("batchTime", batchTime);
        String requestParams = JsonUtil.toJSONString(requestMap);
        System.out.println("requestParams:" + JsonUtil.toJSONString(requestParams));
        RequestBody body = RequestBody.create(MediaType.parse("application/json"),requestParams);
        Request request = new Request.Builder()
                .header("Caller-Name",callerName)
                .header("Caller-Key",callerKey)
                .url(apiUrl)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            System.out.println(response.body().string()) ;
        }
    }
}
