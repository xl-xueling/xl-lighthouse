package com.dtstep.lighthouse.common.util;

import org.junit.Test;

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
}
