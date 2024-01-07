package com.dtstep.lighthouse.insights.test.utils;

import com.dtstep.lighthouse.common.util.DateUtil;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class TestDateUtil {

    @Test
    public void test() throws Exception {
        long t = TimeUnit.DAYS.toSeconds(7);
        System.out.println("t:" + t);
    }
}
