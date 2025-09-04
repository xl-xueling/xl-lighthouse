package com.dtstep.lighthouse.common.util;

import com.dtstep.lighthouse.common.modal.HttpRequestConfig;
import com.dtstep.lighthouse.common.modal.KeyValue;
import com.dtstep.lighthouse.common.modal.RequestBodyDTO;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class OKhttpTest {

    @Test
    public void testRequest() throws Exception {
        String url = "http://10.206.6.31:18101/clusterInfo";
        String response = OkHttpUtil.get(url);
        System.out.println("resonse:" + response);
    }

    @Test
    public void testPost() throws Exception {
        String url = "http://test.com";
        long t1 = System.currentTimeMillis();
        String response = null;
        try{
            response = OkHttpUtil.post(url,"test");
        }catch (Exception ex){
            ex.printStackTrace();
        }
        long t2 = System.currentTimeMillis();
        System.out.println("response:" + response + ",cost:" + (t2 - t1));
    }

    @Test
    public void testPostFormData() throws IOException {
        // 1. 构造请求配置
        HttpRequestConfig config = new HttpRequestConfig();
        config.setMethod("POST");
        config.setUrl("http://localhost:3180/test");
        config.setParams(List.of(
                new KeyValue("key1", "value1"),
                new KeyValue("key2", "value2")
        ));
        config.setHeaders(List.of(
                new KeyValue("asss", "vvvvv")
        ));

        RequestBodyDTO body = new RequestBodyDTO();
        body.setType("x-www-form-urlencoded");
        body.setContent(List.of(
                new KeyValue("body1", "bodev2."),
                new KeyValue("body2", "bodyv3")
        ));
        config.setBody(body);
        String resp = OkHttpUtil.request(config);
        System.out.println("Response = " + resp);
    }

    @Test
    public void testPostJson() throws IOException {
        HttpRequestConfig config = new HttpRequestConfig();
        config.setMethod("POST");
        config.setUrl("http://localhost:3180/test");
        RequestBodyDTO body = new RequestBodyDTO();
        body.setType("json");
        body.setJson("{\"foo\":\"bar\",\"num\":123}");
        config.setBody(body);
        String resp = OkHttpUtil.request(config);
        System.out.println("Response = " + resp);
    }

    @Test
    public void testRequest2() throws Exception {
        String s = "{\"method\":\"POST\",\"url\":\"http://10.206.6.47:3180/test\",\"params\":[{\"key\":\"ssss\",\"value\":\"sssgds\"}],\"headers\":[{\"key\":\"header1\",\"value\":\"header2\"},{\"key\":\"hader3\",\"value\":\"header4\"}],\"body\":{\"type\":\"form-data\",\"content\":[{\"key\":\"aaa\",\"value\":\"bbb\"},{\"key\":\"aaa2\",\"value\":\"bbbb2\"},{\"key\":\"aaa3\",\"value\":\"bbb3\"}]}}";
        HttpRequestConfig config = JsonUtil.toJavaObject(s,HttpRequestConfig.class);
        String resp = OkHttpUtil.request(config);
        System.out.println("Response = " + resp);
    }
}
