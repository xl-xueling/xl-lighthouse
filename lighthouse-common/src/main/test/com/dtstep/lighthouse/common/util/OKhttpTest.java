package com.dtstep.lighthouse.common.util;

import org.junit.Test;

public class OKhttpTest {

    @Test
    public void testRequest() throws Exception {
        String url = "http://127.0.0.1:18101/clusterInfo";
        String response = OkHttpUtil.post(url,"");
        System.out.println("resonse:" + response);
    }
}
