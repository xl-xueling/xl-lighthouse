package com.dtstep.lighthouse.common.util;

import org.junit.Test;

public class HttpUtilTest {

    @Test
    public void testReachable() throws Exception {
        String url = "http://baidu.com";
        boolean is = HttpUtil.isUrlReachable(url);
        System.out.println("is:" + is);
    }
}
