package com.dtstep.lighthouse.common.util;

import org.junit.Test;

public class OKhttpTest {

    @Test
    public void testRequest() throws Exception {
        String url = "http://10.206.6.31:18101/clusterInfo";
        String response = OkHttpUtil.get(url);
        System.out.println("resonse:" + response);
    }
}
